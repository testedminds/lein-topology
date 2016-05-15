(ns topology.core
  (:require [clojure.tools.analyzer.jvm :as jvm]
            [clojure.tools.analyzer.ast :as ast]
            [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.pprint :as cp]
            [clojure.tools.namespace.file :as ns-file]
            [clojure.tools.namespace.find :as ns-find])
  (:import java.io.File))

(defn- with-vars
  [node]
  (let [children-vars (mapcat :vars (ast/children node))]
    (assoc node :vars (if (= :var (:op node))
                        (conj children-vars (clojure.string/replace (str (:var node)) #"#'" ""))
                        children-vars))))

(defn- ns->defs
  [ns-str]
  (filter #(= :def (:op %)) (jvm/analyze-ns ns-str)))

(defn ns->adjacency-list
  [ns-str]
  (reduce #(assoc %1
            (str ns-str "/" (get-in %2 [:name]))
            (:vars (ast/postwalk %2 with-vars)))
          {}
          (ns->defs ns-str)))

(defn- edges
  [ns-str node]
  (let [outv (str ns-str "/" (get-in node [:name]))
        invs (:vars (ast/postwalk node with-vars))]
    (mapv #(vector outv %) invs)))

(defn ns->edges
  [ns-str]
  (mapcat #(edges ns-str %) (ns->defs ns-str)))

(defn edges->csv
  [file edges]
  (with-open [w (clojure.java.io/writer file)]
    (doseq [[outv inv] edges]
      (.write w (str outv "," inv))
      (.newLine w))))

(defn- ignored? [[outv inv] filter-set]
  (let [inv-nspc (clojure.string/split inv #"/")]
    (some filter-set inv-nspc)))

(defn ns->edgelist
  ([ns-str] (ns->edgelist ns-str #{}))
  ([ns-str filter-set]
   (let [e (ns->edges ns-str)]
     (remove #(ignored? % filter-set) e))))

(defn ns->edgelist-csv
  ([ns-str file] (ns->edgelist-csv file #{}))
  ([ns-str file filter-set]
   (->> (ns->edgelist ns-str filter-set)
        (edges->csv file))))

;;;;;;;;;

;; TODO: Move to a finder namespace and acknowledge https://github.com/greglook/lein-hiera/blob/master/src/leiningen/hiera.clj
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

(defn- find-sources
  "Finds a list of source files located in the given directories."
  [dirs]
  (->>
    dirs
    (filter identity)
    (map find-sources-in-dir)
    flatten))

(defn- file-namespaces
  "Calculates the namespaces defined by the given files."
  [files]
  (map (comp second ns-file/read-file-ns-decl) files))

;;;;;;;;;;

(defn all-ns->edgelist-csv [dest source-paths]
  ;;(println (seq (.getURLs (java.lang.ClassLoader/getSystemClassLoader))))
  (println "Now we're in the fucking project...")
  (let [sources (find-sources source-paths)
        namespaces (set (file-namespaces sources))]
    (cp/pprint namespaces)
    (doseq [nspc namespaces]
      (println nspc)
      ;;(ns->edgelist-csv nspc (str dest "/" nspc ".csv") ignored)
      )))
