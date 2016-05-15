(ns leiningen.topology
  (:require [leiningen.core.eval :as lein]
            [clojure.java.io :as io]))

;; https://github.com/clojure-cookbook/clojure-cookbook/blob/master/04_local-io/4-06_delete-file.asciidoc
(defn delete-directory [directory-path]
  (let [directory-contents (file-seq (io/file directory-path))
        files-to-delete (filter #(.isFile %) directory-contents)]
    (doseq [file files-to-delete]
      (io/delete-file (.getPath file)))
    (io/delete-file directory-path true)))

(defn topology [project & args]
  (let [dest (str (:target-path project) "/topology")]
    (delete-directory dest)
    (.mkdir (java.io.File. dest))
    (lein/eval-in-project (update-in project [:dependencies] conj
                                     '[lein-topology "0.1.0-SNAPSHOT"]
                                     '[org.clojure/clojure "1.8.0"])
                          `(topology.core/all-ns->edgelist-csv ~dest ~@(:source-paths project))
                          `(require 'topology.core))))
