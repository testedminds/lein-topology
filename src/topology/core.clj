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

(defn all-ns->edgelist-csv [project namespaces]
  ;; (println (seq (.getURLs (java.lang.ClassLoader/getSystemClassLoader))))
  (doseq [nspc namespaces]
    (println nspc)
    (->> (ns->edges nspc)
         (edges->csv (str "/tmp/" project "/" nspc ".csv")))))
