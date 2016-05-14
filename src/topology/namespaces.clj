(ns topology.namespaces)

(def namespaces
  ["foreclojure.core"])

(def set-of-ignored-ns
  #{"clojure.core"
    "clojure.tools.logging"
    "clojure.tools.logging.impl"
    "clojure.core.async.impl"
    "clojure.core.async.impl.ioc-macros"
    "clojure.core.async"})
