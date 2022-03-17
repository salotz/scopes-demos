
let include-path =
    .. module-dir "/" ".spack-env/view/include"

let lib-path =
    .. module-dir "/" ".spack-env/view/lib"

# the path to the scopes modules
let pkg-path =
    .. module-dir "/" ".spack-env/view/lib/scopes/packages"

# add the package load paths for the local packages
'define-symbol package 'path
    cons
        .. pkg-path "/?.sc"
        .. pkg-path "/?/init.sc"
        package.path

do
    let include-path lib-path pkg-path
    locals;
