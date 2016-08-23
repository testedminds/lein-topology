(ns topology.core-test
  (:require [clojure.test :refer :all]
            [topology.core :refer :all]))

;; Edges are represented as a map of edges to weights:
;; {[source target] weight}
(def edges (all-ns->fn-edges "./test/resources"))

(deftest should-produce-weighted-edges-for-multiple-calls
  (is (= 2 (get edges ['example/multiple-calls 'clojure.core/println]))))

(deftest should-report-java-interop
  (is (= 1 (get edges ['example/java-interop 'java.util.Collections/EMPTY_SET]))))
