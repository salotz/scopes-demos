""""hello world for chipmunk but with visualizations done with raylib
    and code refactored

let cp = (import chipmunk2d.chipmunk2d)
let rl = (import raylib.raylib)
using rl.macros

using import .game-objects
using import .util
let CONFIG = (import .config)

# objects

local world = (World CONFIG)

local new-ground = (copy world.scene.ground)

local camera =
    rl.Camera2D
        (rlVec (CONFIG.SCREEN_WIDTH / 2.0) (CONFIG.SCREEN_HEIGHT / 2.0))
        (rlVec (CONFIG.SCREEN_WIDTH / 2.0) (CONFIG.SCREEN_HEIGHT / 2.0))
        0.0:f32
        1.0:f32

local physics-time = 0.0
let current-time = (get-time)

local accumulator = 0.0


local prev-scene = (copy world.scene)

do-window:
    CONFIG.SCREEN_WIDTH
    CONFIG.SCREEN_HEIGHT
    CONFIG.WINDOW_TITLE
    CONFIG.FPS

    # resolve the real time delta so we know what fraction of a frame
    # has elapsed
    let new-time = (get-time)
    let frame-time = (new-time - current-time)
    let current-time = new-time

    accumulator += (frame-time as f32)

    while (accumulator >= CONFIG.PHYSICS_TIME_STEP)

        prev-scene = (copy world.scene)

        'physics-step world.phys-world CONFIG.PHYSICS_TIME_STEP

        accumulator -= CONFIG.PHYSICS_TIME_STEP

        physics-time += CONFIG.PHYSICS_TIME_STEP


    # update the current scene based on the blending factor
    let blend = (accumulator / CONFIG.PHYSICS_TIME_STEP)

    'update world

    do-draw:

        rl.ClearBackground CONFIG.BACKGROUND_COLOR

        # Do 2D drawing
        rl.BeginMode2D camera

        'draw world.scene prev-scene blend

        rl.EndMode2D;

;
