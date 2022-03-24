let rl = (import raylib.raylib)

let cp = (import .chipmunk2d)


# let cpvzero = (cp.Vect 0.0 0.0)

let cpvzero = (rl.Vector2 0.0 0.0)

let gravity =
    cp.Vect
        0
        -100

local space = (cp.SpaceNew)

cp.SpaceSetGravity space gravity

local ground =
    cp.SegmentShapeNew
        (cp.SpaceGetStaticBody space)
        (cp.Vect -20 5)
        (cp.Vect 20 -5)
        0

cp.ShapeSetFriction ground 1
cp.SpaceAddShape space ground

local radius = (cp.Float 5)
local mass = (cp.Float 1)

local moment =
    (cp.MomentForCircle mass 0 radius cpvzero)

local ballBody =
    cp.SpaceAddBody
        space
        (cp.BodyNew mass moment)

cp.BodySetPosition ballBody (cp.Vect 0 15)

local ballShape =
    cp.SpaceAddShape
        space
        cp.CircleShapeNew
            ballBody
            radius
            cpvzero

cp.ShapeSetFriction ballShape 0.7

let timeStep = (1.0 / 60.0)

loop (time = 0.0)

    if (time < 2.0)

        let pos = (cp.BodyGetPosition ballBody)
        let vel = (cp.BodyGetVelocity ballBody)

        print "Time is " time " ballBody is at (" pos.x ", " pos.y "). It's velocity is (" vel.x ", " vel.y ")"

        (cp.SpaceStep space timeStep)

        repeat (time + timeStep)

    else
        break time

;
