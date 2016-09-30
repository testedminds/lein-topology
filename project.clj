(defproject lein-topology "0.2.0"
  :description "A Leiningen plugin that generates a project's function dependency structure matrix."
  :url "https://github.com/testedminds/lein-topology"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :eval-in-leiningen true
  :dependencies [[org.clojure/tools.namespace "0.2.11"]]
  :profiles {:dev {:resource-paths ["test/resources"]}})
