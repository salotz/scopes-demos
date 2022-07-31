let header = (include "Python.h")
shared-library "libpython3.9.so"


inline filter-scope (scope pattern)
    # Scope objects are immutable, so we have to build the new one iteratively.
    fold (scope = scope) for k v in scope
        # the scope generator returns symbols as boxed Values, so we need to unbox to Symbol first.
        let name = (k as Symbol as string)
        # the 'match? method in string uses the C++ regex engine.
        let match? start end = ('match? pattern name)
        if match?
            # here we make the assumption that we are always removing a prefix, so rslice suffices.
            let new-name = (rslice name end)
            'bind scope (Symbol new-name) v
        else
            # return the unmodified Scope.
            scope

# inline filter-scope (scope pattern)
#     """"For a scope match the pattern to a symbol prefix and remove it from the outputted
#         scope symbol, useful for stripping namespace prefixes from C libraries that
#         are unnecessary in scopes since we have explicit Scopes
#     fold (scope = (Scope)) for k v in scope
#         let name = (k as Symbol as string)
#         let match? start end = ('match? pattern name)
#         if match?
#             'bind scope (Symbol (rslice name end)) v
#         else
#             scope

# dump the namespaces for the differnt header things into the same
# one and then santize the symbols/names

vvv bind python
do
    using header.extern
    using header.const
    using header.define
    using header.typedef
    locals;

let python =
    ..
        (filter-scope python "^Py")
        (filter-scope python "^PY")
        (filter-scope python "^Py_")
        (filter-scope python "^PY_")

run-stage;

let py = python

py.Initialize;
py.Run_SimpleString
    """"from time import time, ctime
        print("Hello from Python!")
        print(f'Today is {ctime(time())}')

