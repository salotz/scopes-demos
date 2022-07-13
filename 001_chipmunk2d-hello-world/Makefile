run:
	scopes -e -m demo.main

test:
	scopes -e -m demo.test

repl:
	scopes -e

.spack-env: spack.yaml
	spack env create -d .
	spack -e . install --reuse

clean:
	rm -rf .spack-env
	rm spack.lock
.PHONY: clean
