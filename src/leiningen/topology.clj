(ns leiningen.topology
  (:require [leiningen.core.eval :as lein]))

(defn topology
  "Generate the project's function dependency structure matrix."
  [project & args]
  (lein/eval-in-project (update-in project [:dependencies] conj
                                   '[lein-topology "0.3.0-SNAPSHOT"]
                                   '[org.clojure/clojure "1.8.0"])
                        `(topology.printer/print-weighted-edges
                          (topology.edgelist/dirs->fn-edges
                           ~@(flatten (conj (:source-paths project) (:test-paths project)))))
                        `(require 'topology.printer 'topology.edgelist)))
