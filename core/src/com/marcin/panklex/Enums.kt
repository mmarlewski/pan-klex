package com.marcin.panklex

import com.badlogic.gdx.math.Vector3

enum class DirectionX
{
    Right, Left
}

enum class DirectionY
{
    Up, Down
}

enum class DirectionZ
{
    Above, Below
}

enum class Direction2d
{
    Up, Right, Down, Left
}

enum class Direction3d
{
    Up, Right, Down, Left, Above, Below
}

enum class Dist1Conn
{
    Up, UpRight, Right, DownRight, Down, DownLeft, Left, UpLeft, Center
}

enum class Dist2Conn
{
    Up, UpUpRight, UpRight, UpRightRight, Right, DownRightRight, DownRight, DownDownRight, Down, DownDownLeft, DownLeft,
    DownLeftLeft, Left, UpLeftLeft, UpLeft, UpUpLeft
}

enum class Dist3Conn
{
    Up, UpUpUpRight, UpUpRight, UpRight, UpRightRight, UpRightRightRight, Right, DownRightRightRight, DownRightRight,
    DownRight, DownDownRight, DownDownDownRight, Down, DownDownDownLeft, DownDownLeft, DownLeft, DownLeftLeft,
    DownLeftLeftLeft, Left, UpLeftLeftLeft, UpLeftLeft, UpLeft, UpUpLeft, UpUpUpLeft
}

enum class SpaceLayer
{
    SelectBack, LinesBelow, SideBelow, LinesLeft, SideLeft, LinesUp, SideUp, Behind, EntityWhole, Before, SideDown,
    SideRight, SideAbove, MoveWhole, SelectFront, EntityOutline, MoveOutline
}

fun getDirectionX(startPosition : Vector3, endPosition : Vector3) : DirectionX?
{
    return when
    {
        (startPosition.x < endPosition.x) -> DirectionX.Right
        (startPosition.x > endPosition.x) -> DirectionX.Left
        else                              -> null
    }
}

fun getDirectionY(startPosition : Vector3, endPosition : Vector3) : DirectionY?
{
    return when
    {
        (startPosition.y < endPosition.y) -> DirectionY.Up
        (startPosition.y > endPosition.y) -> DirectionY.Down
        else                              -> null
    }
}

fun getDirectionZ(startPosition : Vector3, endPosition : Vector3) : DirectionZ?
{
    return when
    {
        (startPosition.z < endPosition.z) -> DirectionZ.Above
        (startPosition.z > endPosition.z) -> DirectionZ.Below
        else                              -> null
    }
}

fun getDirection2d(startPosition : Vector3, endPosition : Vector3) : Direction2d
{
    val directionX = getDirectionX(startPosition, endPosition)
    val directionY = getDirectionY(startPosition, endPosition)

    return when
    {
        directionY == DirectionY.Up && directionX == null    -> Direction2d.Up
        directionY == null && directionX == DirectionX.Right -> Direction2d.Right
        directionY == DirectionY.Down && directionX == null  -> Direction2d.Down
        directionY == null && directionX == DirectionX.Left  -> Direction2d.Left
        else                                                 -> Direction2d.Up
    }
}

fun getDist1Conn(startPosition : Vector3, endPosition : Vector3) : Dist1Conn
{
    val directionX = getDirectionX(startPosition, endPosition)
    val directionY = getDirectionY(startPosition, endPosition)

    return when
    {
        directionY == DirectionY.Up && directionX == null               -> Dist1Conn.Up
        directionY == DirectionY.Up && directionX == DirectionX.Right   -> Dist1Conn.UpRight
        directionY == null && directionX == DirectionX.Right            -> Dist1Conn.Right
        directionY == DirectionY.Down && directionX == DirectionX.Right -> Dist1Conn.DownRight
        directionY == DirectionY.Down && directionX == null             -> Dist1Conn.Down
        directionY == DirectionY.Down && directionX == DirectionX.Left  -> Dist1Conn.DownLeft
        directionY == null && directionX == DirectionX.Left             -> Dist1Conn.Left
        directionY == DirectionY.Up && directionX == DirectionX.Left    -> Dist1Conn.UpLeft
        else                                                            -> Dist1Conn.Center
    }
}

