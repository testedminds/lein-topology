(ns example)

(def a-test-def (map #(* 2 %) (range 10)))

;; TODO...change t to test, and this will resolve to clojure.core/test
;; 'if and 'do also don't get picked up because of the quoted forms.
(defmacro test-when
  [t & body]
  (list 'if t (cons 'do body)))

(defn a-defn-with-a-macro [foo]
  (test-when a-test-def (clojure.string/join foo "baz!")))

(defn java-interop [x]
  (java.util.Collections/EMPTY_SET))

(defn multiple-calls [x]
  (println x)
  (println x))
