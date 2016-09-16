SHELL := /usr/bin/env bash

commit = `git rev-parse --short HEAD`

dogfood:
	lein topology > doc/lein-topology-$(commit).csv

.SILENT: commit
commit:
	echo $(commit)
