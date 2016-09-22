SHELL := /usr/bin/env bash

commit = `git rev-parse --short HEAD`

dogfood:
	rm -rf ~/.m2/repository/lein-topology
	lein do test, install
	lein topology > doc/lein-topology-$(commit).csv
	wc -l doc/*.csv

.SILENT: commit
commit:
	echo $(commit)
