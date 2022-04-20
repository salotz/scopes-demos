using import struct
using import Array

let cp = (import chipmunk2d.chipmunk2d)
let rl = (import raylib.raylib)

# types
struct Ground
    color : rl.Color
    thickness : f32
    shape : (pointer cp.Shape)

    fn show-color (self)
        print self.color

struct Ball
    color : rl.Color
    shape : (pointer cp.Shape)


struct Scene
    ground : Ground
    ball : Ball

do
    let Ground Ball Scene
    locals;
