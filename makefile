SHELL := /usr/bin/env bash

commit = `git rev-parse --short HEAD`

dogfood:
	rm -rf ~/.m2/repository/lein-topology
	lein do test, install
	lein topology > ./docs/data/lein-topology-$(commit).csv
	wc -l ./docs/data/*.csv

.SILENT: commit
commit:
	echo $(commit)
