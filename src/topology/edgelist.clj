(ns topology.edgelist
  (:require [topology.dependencies :as td]
            [topology.finder :as tf]))

(defn- interleave-keys
  [[k vs]]
  (map (fn [v] [k v]) vs))

(defn- ns->edgelist
  [nspc]
  (mapcat interleave-keys (td/ns->fn-dep-map nspc)))

(defn dirs->fn-edges
  "Returns a map of edges to weights: {[source target] weight}"
  [& source-paths]
  (->> source-paths
       tf/source-paths->namespaces
       (mapcat ns->edgelist)
       frequencies))
