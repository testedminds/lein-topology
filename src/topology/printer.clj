(ns topology.printer
  (:require [clojure.string :as str]))

(defn print-weighted-edges
  [edges]
  (doseq [[[outv inv] w] edges]
    (println (str/join "," [outv inv w]))))