fun getPositionDist1Conn(position : Vector3, direction : Dist1Conn) : Vector3
{
    return when (direction)
    {
        Dist1Conn.Up        -> Vector3(position.x, position.y + 1, position.z)
        Dist1Conn.UpRight   -> Vector3(position.x + 1, position.y + 1, position.z)
        Dist1Conn.Right     -> Vector3(position.x + 1, position.y, position.z)
        Dist1Conn.DownRight -> Vector3(position.x + 1, position.y - 1, position.z)
        Dist1Conn.Down      -> Vector3(position.x, position.y - 1, position.z)
        Dist1Conn.DownLeft  -> Vector3(position.x - 1, position.y - 1, position.z)
        Dist1Conn.Left      -> Vector3(position.x - 1, position.y, position.z)
        Dist1Conn.UpLeft    -> Vector3(position.x - 1, position.y + 1, position.z)
        Dist1Conn.Center    -> Vector3(position)
    }
}

fun getPositionDist2Conn(position : Vector3, direction : Dist2Conn) : Vector3
{
    return when (direction)
    {
        Dist2Conn.Up             -> Vector3(position.x + 0, position.y + 2, position.z)
        Dist2Conn.UpUpRight      -> Vector3(position.x + 1, position.y + 2, position.z)
        Dist2Conn.UpRight        -> Vector3(position.x + 2, position.y + 2, position.z)
        Dist2Conn.UpRightRight   -> Vector3(position.x + 2, position.y + 1, position.z)
        Dist2Conn.Right          -> Vector3(position.x + 2, position.y + 0, position.z)
        Dist2Conn.DownRightRight -> Vector3(position.x + 2, position.y - 1, position.z)
        Dist2Conn.DownRight      -> Vector3(position.x + 2, position.y - 2, position.z)
        Dist2Conn.DownDownRight  -> Vector3(position.x + 1, position.y - 2, position.z)
        Dist2Conn.Down           -> Vector3(position.x + 0, position.y - 2, position.z)
        Dist2Conn.DownDownLeft   -> Vector3(position.x - 1, position.y - 2, position.z)
        Dist2Conn.DownLeft       -> Vector3(position.x - 2, position.y - 2, position.z)
        Dist2Conn.DownLeftLeft   -> Vector3(position.x - 2, position.y - 1, position.z)
        Dist2Conn.Left           -> Vector3(position.x - 2, position.y + 0, position.z)
        Dist2Conn.UpLeftLeft     -> Vector3(position.x - 2, position.y + 1, position.z)
        Dist2Conn.UpLeft         -> Vector3(position.x - 2, position.y + 2, position.z)
        Dist2Conn.UpUpLeft       -> Vector3(position.x - 1, position.y + 2, position.z)
    }
}

