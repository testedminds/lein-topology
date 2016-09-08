(ns topology.dependencies
  (:require
   [clojure.repl :as repl]
   [topology.symbols :as ts]))

(defn- interns
  "Returns metadata for all the interns in a namespace."
  [ns]
  (try
    (require ns)
    (map meta (vals (ns-interns ns)))
    (catch Exception e
      (.println *err* e))))

(defn- sources
  "Given a sequence of metadata maps, returns a vector of [name source-string]."
  [vars nspc]
  (filter second
          (map vector
               (map :name vars)
               (map (comp repl/source-fn symbol (partial str nspc \/) :name) vars))))

(defn- fq-ns
  "Returns the fully qualified namespace of the given symbol s in namespace ns."
  ([ns s]
   (if-let [rns (try (-> (ns-resolve ns s) meta :ns)
                     (catch Exception e
                       (.println *err* (str "Could not resolve: " ns "/" s))))]
     (symbol (str rns) (name s))
     s)))

(defn- dependencies
  "Returns all functions used by each function in the given namespace."
  [nspc srcs]
  (into
   {}
   (filter (comp not-empty second)
           (map
            (fn [[fn-name source]]
              [(symbol (str nspc) (str fn-name)) (ts/symbols (read-string source))])
            srcs))))

(defn- filtered
  "Only keep fully-qualified functions, and ignore the source function itself..."
  [ds]
  (reduce
   (fn [r [f sc]]
     (assoc r
            f (filter #(and (namespace %) (not= % f)) sc)))
   {}
   ds))

(defn- all-fq
  "Get all symbols for the dependencies..."
  [ds]
  (reduce
   (fn [r [f sc]]
     (assoc r
            f (map (partial fq-ns (symbol (namespace f))) sc)))
   {}
   ds))

(defn ns->fn-dep-map [nspc]
  (let [vars (interns nspc)
        srcs (sources vars nspc)]
    (filtered (all-fq (dependencies nspc srcs)))))

(defn ns->edges [nspc]
  (mapcat (fn [[f deps]]
            (map (fn [x] [f x]) deps))
          (ns->fn-dep-map nspc)))
