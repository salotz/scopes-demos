run:
	scopes -e -m demo.main

test:
	scopes -e -m demo.test

repl:
	scopes -e

env: .spack-env
.PHONY: env

.spack-env: spack.yaml
	spack env create -d .
	spack -e . install --reuse
	echo "activate with: spacktivate ."

clean:
	rm -rf .spack-env
	rm spack.lock
.PHONY: clean
