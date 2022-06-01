using import struct
using import Array

let cp = (import chipmunk2d.chipmunk2d)
let rl = (import raylib.raylib)

struct Ground < Struct
    # color : rl.Color
    color : i32

# let g =
#     Ground
#         rl.Colors.GRAY
