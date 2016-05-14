(ns topology.core
  (:require [clojure.tools.analyzer.jvm :as jvm]
            [clojure.tools.analyzer.ast :as ast]))

(defn- with-vars
  [node]
  (let [children-vars (mapcat :vars (ast/children node))]
    (assoc node :vars (if (= :var (:op node))
                        (conj children-vars (clojure.string/replace (str (:var node)) #"#'" ""))
                        children-vars))))

(defn- ns->defs
  [ns-str]
  (filter #(= :def (:op %)) (jvm/analyze-ns (symbol ns-str))))

(defn ns->adjacency-list
  [ns-str]
  (reduce #(assoc %1
            (str ns-str "/" (get-in %2 [:name]))
            (:vars (ast/postwalk %2 with-vars)))
          {}
          (ns->defs ns-str)))

(defn- edges
  [ns-str node]
  (let [outv (str ns-str "/" (get-in node [:name]))
        invs (:vars (ast/postwalk node with-vars))]
    (mapv #(vector outv %) invs)))

(defn ns->edges
  [ns-str]
  (mapcat #(edges ns-str %) (ns->defs ns-str)))

(defn edges->csv
  [file edges]
  (with-open [w (clojure.java.io/writer file)]
    (doseq [[outv inv] edges]
      (.write w (str outv "," inv))
      (.newLine w))))

(defn- ignored? [[outv inv] filter-set]
  (let [inv-nspc (clojure.string/split inv #"/")]
    (some filter-set inv-nspc)))

(defn ns->edgelist
  ([ns-str] (ns->edgelist ns-str #{}))
  ([ns-str filter-set]
   (let [e (ns->edges ns-str)]
     (remove #(ignored? % filter-set) e))))

(defn ns->edgelist-csv
  ([ns-str file] (ns->edgelist-csv file #{}))
  ([ns-str file filter-set]
   (->> (ns->edgelist ns-str filter-set)
        (edges->csv file))))

(defn all-ns->edgelist-csv [project namespaces ignored]
  (println (seq (.getURLs (java.lang.ClassLoader/getSystemClassLoader))))
  (doseq [nspc namespaces]
    (println nspc)
    (ns->edgelist-csv nspc (str "/tmp/" project "/" nspc ".csv") ignored)))
