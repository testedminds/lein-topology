(ns topology.finder
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.pprint :as cp]
            [clojure.tools.namespace.file :as ns-file]
            [clojure.tools.namespace.find :as ns-find])
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
  (->>
    (io/file dir)
    file-seq
    (filter #(or (clojurescript-file? %)
                 (ns-file/clojure-file? %)))
    (sort-by #(.getAbsolutePath ^File %))))

(defn find-sources
  "Finds a list of source files located in the given directories."
  [dirs]
  (->>
    dirs
    (filter identity)
    (map find-sources-in-dir)
    flatten))

(defn file-namespaces
  "Calculates the namespaces defined by the given files."
  [files]
  (map (comp second ns-file/read-file-ns-decl) files))
