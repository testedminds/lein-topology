(ns topology.example)

(def a-test-def (map #(* 2 %) (range 10)))

(defn a-defn-with-a-macro [foo]
  (when foo (println "bar! baz!")))
