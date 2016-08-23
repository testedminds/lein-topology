(ns topology.clique-test
  (:require [clojure.test :refer :all]
            [topology.clique :refer :all]))

(deftest should-return-all-functions-in-namespace
  (let [fns (functions 'example)]
    (is (> (count fns) 0))))

(deftest should-compute-fn-calls-in-namespace
  (let [deps (filtered (all-fq (dependencies 'example)))]
    (is (= ('example/a-test-def deps)
           ['clojure.core/map 'clojure.core/* 'clojure.core/range]))
    (is (= ('example/a-defn-with-a-macro deps)
           ['clojure.core/defn 'example/test-when 'example/a-test-def 'clojure.string/join]))))

