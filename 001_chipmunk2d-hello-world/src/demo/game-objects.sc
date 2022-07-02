using import struct
using import Array

let cp = (import chipmunk2d.chipmunk2d)
let rl = (import raylib.raylib)

using import .util

# types
struct Ground
    color : rl.Color
    thickness : f32
    shape : (pointer cp.Shape)

    fn draw (self)
        let a = (cp.SegmentShapeGetA self.shape)
        let b = (cp.SegmentShapeGetB self.shape)

        # then draw the sprite
        rl.DrawLineEx
            (cp-to-rl-vec a)
            (cp-to-rl-vec b)
            self.thickness
            self.color
        ;

inline init-ground (config space)

    local ground_shape =
        cp.SegmentShapeNew
            (cp.SpaceGetStaticBody space)
            config.GROUND_VECS @ 0
            config.GROUND_VECS @ 1
            0

    cp.ShapeSetFriction ground_shape config.GROUND_FRICTION
    cp.ShapeSetElasticity
        ground_shape
        config.GROUND_ELASTICITY

    cp.SpaceAddShape space ground_shape

    let ground =
        Ground
            config.GROUND_COLOR
            config.GROUND_THICKNESS
            ground_shape

    ground

struct Ball
    color : rl.Color
    shape : (pointer cp.Shape)

    fn draw (self)

        let cp-pos =
            cp.BodyGetPosition
                cp.ShapeGetBody self.shape

        rl.DrawCircleV
            cp-to-rl-vec cp-pos
            ((cp.CircleShapeGetRadius self.shape) as f32)
            self.color
        ;

inline init-ball
    config
        space

    local ball_radius = (cp.Float config.BALL_RADIUS)
    local ball_mass = (cp.Float config.BALL_MASS)

    local ball_moment =
        cp.MomentForCircle
            ball_mass
            0
            ball_radius
            config.V_ZERO

    local ball_body =
        cp.SpaceAddBody
            space
            (cp.BodyNew ball_mass ball_moment)

    cp.BodySetPosition
        ball_body
        (rl-to-cp-vec config.BALL_POSITION)

    local ball_shape =
        cp.SpaceAddShape
            space
            cp.CircleShapeNew
                ball_body
                ball_radius
                config.V_ZERO

    cp.ShapeSetElasticity
        ball_shape
        config.BALL_ELASTICITY

    cp.ShapeSetFriction ball_shape config.BALL_FRICTION

    let ball =
        Ball
            config.BALL_COLOR
            ball_shape

    ball


struct Scene
    ground : Ground
    ball : Ball

    fn draw (self)
        'draw self.ball
        'draw self.ground

struct World
    scene : Scene
    space : (pointer cp.Space)

inline init-world (config)

    ## physics space
    local space = (cp.SpaceNew)

    (cp.SpaceSetGravity space config.GRAVITY)

    ## ground
    let ground =
        init-ground
            config
            space

    ## ball
    let ball =
        init-ball
            config
            space

    ## Scene
    let scene =
        Scene
            ground = ground
            ball = ball

    ## World
    let world =
        World
            scene
            space

    print world

    world
    # space


do
    let
        Ground
        Ball
        Scene
        World
        init-ball
        init-ground
        init-world
    locals;
