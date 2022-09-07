package com.marcin.panklex

//                 ABOVE
//     ------------------------------  |
//                ___/\___             |
//      LEFT  ___/        \___  UP     |
//        ___/                \___     |
//       /                        \    |
//       \___                  ___/    |
//           \___          ___/        |
//     DOWN      \___  ___/     RIGHT  |
//                   \/                |
//     ------------------------------  |
//                 BELOW               |

enum class Direction2d
{
    Up, Right, Down, Left
}

enum class Direction3d
{
    Up, Right, Down, Left, Above, Below
}

enum class SpaceLayer
{
    SelectBack, LinesBelow, SideBelow, LinesLeft, SideLeft, LinesUp, SideUp, Behind, EntityWhole, Before, SideDown, SideRight,
    SideAbove, SelectFront, EntityOutline
}

fun direction2dToDirection3d(direction2d : Direction2d) : Direction3d
{
    return when (direction2d)
    {
        Direction2d.Up    -> Direction3d.Up
        Direction2d.Right -> Direction3d.Right
        Direction2d.Down  -> Direction3d.Down
        Direction2d.Left  -> Direction3d.Left
    }
}

fun direction3dToDirection2d(direction3d : Direction3d) : Direction2d?
{
    return when (direction3d)
    {
        Direction3d.Up    -> Direction2d.Up
        Direction3d.Right -> Direction2d.Right
        Direction3d.Down  -> Direction2d.Down
        Direction3d.Left  -> Direction2d.Left
        else              -> null
    }
}

fun spaceLayerToDirection3d(spaceLayer : SpaceLayer) : Direction3d?
{
    return when (spaceLayer)
    {
        SpaceLayer.SideBelow  -> Direction3d.Below
        SpaceLayer.LinesBelow -> Direction3d.Below
        SpaceLayer.SideLeft   -> Direction3d.Left
        SpaceLayer.LinesLeft  -> Direction3d.Left
        SpaceLayer.SideUp     -> Direction3d.Up
        SpaceLayer.LinesUp    -> Direction3d.Up
        SpaceLayer.SideDown   -> Direction3d.Down
        SpaceLayer.SideRight  -> Direction3d.Right
        SpaceLayer.SideAbove  -> Direction3d.Above
        else                  -> null
    }
}

fun direction3dToSpaceLayer(direction3d : Direction3d) : SpaceLayer
{
    return when (direction3d)
    {
        Direction3d.Below -> SpaceLayer.SideBelow
        Direction3d.Left  -> SpaceLayer.SideLeft
        Direction3d.Up    -> SpaceLayer.SideUp
        Direction3d.Down  -> SpaceLayer.SideDown
        Direction3d.Right -> SpaceLayer.SideRight
        Direction3d.Above -> SpaceLayer.SideAbove
    }
}

fun direction2dToRight(direction2d : Direction2d) : Direction2d
{
    return when (direction2d)
    {
        Direction2d.Up    -> Direction2d.Right
        Direction2d.Right -> Direction2d.Down
        Direction2d.Down  -> Direction2d.Left
        Direction2d.Left  -> Direction2d.Up
    }
}

fun direction2dToLeft(direction2d : Direction2d) : Direction2d
{
    return when (direction2d)
    {
        Direction2d.Up    -> Direction2d.Left
        Direction2d.Left  -> Direction2d.Down
        Direction2d.Down  -> Direction2d.Right
        Direction2d.Right -> Direction2d.Up
    }
}

fun oppositeDirection2d(direction2d : Direction2d) : Direction2d
{
    return when (direction2d)
    {
        Direction2d.Left  -> Direction2d.Right
        Direction2d.Up    -> Direction2d.Down
        Direction2d.Down  -> Direction2d.Up
        Direction2d.Right -> Direction2d.Left
    }
}

fun oppositeDirection3d(direction3d : Direction3d) : Direction3d
{
    return when (direction3d)
    {
        Direction3d.Below -> Direction3d.Above
        Direction3d.Left  -> Direction3d.Right
        Direction3d.Up    -> Direction3d.Down
        Direction3d.Down  -> Direction3d.Up
        Direction3d.Right -> Direction3d.Left
        Direction3d.Above -> Direction3d.Below
    }
}

