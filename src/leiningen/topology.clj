(ns leiningen.topology
  (:require [leiningen.core.eval :as lein]))

;; TODO: Might need to remove the version of clojure included by the project.
(defn topology [project & args]
  (lein/eval-in-project (update-in project [:dependencies] conj
                                   '[lein-topology "0.1.0-SNAPSHOT"]
                                   '[org.clojure/clojure "1.8.0"])
                        `(topology.core/all-ns->edgelist-csv "/tmp/foo" ~@(:source-paths project))
                        `(require 'topology.core)))
