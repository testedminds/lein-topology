# lein-topology

`lein topology` generates a Clojure project's function dependency graph.

<TODO: Topology of topology>

<TODO: Explain the distinction between control flow and function dependency>

        • Link to the Code as Network analysis to indicate how the results of this network can be used.
        • Add docs to indicate how to use the plugin.

This is partly what distinguishes `lein topology` from the numerous other projects and lein plugins that create a namespace or partially complete function call graphs: Use `lein topology` to "collect the dots"; visualization, filtering, and analysis can be done with the tools of your choice, regardless of whether or not those tools are written in Clojure.

ns --contains-> function

ns/function --calls-> ns/function


## Usage

Put `[lein-topology "0.1.0-SNAPSHOT"]` into the `:plugins` vector of your `:user` profile.

    $ lein topology



## License

