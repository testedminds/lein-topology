(ns topology.clique
  (:require
   [clojure.repl :as repl]
   [topology.symbols :as ts]))

(defn functions
  "Returns all the functions in the namespace ns"
  [ns]
  (try
    ((fn []
       (require ns)
       (map meta (vals (ns-interns ns)))))
    (catch Exception e
      (.println *err* e))))

(defn fq-ns
  "Returns the fully qualified namespace of the given symbol s in namespace ns"
  ([ns s]
   (if-let [rns (try (-> (ns-resolve ns s) meta :ns)
                     (catch Exception e
                       (.println *err* (str "Could not resolve: " ns "/" s))
                       nil))]
     (symbol (str rns) (name s))
     s)))

(defn sources [fxns nspc]
  (filter second
          (map vector
               (map :name fxns)
               (map (comp repl/source-fn symbol (partial str nspc \/) :name) fxns))))

(defn dependencies
  "Returns all functions used by each function in the given namespace"
  [nspc fns]
  (into
   {}
   (filter (comp not-empty second)
           (map
            (fn [[fn-name source]]
              [(symbol (str nspc) (str fn-name)) (ts/symbols (read-string source))])
            (sources fns nspc)))))

(defn filtered
  "Only keep fully-qualified functions, and ignore the source function itself..."
  [ds]
  (reduce
   (fn [r [f sc]]
     (assoc r
            f (filter #(and (namespace %) (not= % f)) sc)))
   {}
   ds))

(defn all-fq
  "Get all symbols for the dependencies..."
  [ds]
  (reduce
   (fn [r [f sc]]
     (assoc r
            f (map (partial fq-ns (symbol (namespace f))) sc)))
   {}
   ds))

(defn ns->edges [ns-str]
  (mapcat (fn [[f deps]] (map (fn [x] [f x]) deps))
          (filtered (all-fq (dependencies ns-str (functions ns-str))))))
