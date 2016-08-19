(ns topology.example)

(def a-test-def (map #(* 2 %) (range 10)))

(defn a-defn-with-a-macro [foo]
  (when foo (println "bar! baz!")))

;; TODO...change t to test, and this will resolve to clojure.core/test
;; 'if and 'do also don't get picked up because of the quoted forms.
(defmacro test-when
  [t & body]
  (list 'if t (cons 'do body)))
