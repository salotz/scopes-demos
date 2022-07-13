"""" Utility functions.

let cp = (import chipmunk2d.chipmunk2d)
let rl = (import raylib.raylib)


let C.unistd = (include "unistd.h")
let C.time = (include "time.h")

""""Program pauses execution for N seconds
let sleep! = (C.unistd . extern . sleep)

inline get-time ()
    C.time.extern.time null


## Vector conversions
let
    cpVec = cp.Vect
    rlVec = rl.Vector2

fn cp-to-rl-vec (cp-vec)
    rlVec
        ((cp-vec . x) as f32)
        ((cp-vec . y) as f32)

fn rl-to-cp-vec (rl-vec)
    cpVec
        ((rl-vec . x) as f64)
        ((rl-vec . y) as f64)


inline container-repr (obj)
    """"Function to automate the building of __repr metamethods for container structs.


inline run-repl (scope)
    """"Will start up the repl with all of the current locals injected
        into the environment.

    let final_scope = (.. (globals) scope)

    if main-module?
        run-stage;

    if main-module?
        using import console
        read-eval-print-loop final_scope false

do
    let
        sleep!
        get-time
        cp-to-rl-vec
        rl-to-cp-vec
        cpVec
        rlVec
        # container-repr
        run-repl

    (locals)
