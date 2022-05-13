package com.marcin.panklex

enum class Direction
{
    None, Up, Down, Left, Right
}

enum class Block
{
    Empty, UndamagedStone, DamagedStone, Brick, Metal
}

enum class Action
{
    None, Pickaxe, Bomb, Coin, Cell, Walk
}

enum class MapLayer
{
    Pit, Hole, Base, Block, Wall, Entity, Cover, Action
}
