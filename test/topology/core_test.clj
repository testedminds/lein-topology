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

;; syntax-quotes are read as (seq (concat (list ...)))
;; https://github.com/clojure/clojure/blob/d274b2b96588b100c70be065f949e1fdc9e7e14d/src/jvm/clojure/lang/LispReader.java#L1017
(deftest syntax-quotes-add-seq-concat-list
  (let [syn-quotes (filter #(= (ffirst %) 'example/using-syntax-quote) edges)]
    (is (= syn-quotes
           [[['example/using-syntax-quote 'clojure.core/shuffle] 1]
            [['example/using-syntax-quote 'clojure.core/seq] 1]
            [['example/using-syntax-quote 'clojure.core/max] 1]
            [['example/using-syntax-quote 'clojure.core/list] 1]
            [['example/using-syntax-quote 'clojure.core/range] 1]
            [['example/using-syntax-quote 'clojure.core/concat] 1]
            [['example/using-syntax-quote 'clojure.core/defn] 1]]))))
