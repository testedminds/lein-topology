(ns example)

(def a-test-def (map #(* 2 %) (range 10)))

(defmacro test-when
  [t & body]
  (list 'if t (cons 'do body)))

(defn a-defn-with-a-macro [foo]
  (test-when a-test-def (clojure.string/join foo "baz!")))

(defn java-interop [x]
  (java.util.Collections/EMPTY_SET))

(defn multiple-calls [x]
  (meta (meta x)))

(defn using-syntax-quote [x]
  `(max ~@(shuffle (range x))))
