using import struct
using import Array

let cp = (import chipmunk2d.chipmunk2d)
let rl = (import raylib.raylib)

using import .util
let math = (import .math)

# types

struct GroundState
    color : rl.Color
    thickness : f32
    vecs : (Array rl.Vector2 2)

    inline __repr (self)
        "GroundState"

    fn __copy (self)
        (typeof self)
            (copy self.color)
            (copy self.thickness)
            (copy self.vecs)

    inline blend (self prev blend)

        let blended_vecs =
            (Array rl.Vector2 2)
                math.blend-vec
                    blend
                    (self.vecs @ 0)
                    (prev.vecs @ 0)
                math.blend-vec
                    blend
                    (self.vecs @ 1)
                    (prev.vecs @ 1)

        (typeof self)
            color = self.color
            thickness = self.thickness
            vecs = blended_vecs


    fn draw (self prev-ground blend)

        # ingore blend since this doesn't move

        # then draw the sprite
        rl.DrawLineEx
            (self.vecs @ 0)
            (self.vecs @ 1)
            self.thickness
            self.color
        ;


struct BallState
    color : rl.Color
    position : rl.Vector2
    radius : f32

    inline __repr (self)
        "BallState"

    fn __copy (self)
        (typeof self)
            (copy self.color)
            (copy self.position)

    inline blend (self prev blend)

        let blend_position =
            math.blend-vec
                blend
                self.position
                prev.position

        (typeof self)
            color = self.color
            position = blend_position
            radius = self.radius

    fn draw (self prev-ball blend)

        # blend the current position to get the draw position
        let blended_state = ('blend self prev-ball blend)

        rl.DrawCircleV
            blended_state.position
            self.radius
            self.color
        ;

struct Scene
    ground : GroundState
    ball : BallState

    inline __copy (self)
        (typeof self)
            (copy self.ground)
            (copy self.ball)

    # inline from_config (cls config)
    inline... __typecall
    case (
        cls,
        ground : GroundState,
        ball : BallState
    )

        super-type.__typecall cls
            ground = ground
            ball = ball

    case (cls, config : Scope)

        # convert both ground vecs to raylib
        let rl-vecs =
            (Array rl.Vector2 2)
                (cp-to-rl-vec (config.GROUND_VECS @ 0))
                (cp-to-rl-vec (config.GROUND_VECS @ 1))

        let ground_state =
            GroundState
                color = config.GROUND_COLOR
                thickness = config.GROUND_THICKNESS
                vecs = (move rl-vecs)

        let ball_state =
            BallState
                color = config.BALL_COLOR
                position = (cp-to-rl-vec config.BALL_POSITION)

        super-type.__typecall cls
            ground = ground_state
            ball = ball_state

    inline __repr (self)
        "Scene"


    fn draw (self prev-scene blend)
        'draw self.ball prev-scene.ball blend
        'draw self.ground prev-scene.ground blend

struct GroundPhys
    shape : (pointer cp.Shape)

    inline... __typecall
    case (
        cls,
        shape : (pointer cp.Shape),
    )

        super-type.__typecall cls
            shape

    case (
        cls,
        config : Scope,
        space
    )

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

        super-type.__typecall cls
            shape = ground_shape

    inline __repr (self)
        "GroundPhys"

    fn update-state (self state)

        # update the ground vecs from the physics shape
        (state.vecs @ 0) =
            cp-to-rl-vec (cp.SegmentShapeGetA self.shape)

        (state.vecs @ 1) =
            cp-to-rl-vec (cp.SegmentShapeGetB self.shape)

        ;

struct BallPhys
    shape : (pointer cp.Shape)

    inline __repr (self)
        "BallPhys"

    inline... __typecall
    case (
        cls,
        shape : (pointer cp.Shape)
    )

        super-type.__typecall cls
            shape

    case (
        cls,
        config : Scope,
        space
    )

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

        super-type.__typecall cls
            shape = ball_shape

    fn update-state (self state)

        # update the position of the ball
        let curr-pos =
            cp.BodyGetPosition
                cp.ShapeGetBody self.shape

        state.position =
            cp-to-rl-vec curr-pos

        # update the radius of the ball
        state.radius = ((cp.CircleShapeGetRadius self.shape) as f32)
        ;

struct PhysicsWorld
    space : (mutable pointer cp.Space)
    ground : GroundPhys
    ball : BallPhys


    inline __repr (self)
        ..
            "PhysicsWorld["
            (repr self.space)
            ";"
            (repr self.ground)
            ";"
            (repr self.ball)
            "]"


    inline... __typecall
    case (
        cls,
        space : (mutable pointer cp.Space),
        ground : GroundPhys,
        ball : BallPhys
    )

        super-type.__typecall cls
            space
            ground
            ball

    case (cls, config : Scope)

        ## physics space
        local space = (cp.SpaceNew)

        (cp.SpaceSetGravity space config.GRAVITY)

        super-type.__typecall cls
            space = space
            ground =
                GroundPhys
                    config
                    space
            ball =
                BallPhys
                    config
                    space

    fn physics-step (self time_step)
        (cp.SpaceStep self.space time_step)
        ;

    fn update-scene (self scene)
        'update-state self.ground scene.ground
        'update-state self.ball scene.ball


struct World
    scene : Scene
    phys-world : PhysicsWorld

    inline... __typecall
    case (
        cls,
        scene : Scene,
        phys-world : PhysicsWorld
    )

        super-type.__typecall cls
            scene
            phys-world

    case (
        cls,
        config : Scope
    )

        let scene = (Scene config)

        let phys-world = (PhysicsWorld config)

        let self =
            super-type.__typecall cls
                scene = scene
                phys-world = phys-world

        self

    inline __repr (self)
        "World"

    fn update (self)
        'update-scene self.phys-world self.scene

do
    let
        GroundState
        BallState
        Scene
        GroundPhys
        BallPhys
        PhysicsWorld
        World
    locals;
