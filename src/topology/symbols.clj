(ns topology.symbols
  (:require
   [clojure.zip :as zip]))

(defn- seq-map-zip [x]
  (zip/zipper
   (fn [n] (or (seq? n) (map? n) (vector? n)))
   (fn [b] (if (map? b) (seq b) b))
   (fn [node children] (with-meta children (meta node)))
   x))

(defn- zip-nodes [x]
  (take-while (complement zip/end?) (iterate zip/next (seq-map-zip x))))

(defn symbols [source-str]
  (filter symbol? (map zip/node (zip-nodes source-str)))) ; also returns Java classes