fun getPositionDist3Conn(position : Vector3, direction : Dist3Conn) : Vector3
{
    return when (direction)
    {
        Dist3Conn.Up                  -> Vector3(position.x + 0, position.y + 3, position.z)
        Dist3Conn.UpUpUpRight         -> Vector3(position.x + 1, position.y + 3, position.z)
        Dist3Conn.UpUpRight           -> Vector3(position.x + 2, position.y + 3, position.z)
        Dist3Conn.UpRight             -> Vector3(position.x + 3, position.y + 3, position.z)
        Dist3Conn.UpRightRight        -> Vector3(position.x + 3, position.y + 2, position.z)
        Dist3Conn.UpRightRightRight   -> Vector3(position.x + 3, position.y + 1, position.z)
        Dist3Conn.Right               -> Vector3(position.x + 3, position.y + 0, position.z)
        Dist3Conn.DownRightRightRight -> Vector3(position.x + 3, position.y - 1, position.z)
        Dist3Conn.DownRightRight      -> Vector3(position.x + 3, position.y - 2, position.z)
        Dist3Conn.DownRight           -> Vector3(position.x + 3, position.y - 3, position.z)
        Dist3Conn.DownDownRight       -> Vector3(position.x + 2, position.y - 3, position.z)
        Dist3Conn.DownDownDownRight   -> Vector3(position.x + 1, position.y - 3, position.z)
        Dist3Conn.Down                -> Vector3(position.x + 0, position.y - 3, position.z)
        Dist3Conn.DownDownDownLeft    -> Vector3(position.x - 1, position.y - 3, position.z)
        Dist3Conn.DownDownLeft        -> Vector3(position.x - 2, position.y - 3, position.z)
        Dist3Conn.DownLeft            -> Vector3(position.x - 3, position.y - 3, position.z)
        Dist3Conn.DownLeftLeft        -> Vector3(position.x - 3, position.y - 2, position.z)
        Dist3Conn.DownLeftLeftLeft    -> Vector3(position.x - 3, position.y - 1, position.z)
        Dist3Conn.Left                -> Vector3(position.x - 3, position.y + 0, position.z)
        Dist3Conn.UpLeftLeftLeft      -> Vector3(position.x - 3, position.y + 1, position.z)
        Dist3Conn.UpLeftLeft          -> Vector3(position.x - 3, position.y + 2, position.z)
        Dist3Conn.UpLeft              -> Vector3(position.x - 3, position.y + 3, position.z)
        Dist3Conn.UpUpLeft            -> Vector3(position.x - 2, position.y + 3, position.z)
        Dist3Conn.UpUpUpLeft          -> Vector3(position.x - 1, position.y + 3, position.z)
    }
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

fun oppositeDist1Conn(dist1Conn : Dist1Conn) : Dist1Conn
{
    return when (dist1Conn)
    {
        Dist1Conn.Up        -> Dist1Conn.Down
        Dist1Conn.UpRight   -> Dist1Conn.DownLeft
        Dist1Conn.Right     -> Dist1Conn.Left
        Dist1Conn.DownRight -> Dist1Conn.UpLeft
        Dist1Conn.Down      -> Dist1Conn.Up
        Dist1Conn.DownLeft  -> Dist1Conn.UpRight
        Dist1Conn.Left      -> Dist1Conn.Right
        Dist1Conn.UpLeft    -> Dist1Conn.DownRight
        Dist1Conn.Center    -> Dist1Conn.Center
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

fun objectiveToRelativeDist1Conn(relativeDist1Conn : Dist1Conn, mapDirection : Direction2d) : Dist1Conn
{
    return when (relativeDist1Conn)
    {
        Dist1Conn.Up        -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.Up
            Direction2d.Right -> Dist1Conn.Left
            Direction2d.Down  -> Dist1Conn.Down
            Direction2d.Left  -> Dist1Conn.Right
        }
        Dist1Conn.UpRight   -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.UpRight
            Direction2d.Right -> Dist1Conn.UpLeft
            Direction2d.Down  -> Dist1Conn.DownLeft
            Direction2d.Left  -> Dist1Conn.DownRight
        }
        Dist1Conn.Right     -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.Right
            Direction2d.Right -> Dist1Conn.Up
            Direction2d.Down  -> Dist1Conn.Left
            Direction2d.Left  -> Dist1Conn.Down
        }
        Dist1Conn.DownRight -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.DownRight
            Direction2d.Right -> Dist1Conn.UpRight
            Direction2d.Down  -> Dist1Conn.UpLeft
            Direction2d.Left  -> Dist1Conn.DownLeft
        }
        Dist1Conn.Down      -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.Down
            Direction2d.Right -> Dist1Conn.Right
            Direction2d.Down  -> Dist1Conn.Up
            Direction2d.Left  -> Dist1Conn.Left
        }
        Dist1Conn.DownLeft  -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.DownLeft
            Direction2d.Right -> Dist1Conn.DownRight
            Direction2d.Down  -> Dist1Conn.UpRight
            Direction2d.Left  -> Dist1Conn.UpLeft
        }
        Dist1Conn.Left      -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.Left
            Direction2d.Right -> Dist1Conn.Down
            Direction2d.Down  -> Dist1Conn.Right
            Direction2d.Left  -> Dist1Conn.Up
        }
        Dist1Conn.UpLeft    -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.UpLeft
            Direction2d.Right -> Dist1Conn.DownLeft
            Direction2d.Down  -> Dist1Conn.DownRight
            Direction2d.Left  -> Dist1Conn.UpRight
        }
        Dist1Conn.Center    -> Dist1Conn.Center
    }
}