fun objectiveToRelativeDirection2d(objectiveDirection2d : Direction2d, mapDirection : Direction2d) : Direction2d
{
    return when (objectiveDirection2d)
    {
        Direction2d.Left  -> when (mapDirection)
        {
            Direction2d.Up    -> Direction2d.Left
            Direction2d.Right -> Direction2d.Down
            Direction2d.Down  -> Direction2d.Right
            Direction2d.Left  -> Direction2d.Up
        }
        Direction2d.Up    -> when (mapDirection)
        {
            Direction2d.Up    -> Direction2d.Up
            Direction2d.Right -> Direction2d.Left
            Direction2d.Down  -> Direction2d.Down
            Direction2d.Left  -> Direction2d.Right
        }
        Direction2d.Down  -> when (mapDirection)
        {
            Direction2d.Up    -> Direction2d.Down
            Direction2d.Right -> Direction2d.Right
            Direction2d.Down  -> Direction2d.Up
            Direction2d.Left  -> Direction2d.Left
        }
        Direction2d.Right -> when (mapDirection)
        {
            Direction2d.Up    -> Direction2d.Right
            Direction2d.Right -> Direction2d.Up
            Direction2d.Down  -> Direction2d.Left
            Direction2d.Left  -> Direction2d.Down
        }
    }
}

fun relativeToObjectiveDirection2d(objectiveDirection2d : Direction2d, mapDirection : Direction2d) : Direction2d
{
    return when (objectiveDirection2d)
    {
        Direction2d.Left  -> when (mapDirection)
        {
            Direction2d.Up    -> Direction2d.Left
            Direction2d.Right -> Direction2d.Up
            Direction2d.Down  -> Direction2d.Right
            Direction2d.Left  -> Direction2d.Down
        }
        Direction2d.Up    -> when (mapDirection)
        {
            Direction2d.Up    -> Direction2d.Up
            Direction2d.Right -> Direction2d.Right
            Direction2d.Down  -> Direction2d.Down
            Direction2d.Left  -> Direction2d.Left
        }
        Direction2d.Down  -> when (mapDirection)
        {
            Direction2d.Up    -> Direction2d.Down
            Direction2d.Right -> Direction2d.Left
            Direction2d.Down  -> Direction2d.Up
            Direction2d.Left  -> Direction2d.Right
        }
        Direction2d.Right -> when (mapDirection)
        {
            Direction2d.Up    -> Direction2d.Right
            Direction2d.Right -> Direction2d.Down
            Direction2d.Down  -> Direction2d.Left
            Direction2d.Left  -> Direction2d.Up
        }
    }
}

fun objectiveToRelativeDirection3d(objectiveDirection3d : Direction3d, mapDirection : Direction2d) : Direction3d
{
    return when (objectiveDirection3d)
    {
        Direction3d.Below -> Direction3d.Below
        Direction3d.Left  -> when (mapDirection)
        {
            Direction2d.Up    -> Direction3d.Left
            Direction2d.Right -> Direction3d.Down
            Direction2d.Down  -> Direction3d.Right
            Direction2d.Left  -> Direction3d.Up
        }
        Direction3d.Up    -> when (mapDirection)
        {
            Direction2d.Up    -> Direction3d.Up
            Direction2d.Right -> Direction3d.Left
            Direction2d.Down  -> Direction3d.Down
            Direction2d.Left  -> Direction3d.Right
        }
        Direction3d.Down  -> when (mapDirection)
        {
            Direction2d.Up    -> Direction3d.Down
            Direction2d.Right -> Direction3d.Right
            Direction2d.Down  -> Direction3d.Up
            Direction2d.Left  -> Direction3d.Left
        }
        Direction3d.Right -> when (mapDirection)
        {
            Direction2d.Up    -> Direction3d.Right
            Direction2d.Right -> Direction3d.Up
            Direction2d.Down  -> Direction3d.Left
            Direction2d.Left  -> Direction3d.Down
        }
        Direction3d.Above -> Direction3d.Above
    }
}

fun relativeToObjectiveDirection3d(objectiveDirection3d : Direction3d, mapDirection : Direction2d) : Direction3d
{
    return when (objectiveDirection3d)
    {
        Direction3d.Below -> Direction3d.Below
        Direction3d.Left  -> when (mapDirection)
        {
            Direction2d.Up    -> Direction3d.Left
            Direction2d.Right -> Direction3d.Up
            Direction2d.Down  -> Direction3d.Right
            Direction2d.Left  -> Direction3d.Down
        }
        Direction3d.Up    -> when (mapDirection)
        {
            Direction2d.Up    -> Direction3d.Up
            Direction2d.Right -> Direction3d.Right
            Direction2d.Down  -> Direction3d.Down
            Direction2d.Left  -> Direction3d.Left
        }
        Direction3d.Down  -> when (mapDirection)
        {
            Direction2d.Up    -> Direction3d.Down
            Direction2d.Right -> Direction3d.Left
            Direction2d.Down  -> Direction3d.Up
            Direction2d.Left  -> Direction3d.Right
        }
        Direction3d.Right -> when (mapDirection)
        {
            Direction2d.Up    -> Direction3d.Right
            Direction2d.Right -> Direction3d.Down
            Direction2d.Down  -> Direction3d.Left
            Direction2d.Left  -> Direction3d.Up
        }
        Direction3d.Above -> Direction3d.Above
    }
}
