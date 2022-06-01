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



struct Scene
    ground : Ground
    ball : Ball

    fn draw (self)
        'draw self.ball
        'draw self.ground

do
    let Ground Ball Scene
    locals;
