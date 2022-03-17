using import struct
using import Array

let cp = (import .src.chipmunk2d)
let rl = (import ..src.raylib)
using rl.macros

## Screen
let
    SCREEN_WIDTH = 800
    SCREEN_HEIGHT = 800
    FPS = 60

let
    BACKGROUND_COLOR = rl.Colors.LIGHTGRAY
    BALL_SPEED = 350.0
    BALL_RADIUS = 10
    BALL_COLOR = rl.Colors.WHITE

# useful physics constants
let
    zero_vec = ()

local scene =
    Scene
        arena
        (player1 = paddle1)
        (player2 = paddle2)
        ball
        arena_sprite
        paddle1_sprite
        paddle2_sprite
        ball_sprite

# camera
local camera =
    rl.Camera2D
        (rl.Vector2 (SCREEN_WIDTH / 2.0) (SCREEN_HEIGHT / 2.0))
        (rl.Vector2 (SCREEN_WIDTH / 2.0) (SCREEN_HEIGHT / 2.0))
        0.0:f32
        1.0:f32

do-window:
    SCREEN_WIDTH
    SCREEN_HEIGHT
    "Pong"
    FPS

    local delta-time = ((rl.GetFrameTime) as f32)

    # (update-positions)
    # (update-scene)

    do-draw:

        rl.ClearBackground BACKGROUND_COLOR

        (update-scene delta-time scene)

        # Do 2D drawing
        rl.BeginMode2D camera

        (draw-scene scene)

        rl.EndMode2D;
