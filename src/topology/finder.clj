(ns topology.finder
  (:require [clojure.java.io :as io]
            [clojure.tools.namespace.file :as ns-file])
  (:import java.io.File))

;; h/t https://github.com/greglook/lein-hiera/blob/master/src/leiningen/hiera.clj

(defn- clojurescript-file?
  "Returns true if the file represents a normal ClojureScript source file."
  [^File file]
  (and (.isFile file)
       (.endsWith (.getName file) ".cljs")))

(defn- find-sources-in-dir
  "Searches recursively under dir for source files (.clj and .cljs).
  Returns a sequence of File objects, in breadth-first sort order."
  [dir]
  (->> dir
       io/file
       file-seq
       (filter #(or (clojurescript-file? %)
                    (ns-file/clojure-file? %)))
       (sort-by #(.getAbsolutePath ^File %))))

(defn- dirs->sources
  "Finds a list of source files located in the given directories."
  [dirs]
  (->> dirs
       (filter identity)
       (map find-sources-in-dir)
       flatten))

(defn source-paths->namespaces
  "Calculates the namespaces defined by the given files."
  [dirs]
  (->> dirs
       dirs->sources
       (map (comp second ns-file/read-file-ns-decl))
       set))
