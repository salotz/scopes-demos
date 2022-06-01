#!/usr/bin/env scopes

using import struct
using import Array

let cp = (import chipmunk2d.chipmunk2d)
let rl = (import raylib.raylib)
using rl.macros

using import .structs
using import .util

## Screen
let
    SCREEN_WIDTH = 800
    SCREEN_HEIGHT = 800
    FPS = 60

let SCREEN_CENTER =
    rl.Vector2
        (SCREEN_WIDTH / 2)
        (SCREEN_HEIGHT / 2)


## Game constants

let
    BACKGROUND_COLOR = rl.Colors.LIGHTGRAY

    # Ball: the only actual physics body
    # BALL_SPEED = 1000.0
    BALL_RADIUS = 10
    BALL_COLOR = rl.Colors.WHITE
    BALL_MASS = 1
    BALL_FRICTION = 0.7
    BALL_ELASTICITY = 1.0

    # player paddles
    PADDLE_SPEED = 200.0
    PADDLE_WIDTH = 10
    PADDLE_LENGTH = 100
    PADDLE_ARENA_BUFFER = 20
    PLAYER_1_COLOR = rl.Colors.PURPLE
    PLAYER_2_COLOR = rl.Colors.BLUE

    # arena
    ARENA_WIDTH = (SCREEN_WIDTH - 50)
    ARENA_HEIGHT = (SCREEN_HEIGHT - 50)
    ARENA_BORDER_THICKNESS = 10
    ARENA_BORDER_COLOR = rl.Colors.WHITE
    ARENA_BACKGROUND_COLOR = rl.Colors.BLACK
    # the arena should start centered in the screen
    ARENA_STARTING_POSITION =
        rl.Vector2
            ((SCREEN_WIDTH / 2) - (ARENA_WIDTH / 2))
            ((SCREEN_HEIGHT / 2) - (ARENA_HEIGHT / 2))

    # global physics constants
    V_ZERO = (cpVec 0.0 0.0)
    GRAVITY = (cpVec 0.0 100.0)
    TIME_STEP = (1.0 / 60.0)

## Structs


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
