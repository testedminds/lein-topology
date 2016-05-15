(ns leiningen.topology
  (:require [leiningen.core.eval :as lein]))

(defn topology [project & args]
  (println "Project name: " (:name project))
  (lein/eval-in-project (update-in project [:dependencies] conj '[lein-topology "0.1.0-SNAPSHOT"])
                        `(topology.core/all-ns->edgelist-csv ~(:name project) ~@(:source-paths project) topology.namespaces/namespaces)
                        `(require 'topology.core 'topology.namespaces))
  (println "That's all for now..."))
