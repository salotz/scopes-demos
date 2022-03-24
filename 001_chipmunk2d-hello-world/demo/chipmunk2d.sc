let header = (include "chipmunk/chipmunk.h")
load-library "libchipmunk.so"

inline filter-scope (scope pattern)
    """"For a scope match the pattern to a symbol prefix and remove it from the outputted
        scope symbol, useful for stripping namespace prefixes from C libraries that
        are unnecessary in scopes since we have explicit Scopes
    fold (scope = (Scope)) for k v in scope
        let name = (k as Symbol as string)
        let match? start end = ('match? pattern name)
        if match?
            'bind scope (Symbol (rslice name end)) v
        else
            scope

# ## dump the namespaces for the differnt header things
let chipmunk =
    ..
        (filter-scope header.extern "^cp")
        (filter-scope header.typedef "^cp")
        (filter-scope header.define "^cp")
        (filter-scope header.const "^CP")
        (filter-scope header.struct "^cp")

do
    let
        header
        # macros

    .. chipmunk (locals)