fun relativeToObjectiveDist1Conn(objectiveDist1Conn : Dist1Conn, mapDirection : Direction2d) : Dist1Conn
{
    return when (objectiveDist1Conn)
    {
        Dist1Conn.Up        -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.UpRight
            Direction2d.Right -> Dist1Conn.Right
            Direction2d.Down  -> Dist1Conn.DownRight
            Direction2d.Left  -> Dist1Conn.Left
        }
        Dist1Conn.UpRight   -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.UpRight
            Direction2d.Right -> Dist1Conn.DownRight
            Direction2d.Down  -> Dist1Conn.DownLeft
            Direction2d.Left  -> Dist1Conn.UpLeft
        }
        Dist1Conn.Right     -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.UpRight
            Direction2d.Right -> Dist1Conn.Down
            Direction2d.Down  -> Dist1Conn.Left
            Direction2d.Left  -> Dist1Conn.Up
        }
        Dist1Conn.DownRight -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.UpRight
            Direction2d.Right -> Dist1Conn.DownLeft
            Direction2d.Down  -> Dist1Conn.UpLeft
            Direction2d.Left  -> Dist1Conn.UpRight
        }
        Dist1Conn.Down      -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.UpRight
            Direction2d.Right -> Dist1Conn.Left
            Direction2d.Down  -> Dist1Conn.Up
            Direction2d.Left  -> Dist1Conn.Right
        }
        Dist1Conn.DownLeft  -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.UpRight
            Direction2d.Right -> Dist1Conn.UpLeft
            Direction2d.Down  -> Dist1Conn.UpRight
            Direction2d.Left  -> Dist1Conn.DownRight
        }
        Dist1Conn.Left      -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.UpRight
            Direction2d.Right -> Dist1Conn.Up
            Direction2d.Down  -> Dist1Conn.Right
            Direction2d.Left  -> Dist1Conn.Down
        }
        Dist1Conn.UpLeft    -> when (mapDirection)
        {
            Direction2d.Up    -> Dist1Conn.UpRight
            Direction2d.Right -> Dist1Conn.UpRight
            Direction2d.Down  -> Dist1Conn.DownRight
            Direction2d.Left  -> Dist1Conn.DownLeft
        }
        Dist1Conn.Center    -> Dist1Conn.Center
    }
}

fun dist3ConnToDist2Conn(dist3Conn : Dist3Conn) : Dist2Conn
{
    return when (dist3Conn)
    {
        Dist3Conn.Up                  -> Dist2Conn.Up
        Dist3Conn.UpUpUpRight         -> Dist2Conn.Up
        Dist3Conn.UpUpRight           -> Dist2Conn.UpUpRight
        Dist3Conn.UpRight             -> Dist2Conn.UpRight
        Dist3Conn.UpRightRight        -> Dist2Conn.UpRightRight
        Dist3Conn.UpRightRightRight   -> Dist2Conn.Right
        Dist3Conn.Right               -> Dist2Conn.Right
        Dist3Conn.DownRightRightRight -> Dist2Conn.Right
        Dist3Conn.DownRightRight      -> Dist2Conn.DownRightRight
        Dist3Conn.DownRight           -> Dist2Conn.DownRight
        Dist3Conn.DownDownRight       -> Dist2Conn.DownDownRight
        Dist3Conn.DownDownDownRight   -> Dist2Conn.Down
        Dist3Conn.Down                -> Dist2Conn.Down
        Dist3Conn.DownDownDownLeft    -> Dist2Conn.Down
        Dist3Conn.DownDownLeft        -> Dist2Conn.DownDownLeft
        Dist3Conn.DownLeft            -> Dist2Conn.DownLeft
        Dist3Conn.DownLeftLeft        -> Dist2Conn.DownLeftLeft
        Dist3Conn.DownLeftLeftLeft    -> Dist2Conn.Left
        Dist3Conn.Left                -> Dist2Conn.Left
        Dist3Conn.UpLeftLeftLeft      -> Dist2Conn.Left
        Dist3Conn.UpLeftLeft          -> Dist2Conn.UpLeftLeft
        Dist3Conn.UpLeft              -> Dist2Conn.UpLeft
        Dist3Conn.UpUpLeft            -> Dist2Conn.UpUpLeft
        Dist3Conn.UpUpUpLeft          -> Dist2Conn.Up
    }
}

