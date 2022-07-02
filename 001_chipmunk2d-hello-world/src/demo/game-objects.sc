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

    inline __typecall
        cls
            config
            space

        # construct the shape
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

        # then construct the actual struct
        let self =
            super-type.__typecall cls
                color = config.GROUND_COLOR
                thickness = config.GROUND_THICKNESS
                shape = ground_shape

        self

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

    inline __typecall
        cls
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

        let self =
            super-type.__typecall cls
                color = config.BALL_COLOR
                shape = ball_shape

        self


struct Scene
    ground : Ground
    ball : Ball

    fn draw (self)
        'draw self.ball
        'draw self.ground

struct World
    scene : Scene
    space : (mutable pointer cp.Space)

    inline __typecall (cls config)

        ## physics space
        local space = (cp.SpaceNew)

        (cp.SpaceSetGravity space config.GRAVITY)

        ## ground
        let ground =
            Ground
                config
                space

        ## ball
        let ball =
            Ball
                config
                space

        ## Scene
        let scene =
            Scene
                ground = ground
                ball = ball

        let self =
            super-type.__typecall cls
                scene = scene
                space = space

        self

    fn physics-step (self time_step)
        (cp.SpaceStep self.space time_step)
        ;


do
    let
        Ground
        Ball
        Scene
        World
    locals;
