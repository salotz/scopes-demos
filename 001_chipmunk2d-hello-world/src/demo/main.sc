""""hello world for chipmunk but with visualizations done with raylib
    and code refactored

let cp = (import chipmunk2d.chipmunk2d)
let rl = (import raylib.raylib)
using rl.macros

using import .game-objects
using import .util
let CONFIG = (import .config)

# objects

let world = (init-world CONFIG)

camera
local camera =
    rl.Camera2D
        (rlVec (CONFIG.SCREEN_WIDTH / 2.0) (CONFIG.SCREEN_HEIGHT / 2.0))
        (rlVec (CONFIG.SCREEN_WIDTH / 2.0) (CONFIG.SCREEN_HEIGHT / 2.0))
        0.0:f32
        1.0:f32

do-window:
    CONFIG.SCREEN_WIDTH
    CONFIG.SCREEN_HEIGHT
    "Chipmunk2D: Hello World"
    CONFIG.FPS

    (cp.SpaceStep world.space CONFIG.TIME_STEP)

    do-draw:


        rl.ClearBackground CONFIG.BACKGROUND_COLOR

        # Do 2D drawing
        rl.BeginMode2D camera

        'draw world.scene

        rl.EndMode2D;

;
