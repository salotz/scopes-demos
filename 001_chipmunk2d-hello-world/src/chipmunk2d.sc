# this loads the site specific packaging information
let pkg = (import ..pkg)

let header =
    include
        "chipmunk/chipmunk.h"
        options
            # "-v"
            .. "-I" pkg.include-path

let lib-path = (.. pkg.lib-path "/libchipmunk.so")

load-library lib-path

## dump the namespaces for the differnt header things
let chipmunk =
    ..
        header.extern
        header.typedef
        header.define
        header.const
        header.struct

do
    let
        header
        # macros

    .. chipmunk (locals)
