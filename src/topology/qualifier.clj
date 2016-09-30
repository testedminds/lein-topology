(ns topology.qualifier)

(defn- fq-ns
  "Returns the fully qualified namespace of the given symbol s in namespace ns."
  ([ns s]
   (if-let [rns (try (-> (ns-resolve ns s) meta :ns)
                     (catch Exception e
                       (.println *err* (str "Could not resolve: " ns "/" s))))]
     (symbol (str rns) (name s))
     s)))

(defn- all-fq
  "Get all symbols for the dependencies..."
  [ds]
  (reduce
   (fn [r [f sc]]
     (assoc r
            f (map (partial fq-ns (symbol (namespace f))) sc)))
   {}
   ds))

(defn filter-fully-qualified
  "Only keep fully-qualified functions, and ignore the source function itself..."
  [ds]
  (reduce
   (fn [r [f sc]]
     (assoc r
            f (filter #(and (namespace %) (not= % f)) sc)))
   {}
   (all-fq ds)))
