
let pkg-path =
    .. module-dir "/" ".spack-env/view/lib/scopes/packages"

'define-symbol package 'path
    cons
        .. pkg-path "/?.sc"
        .. pkg-path "/?/init.sc"
        package.path

run-stage;
import raylib.raylib
