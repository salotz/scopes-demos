#!/usr/bin/env scopes

using import struct
using import Array

let rl = (import raylib.raylib)
using rl.macros

## Screen
let
    SCREEN_WIDTH = 800
    SCREEN_HEIGHT = 800
    FPS = 60


## Game constants

let
    BACKGROUND_COLOR = rl.Colors.LIGHTGRAY
    BALL_SPEED = 1000.0
    BALL_RADIUS = 10
    BALL_COLOR = rl.Colors.WHITE
    PADDLE_SPEED = 200.0
    PADDLE_WIDTH = 10
    PADDLE_LENGTH = 100
    PADDLE_ARENA_BUFFER = 20
    PLAYER_1_COLOR = rl.Colors.PURPLE
    PLAYER_2_COLOR = rl.Colors.BLUE
    ARENA_WIDTH = (SCREEN_WIDTH - 50)
    ARENA_HEIGHT = (SCREEN_HEIGHT - 50)
    ARENA_BORDER_THICKNESS = 10
    ARENA_BORDER_COLOR = rl.Colors.WHITE
    ARENA_BACKGROUND_COLOR = rl.Colors.BLACK

let SCREEN_CENTER =
    rl.Vector2
        (SCREEN_WIDTH / 2)
        (SCREEN_HEIGHT / 2)

# the arena should start centered in the screen
let ARENA_STARTING_POSITION =
    rl.Vector2
        ((SCREEN_WIDTH / 2) - (ARENA_WIDTH / 2))
        ((SCREEN_HEIGHT / 2) - (ARENA_HEIGHT / 2))


## Structs

struct Paddle
    position : rl.Vector2
    width : f32
    length : f32
    speed : f32

struct Paddle_Sprite
    rect : rl.Rectangle
    color : rl.Color

struct Ball
    position : rl.Vector2
    velocity : rl.Vector2
    radius : f32

struct Ball_Sprite
    position : rl.Vector2
    radius : f32
    color : rl.Color

struct Arena
    position : rl.Vector2
    width : f32
    height : f32
    wall-thickness : i32

struct Arena_Sprite
    rect : rl.Rectangle
    border-thickness : f32
    border-color : rl.Color
    background-color : rl.Color

struct Scene
    arena : Arena
    player1 : Paddle
    player2 : Paddle
    ball : Ball
    arena-sprite : Arena_Sprite
    player1-sprite : Paddle_Sprite
    player2-sprite : Paddle_Sprite
    ball-sprite : Ball_Sprite

## Math

# fn check-collision-circle-rect (center radius rect)

#     local collision = false

#     let
#         # center of the rectangle
#         rect-center-x = ((rect . x) + (rect.width / 2.0) as i32)
#         rect-center-y = ((rect . y) + (rect.height / 2.0) as i32)
#         # difference between them in each dimension
#         dx = (abs center.x - (rect-center-x as f32))
#         dy = (abs center.y - (rect-center-y as f32))

#     # quick check for no collision
#     if (dx > ((rect.width / 2.0) + radius))
#         return false

#     if (dy > ((rect.height / 2.0) + radius))
#         return false

#     # quick check for collision
#     if (dx <= (rect.width / 2.0))
#         return true

#     if (dy <= (rect.height / 2.0))
#         return true

#     # if nothing else passed check the corners

#     return collision

## Functions

fn update-ball (delta scene)

    # calculate the new velocity of the ball if necessary

    # first detect collisions with paddles

    # player1 paddle
    if
        rl.CheckCollisionCircleRec
            (scene . ball . position)
            (scene . ball . radius)
            (scene . player1-sprite . rect)

        (scene . ball . velocity . x) *= -1

    elseif
        rl.CheckCollisionCircleRec
            (scene . ball . position)
            (scene . ball . radius)
            (scene . player2-sprite . rect)

        (scene . ball . velocity . x) *= -1


    # apply the velocity to the position
    (scene . ball . position . x) += (scene . ball . velocity . x)
    (scene . ball . position . y) += (scene . ball . velocity . y)

fn update-player-paddle (delta scene)

    if (rl.IsKeyDown rl.KEY_UP)
        (scene . player1 . position . y) -= (scene . player1 . speed * delta)

    elseif (rl.IsKeyDown rl.KEY_DOWN)
        (scene . player1 . position . y) += (scene . player1 . speed * delta)

