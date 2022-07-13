
run:
	scopes -e -m demo.main
.PHONY: run

test:
	scopes -e -m demo.test
.PHONY: test

repl:
	scopes -e
.PHONY: repl

.spack-env: spack.yaml
	spack env create -d .
	spack -e . install --reuse

clean:
	rm -rf .spack-env
	rm spack.lock
.PHONY: clean
