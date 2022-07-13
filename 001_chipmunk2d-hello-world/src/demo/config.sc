using import Array

let cp = (import chipmunk2d.chipmunk2d)
let rl = (import raylib.raylib)
using rl.macros
using import .util


do
    ## Screen

    let
        WINDOW_TITLE = "Chipmunk2D: Hello World"
        SCREEN_WIDTH = 800
        SCREEN_HEIGHT = 800
        FPS = 30

    let SCREEN_CENTER =
        rl.Vector2
            (SCREEN_WIDTH / 2)
            (SCREEN_HEIGHT / 2)

    ## Game World
    let
        BACKGROUND_COLOR = rl.Colors.DARKGRAY
        # physics
        V_ZERO = (cpVec 0.0 0.0)
        GRAVITY = (cpVec 0.0 8.0)
        PHYSICS_TIME_STEP = (1.0 / 70.0)
        # ball
        BALL_COLOR = rl.Colors.WHITE
        BALL_RADIUS = 20
        BALL_MASS = 1.0
        BALL_POSITION = (copy SCREEN_CENTER)
        BALL_FRICTION = 0.7
        BALL_ELASTICITY = 1.0
        # ground
        GROUND_COLOR = rl.Colors.GREEN
        GROUND_THICKNESS = 10:f32
        GROUND_FRICTION = 0.7
        GROUND_ELASTICITY = 0.7
        GROUND_VECS =
            (Array cpVec 2)
                cpVec
                    SCREEN_CENTER.x - 1000
                    SCREEN_CENTER.y - 200
                cpVec
                    SCREEN_CENTER.x + 1000
                    SCREEN_CENTER.y + 600


    locals;
