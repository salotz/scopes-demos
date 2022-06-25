using import Array

let cp = (import chipmunk2d.chipmunk2d)
let rl = (import raylib.raylib)
using rl.macros
using import .util


do
    ## Screen

    let
        SCREEN_WIDTH = 800
        SCREEN_HEIGHT = 800
        FPS = 60

    let SCREEN_CENTER =
        rl.Vector2
            (SCREEN_WIDTH / 2)
            (SCREEN_HEIGHT / 2)

    ## Game World
    let

        BACKGROUND_COLOR = rl.Colors.DARKGRAY
        V_ZERO = (cpVec 0.0 0.0)
        GRAVITY = (cpVec 0.0 100.0)
        BALL_COLOR = rl.Colors.WHITE
        BALL_RADIUS = 20
        BALL_MASS = 1
        BALL_POSITION = SCREEN_CENTER
        BALL_FRICTION = 0.7
        BALL_ELASTICITY = 1.0
        TIME_STEP = (1.0 / 60.0)
        GROUND_COLOR = rl.Colors.GREEN
        GROUND_THICKNESS = 10:f32
        GROUND_FRICTION = 0.7
        GROUND_ELASTICITY = 0.9
        GROUND_VECS =
            (Array cpVec)
                cpVec
                    SCREEN_CENTER.x - 1000
                    SCREEN_CENTER.y - 200
                cpVec
                    SCREEN_CENTER.x + 1000
                    SCREEN_CENTER.y + 600


    locals;
