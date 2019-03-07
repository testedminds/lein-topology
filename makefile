SHELL := /usr/bin/env bash

commit = `git rev-parse --short HEAD`
name = lein-topology

dogfood:
	rm -rf ~/.m2/repository/$(name)
	lein do test, install
	lein topology > ./docs/data/$(name)-$(commit).csv
	wc -l ./docs/data/*.csv

cloc:
	cloc --csv --by-file . | tail -n +5 > ./docs/data/$(name)-$(commit)-cloc-by-file.csv
	cloc --csv . | tail -n +5 > ./docs/data/$(name)-$(commit)-cloc-by-lang.csv
