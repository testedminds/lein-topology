# lein-topology

A Leiningen plugin that generates a project's function call network.

ns --contains-> function

ns/function --calls-> ns/function


## Usage

FIXME: Use this for user-level plugins:

Put `[lein-topology "0.1.0-SNAPSHOT"]` into the `:plugins` vector of your `:user`
profile.

FIXME: Use this for project-level plugins:

Put `[lein-topology "0.1.0-SNAPSHOT"]` into the `:plugins` vector of your project.clj.

FIXME: and add an example usage that actually makes sense:

    $ lein topology

## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
