(ns topology.clique-test
  (:require [clojure.test :refer :all]
            [topology.clique :refer :all]))

(deftest should-return-all-functions-in-namespace
  (let [fns (functions 'topology.example)]
    (is (> (count fns) 0))))
