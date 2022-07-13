using import struct

let cp = (import chipmunk2d.chipmunk2d)
let rl = (import raylib.raylib)

using import .util

""""Two element vector type
struct Vector2
    x : f32
    y : f32

""""Type alias for Vector2
let Vec2 = Vector2

inline blend-vec (blend target_vec source_vec)

    (typeof target_vec)
        (target_vec.x * blend) + (source_vec.x * (1.0 - blend))
        (target_vec.y * blend) + (source_vec.y * (1.0 - blend))

do
    let
        blend-vec
        Vector2
        Vec2

    locals;
