
run:
	scopes -e -m demo.main
.PHONY: run

test:
	scopes -e -m demo.test
.PHONY: test

repl:
	scopes -e
.PHONY: repl

env: .spack-env
.PHONY: env

.spack-env: spack.yaml
	spack env create -d .
	spack -e . install --reuse
	echo "Activate with this command: spacktivate ."

clean:
	rm -rf .spack-env
	rm spack.lock
.PHONY: clean
