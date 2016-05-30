# lein-topology

`lein topology` generates a Clojure project's function dependency graph. Data is output as CSV in the format `source,target,weight` and should be interpreted as ns/source-function calls ns/target-function <weight> times. Try running lein-topology on itself:

<TODO: Topology of topology>

Use `lein topology` to "collect the dots"; visualization, filtering, and analysis can be done with the tools of your choice, regardless of whether or not those tools are written in Clojure.
This is partly what distinguishes `lein topology` from the numerous other projects and lein plugins that create namespace or partially complete function call graphs.


## Usage

Put `[lein-topology "0.1.0-SNAPSHOT"]` into the `:plugins` vector of your `:user` profile.

cd into a Clojure project that uses `leiningen`.

Preview the network: `lein topology`

If everything looks good, redirect to file: `lein topology > /tmp/topology.csv`


## TODO

• Provide a way to get a list of nodes from the Edgelist:

`{ cut -d "," -f 1 /tmp/topology.csv & cut -d "," -f 2 /tmp/topology.csv; } | sort | uniq > /tmp/nodes.csv`

• Provide `ns --contains-> function` output.


## Topology Network Visualization Workflow

* Run the data through Edgewise to convert to GraphML
  * edgewise edgelist->graphml /tmp/topology.csv > /tmp/topology.graphml
  * Graphml is probably the lowest common denominator. TGF sucks though because the edges can't have weights.
* Open the graphml in yEd and view in a hierarchical layout.
  * Might need to introduce surrogate source or sink nodes.
  * Might need to filter out namespaces that are too common.
  * An online option with vis.js as with Storm of Swords allows the analysis to be more easily shared with a team. Having an up-to-date diagram with each commit might be interesting. Might even make sense to do this before a commit.


## What is a Function Dependency Network?

<TODO: Explain the distinction between control flow graph and function dependency>

• Link to the Code as Network analysis to indicate how the results of this network can be used as a refactoring and orientation tool.



## License

