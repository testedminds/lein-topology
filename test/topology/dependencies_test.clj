(ns topology.dependencies-test
  (:require [clojure.test :refer :all]
            [topology.dependencies :refer :all]))

(deftest should-compute-fn-calls-in-namespace
  (let [deps (ns->fn-dep-map 'example)]
    (is (= ('example/a-test-def deps)
           ['clojure.core/map 'clojure.core/* 'clojure.core/range]))
    (is (= ('example/a-defn-with-a-macro deps)
           ['clojure.core/defn 'example/test-when 'example/a-test-def 'clojure.string/join]))))
