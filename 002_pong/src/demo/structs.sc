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
