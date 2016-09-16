>(ns topology.dependencies
  (:require
   [clojure.repl :as repl]
   [topology.symbols :as ts]
   [topology.qualifier :as tq]))

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
  [nspc vars]
  (filter second
          (map vector
               (map :name vars)
               (map (comp repl/source-fn symbol (partial str nspc \/) :name) vars))))

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

(defn ns->fn-dep-map
  [nspc]
  (->> nspc
       interns
       (sources nspc)
       (dependencies nspc)
       tq/filter-fully-qualified))

(defn ns->edges [nspc]
  (mapcat (fn [[f deps]]
            (map (fn [x] [f x]) deps))
          (ns->fn-dep-map nspc)))
