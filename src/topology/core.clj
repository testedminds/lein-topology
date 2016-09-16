(ns topology.core
  (:require [clojure.string :as str]
            [topology.dependencies :as td]
            [topology.finder :as tf]))

(defn print-weighted-edges
  [edges]
  (doseq [[[outv inv] w] edges]
    (println (str/join "," [outv inv w]))))

(defn all-ns->fn-edges
  [& source-paths]
  (->> source-paths
       tf/source-paths->namespaces
       (mapcat td/ns->edges)
       frequencies))