fn update-enemy-paddle (delta scene)

    # up
    if (rl.IsKeyDown rl.KEY_W)
        (scene . player2 . position . y) -= (scene . player2 . speed * delta)

    # down
    elseif (rl.IsKeyDown rl.KEY_S)
        (scene . player2 . position . y) += (scene . player2 . speed * delta)

fn update-scene (delta scene)

    (update-ball delta scene)
    (update-player-paddle delta scene)
    (update-enemy-paddle delta scene)


fn draw-paddle (paddle paddle_sprite)

    # update the sprite
    (paddle_sprite . rect . x) = (paddle . position . x)
    (paddle_sprite . rect . y) = (paddle . position . y)

    # then draw the sprite
    rl.DrawRectangleRec
        paddle_sprite.rect
        paddle_sprite.color

fn draw-ball (scene)

    let
        ball_sprite = (scene . ball-sprite)
        ball = (scene . ball)

    # update the sprite
    (ball_sprite . position . x) = (ball . position . x)
    (ball_sprite . position . y) = (ball . position . y)

    # draw the sprite
    rl.DrawCircleV
        ball_sprite.position
        ball_sprite.radius
        ball_sprite.color

fn draw-arena (scene)

    # update the sprite
    (scene . arena-sprite . rect . x) = (scene . arena . position . x)
    (scene . arena-sprite . rect . y) = (scene . arena . position . y)

    # draw the arena
    rl.DrawRectangleRec
        (scene . arena-sprite . rect)
        (scene . arena-sprite . background-color)

    rl.DrawRectangleLinesEx
        (scene . arena-sprite . rect)
        (scene . arena-sprite . border-thickness)
        (scene . arena-sprite . border-color)

fn draw-scene (scene)
    draw-arena scene
    draw-paddle (scene . player1) (scene . player1-sprite)
    draw-paddle (scene . player2) (scene . player2-sprite)
    draw-ball scene


## Scene

local arena =
    Arena
        position = ARENA_STARTING_POSITION
        (width = ARENA_WIDTH)
        (height = ARENA_HEIGHT)
        (wall-thickness = ARENA_BORDER_THICKNESS)

local paddle1 =
    Paddle
        position =
            rl.Vector2
                (arena . position . x) + (arena.wall-thickness as f32) + PADDLE_ARENA_BUFFER
                ((arena . position . y) + (arena.height / 2)) - (PADDLE_LENGTH / 2)
        PADDLE_WIDTH
        PADDLE_LENGTH
        PADDLE_SPEED

local paddle2 =
    Paddle
        position =
            rl.Vector2
                ((arena . position . x) + arena.width) - (arena.wall-thickness as f32) - PADDLE_ARENA_BUFFER - PADDLE_WIDTH
                ((arena . position . y) + (arena.height / 2)) - (PADDLE_LENGTH / 2)
        PADDLE_WIDTH
        PADDLE_LENGTH
        PADDLE_SPEED

local ball =
    Ball
        (position = SCREEN_CENTER)
        velocity =
            rl.Vector2 -3.0 0.0
        BALL_RADIUS

local paddle1_sprite =
    Paddle_Sprite
        rl.Rectangle
            (paddle1 . position . x) + (paddle1.width / 2)
            (paddle1 . position . y) + (paddle1.width / 2)
            paddle1.width
            paddle1.length
        PLAYER_1_COLOR

local paddle2_sprite =
    Paddle_Sprite
        rl.Rectangle
            (paddle2 . position . x)
            (paddle2 . position . y)
            paddle2.width
            paddle2.length
        PLAYER_2_COLOR

local ball_sprite =
    Ball_Sprite
        ball.position
        BALL_RADIUS
        BALL_COLOR

local arena_sprite =
    Arena_Sprite
        rect =
            rl.Rectangle
                (arena . position . x)
                (arena . position . y)
                arena.width
                arena.height
        border-thickness = ARENA_BORDER_THICKNESS
        border-color = ARENA_BORDER_COLOR
        background-color = ARENA_BACKGROUND_COLOR

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
