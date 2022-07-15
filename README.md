
## Running the Demos

You will need Spack installed as well as the [snailpacks]() repo. The
quick bootstrap script should be enough to get going if you don't have
this installed already:

```sh
curl --proto '=https' --tlsv1.2 -sSf https://raw.githubusercontent.com/salotz/snailpacks/master/bootstrap.sh | sh
```

Then for each demo you can build the environment, activate it, and run
them.

```sh
  cd XXX_demo-name
  make env
  spacktivate .
  make run
```
