(ns topology.analyzer
  (:require [clojure.tools.analyzer.jvm :as jvm]
            [clojure.tools.analyzer.ast :as ast]
            [clojure.string :as str]
            [topology.clique :as c]))

(defn- dependencies
  [node]
  (let [child-vars (mapcat :vars (ast/children node))]
    (assoc node :vars (if (= :var (:op node))
                        (conj child-vars (str/replace (str (:var node)) #"#'" ""))
                        child-vars))))

(defn- edges
  [ns-str node]
  (let [outv (str ns-str "/" (:name node))
        invs (:vars (ast/postwalk node dependencies))]
    (mapv #(vector outv %) invs)))

(defn- ns->defs
  [ns-str]
  (filter #(= :def (:op %)) (jvm/analyze-ns ns-str)))

(defn ns->edges
  [ns-str]
  (mapcat #(edges ns-str %) (ns->defs ns-str)))

;; classpath
(defn broken-ns->edges [ns-str]
  (mapcat (fn [[f deps]] (map (fn [x] [f x]) deps))
          (c/filtered (c/all-fq (c/dependencies ns-str)))))

;; a test def
(def foobar (map #(* 2 %) (range 10)))

;; a test defn that includes a macro
(defn testing [foo]
  (when foo (println "bar! baz!")))
