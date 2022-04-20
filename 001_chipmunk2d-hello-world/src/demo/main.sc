# hello world for chipmunk but with visualizations done with raylib

using import Array
using import String

let cp = (import chipmunk2d.chipmunk2d)
let rl = (import raylib.raylib)
using rl.macros

using import .structs
using import .util

### Scene

## Screen
let
    SCREEN_WIDTH = 800
    SCREEN_HEIGHT = 800
    FPS = 60

let SCREEN_CENTER =
    rl.Vector2
        (SCREEN_WIDTH / 2)
        (SCREEN_HEIGHT / 2)

## constants
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
    GROUND_VECS =
        (Array cpVec)
            cpVec
                SCREEN_CENTER.x - 1000
                SCREEN_CENTER.y - 200
            cpVec
                SCREEN_CENTER.x + 1000
                SCREEN_CENTER.y + 600

## scene objects

# objects

## physics space
local space = (cp.SpaceNew)
cp.SpaceSetGravity space GRAVITY

## ground
local ground_shape =
    cp.SegmentShapeNew
        (cp.SpaceGetStaticBody space)
        GROUND_VECS @ 0
        GROUND_VECS @ 1
        0

cp.ShapeSetFriction ground_shape GROUND_FRICTION
cp.ShapeSetElasticity
    ground_shape
    0.9

cp.SpaceAddShape space ground_shape

local ground =
    Ground
        rl.Colors.GREEN
        GROUND_THICKNESS
        ground_shape

fn draw-ground (ground)

    let a = (cp.SegmentShapeGetA (ground . shape))
    let b = (cp.SegmentShapeGetB (ground . shape))

    # then draw the sprite
    rl.DrawLineEx
        (cp-to-rl-vec a)
        (cp-to-rl-vec b)
        ground.thickness
        ground.color
    ;

## ball
local ball_radius = (cp.Float BALL_RADIUS)
local ball_mass = (cp.Float BALL_MASS)

local ball_moment =
    cp.MomentForCircle
        ball_mass
        0
        ball_radius
        V_ZERO

local ballBody =
    cp.SpaceAddBody
        space
        (cp.BodyNew ball_mass ball_moment)

cp.BodySetPosition
    ballBody
    (rl-to-cp-vec BALL_POSITION)


local ballShape =
    cp.SpaceAddShape
        space
        cp.CircleShapeNew
            ballBody
            ball_radius
            V_ZERO

cp.ShapeSetElasticity
    ballShape
    BALL_ELASTICITY

cp.ShapeSetFriction ballShape BALL_FRICTION

local ball =
    Ball
        BALL_COLOR
        ballShape

let pos =
    cp.BodyGetPosition
        cp.ShapeGetBody (ball . shape)

fn draw-ball (ball)

    let cp-pos =
        cp.BodyGetPosition
            cp.ShapeGetBody (ball . shape)

    rl.DrawCircleV
        cp-to-rl-vec cp-pos
        ((cp.CircleShapeGetRadius (ball . shape)) as f32)
        ball.color
    ;


local scene =
    Scene
        ground = ground
        ball = ball

fn draw-scene (scene)
    (draw-ball scene.ball)
    (draw-ground scene.ground)
    ;

# camera
local camera =
    rl.Camera2D
        (rlVec (SCREEN_WIDTH / 2.0) (SCREEN_HEIGHT / 2.0))
        (rlVec (SCREEN_WIDTH / 2.0) (SCREEN_HEIGHT / 2.0))
        0.0:f32
        1.0:f32

do-window:
    SCREEN_WIDTH
    SCREEN_HEIGHT
    "Chipmunk2D: Hello World"
    FPS

    (cp.SpaceStep space TIME_STEP)

    do-draw:


        rl.ClearBackground BACKGROUND_COLOR

        # Do 2D drawing
        rl.BeginMode2D camera

        (draw-scene scene)

        rl.EndMode2D;

;
