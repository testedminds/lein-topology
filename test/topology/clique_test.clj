(ns topology.clique-test
  (:require [clojure.test :refer :all]
            [topology.clique :refer :all]))

(deftest should-return-all-functions-in-namespace
  (let [fns (functions 'topology.example)]
    (is (> (count fns) 0))))

(deftest should-compute-fn-calls-in-namespace
  (let [deps (filtered (all-fq (dependencies 'topology.example)))]
    (is (= ('topology.example/a-test-def deps) ['clojure.core/map 'clojure.core/* 'clojure.core/range]))
    (is (= ('topology.example/a-defn-with-a-macro deps) ['clojure.core/defn 'clojure.core/when 'clojure.core/println]))))
