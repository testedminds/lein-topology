(ns leiningen.topology
  (:require [leiningen.core.eval :as lein]))

(defn topology [project & args]
  (lein/eval-in-project (update-in project [:dependencies] conj
                                   '[lein-topology "0.1.0-SNAPSHOT"]
                                   '[org.clojure/clojure "1.8.0"])
                        `(topology.core/print-weighted-edges (topology.core/all-ns->fn-edges ~@(:source-paths project)))
                        `(require 'topology.core)))