fun dist3ConnToDist1Conn(dist3Conn : Dist3Conn) : Dist1Conn
{
    return when (dist3Conn)
    {
        Dist3Conn.Up                  -> Dist1Conn.Up
        Dist3Conn.UpUpUpRight         -> Dist1Conn.Up
        Dist3Conn.UpUpRight           -> Dist1Conn.UpRight
        Dist3Conn.UpRight             -> Dist1Conn.UpRight
        Dist3Conn.UpRightRight        -> Dist1Conn.UpRight
        Dist3Conn.UpRightRightRight   -> Dist1Conn.Right
        Dist3Conn.Right               -> Dist1Conn.Right
        Dist3Conn.DownRightRightRight -> Dist1Conn.Right
        Dist3Conn.DownRightRight      -> Dist1Conn.DownRight
        Dist3Conn.DownRight           -> Dist1Conn.DownRight
        Dist3Conn.DownDownRight       -> Dist1Conn.DownRight
        Dist3Conn.DownDownDownRight   -> Dist1Conn.Down
        Dist3Conn.Down                -> Dist1Conn.Down
        Dist3Conn.DownDownDownLeft    -> Dist1Conn.Down
        Dist3Conn.DownDownLeft        -> Dist1Conn.DownLeft
        Dist3Conn.DownLeft            -> Dist1Conn.DownLeft
        Dist3Conn.DownLeftLeft        -> Dist1Conn.DownLeft
        Dist3Conn.DownLeftLeftLeft    -> Dist1Conn.Left
        Dist3Conn.Left                -> Dist1Conn.Left
        Dist3Conn.UpLeftLeftLeft      -> Dist1Conn.Left
        Dist3Conn.UpLeftLeft          -> Dist1Conn.UpLeft
        Dist3Conn.UpLeft              -> Dist1Conn.UpLeft
        Dist3Conn.UpUpLeft            -> Dist1Conn.UpLeft
        Dist3Conn.UpUpUpLeft          -> Dist1Conn.Up
    }
}

fun dist2ConnToDist1Conn(dist2Conn : Dist2Conn) : Dist1Conn
{
    return when (dist2Conn)
    {
        Dist2Conn.Up             -> Dist1Conn.Up
        Dist2Conn.UpUpRight      -> Dist1Conn.UpRight
        Dist2Conn.UpRight        -> Dist1Conn.UpRight
        Dist2Conn.UpRightRight   -> Dist1Conn.UpRight
        Dist2Conn.Right          -> Dist1Conn.Right
        Dist2Conn.DownRightRight -> Dist1Conn.DownRight
        Dist2Conn.DownRight      -> Dist1Conn.DownRight
        Dist2Conn.DownDownRight  -> Dist1Conn.DownRight
        Dist2Conn.Down           -> Dist1Conn.Down
        Dist2Conn.DownDownLeft   -> Dist1Conn.DownLeft
        Dist2Conn.DownLeft       -> Dist1Conn.DownLeft
        Dist2Conn.DownLeftLeft   -> Dist1Conn.DownLeft
        Dist2Conn.Left           -> Dist1Conn.Left
        Dist2Conn.UpLeftLeft     -> Dist1Conn.UpLeft
        Dist2Conn.UpLeft         -> Dist1Conn.UpLeft
        Dist2Conn.UpUpLeft       -> Dist1Conn.UpLeft
    }
}

fun dist1ConnToDirection2d(dist1Conn : Dist1Conn) : Direction2d?
{
    return when (dist1Conn)
    {
        Dist1Conn.Up        -> Direction2d.Up
        Dist1Conn.UpRight   -> null
        Dist1Conn.Right     -> Direction2d.Right
        Dist1Conn.DownRight -> null
        Dist1Conn.Down      -> Direction2d.Down
        Dist1Conn.DownLeft  -> null
        Dist1Conn.Left      -> Direction2d.Left
        Dist1Conn.UpLeft    -> null
        Dist1Conn.Center    -> null
    }
}
