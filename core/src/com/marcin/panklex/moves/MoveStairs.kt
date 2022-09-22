package com.marcin.panklex.moves

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class MoveStairs(
    val centerPosition : Vector3, val stairsDirection : Direction2d, val startPosition : Vector3,
    val endPosition : Vector3) : Move(startPosition, endPosition)
{
    override fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val directionZ = getDirectionZ(startPosition, endPosition)
        val relativeStairsDirection = objectiveToRelativeDirection2d(stairsDirection, mapDirection)

        when (spacePosition)
        {
            centerPosition -> when (directionZ)
            {
                DirectionZ.Below -> when (relativeStairsDirection)
                {
                    Direction2d.Up    ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveUpBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveUpBelowOutline
                    }
                    Direction2d.Right ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveRightBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveRightBelowOutline
                    }
                    Direction2d.Down  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveDownBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveDownBelowOutline
                    }
                    Direction2d.Left  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveLeftBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveLeftBelowOutline
                    }
                }
                DirectionZ.Above -> when (relativeStairsDirection)
                {
                    Direction2d.Up    ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveUpAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveUpAboveOutline
                    }
                    Direction2d.Right ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveRightAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveRightAboveOutline
                    }
                    Direction2d.Down  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveDownAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveDownAboveOutline
                    }
                    Direction2d.Left  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveLeftAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveLeftAboveOutline
                    }
                }
                null             -> Unit
            }
        }
    }

    override fun getCost() : Int
    {
        return 1
    }

    override fun getPositions(positionList : MutableList<Vector3>)
    {
        positionList.add(centerPosition)
    }
}
