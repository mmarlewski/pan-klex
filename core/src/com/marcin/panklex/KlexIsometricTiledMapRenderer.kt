package com.marcin.panklex

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import kotlin.math.sqrt

// i have no idea what im doing
class KlexIsometricTiledMapRenderer(map : TiledMap, unitScale : Float, val floor : Int, val entity : TextureRegion) :
    IsometricTiledMapRenderer(map, unitScale)
{
    private var isoTransform : Matrix4? = null
    private var invIsotransform : Matrix4? = null
    private val screenPos = Vector3()

    private val topRight = Vector2()
    private val bottomLeft = Vector2()
    private val topLeft = Vector2()
    private val bottomRight = Vector2()

    init
    {
        isoTransform = Matrix4()
        isoTransform!!.idt()

        isoTransform!!.scale((sqrt(2.0) / 2.0).toFloat(), (sqrt(2.0) / 4.0).toFloat(), 1.0f)
        isoTransform!!.rotate(0.0f, 0.0f, 1.0f, -45f)

        invIsotransform = Matrix4(isoTransform)
        invIsotransform!!.inv()
    }

    private fun translateScreenToIso(vec : Vector2) : Vector3
    {
        screenPos[vec.x, vec.y] = 0f
        screenPos.mul(invIsotransform)
        return screenPos
    }

    // thank you random guy from the internet from 2015 Tiled forum post
    fun klexRender(width : Int, height : Int)
    {
        beginRender()

        // removed a little from original

        val batchColor : Color = batch.color
        val tileWidth : Float = width * unitScale
        val tileHeight : Float = height * unitScale
        val halfTileWidth = tileWidth * 0.5f
        val halfTileHeight = tileHeight * 0.5f

        topRight[viewBounds.x + viewBounds.width] = viewBounds.y
        bottomLeft[viewBounds.x] = viewBounds.y + viewBounds.height
        topLeft[viewBounds.x] = viewBounds.y
        bottomRight[viewBounds.x + viewBounds.width] = viewBounds.y + viewBounds.height

        val row1 = (translateScreenToIso(topLeft).y / tileWidth).toInt() - 2
        val row2 = (translateScreenToIso(bottomRight).y / tileWidth).toInt() + 2
        val col1 = (translateScreenToIso(bottomLeft).x / tileWidth).toInt() - 2
        val col2 = (translateScreenToIso(topRight).x / tileWidth).toInt() + 2

        for (row in row2 downTo row1)
        {
            for (col in col1..col2)
            {
                // draw every layer of each tile ...
                // ... instead of drawing every tile of each layer
                for (layer in map.layers)
                {
                    if (layer.isVisible && layer is TiledMapTileLayer)
                    {
                        val color =
                            Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.opacity)
                        val x = col * halfTileWidth + row * halfTileWidth
                        // draw each layer higher than previous
                        val y = row * halfTileHeight - col * halfTileHeight + floor * tileHeight
                        val cell = layer.getCell(col, row) ?: continue
                        val tile = cell.tile
                        if (tile != null)
                        {
                            val flipX = cell.flipHorizontally
                            val flipY = cell.flipVertically
                            val rotations = cell.rotation
                            val region = tile.textureRegion
                            val x1 : Float = x + tile.offsetX * unitScale
                            val y1 : Float = y + tile.offsetY * unitScale
                            val x2 : Float = x1 + region.regionWidth * unitScale
                            val y2 : Float = y1 + region.regionHeight * unitScale
                            val u1 = region.u
                            val v1 = region.v2
                            val u2 = region.u2
                            val v2 = region.v
                            vertices[Batch.X1] = x1
                            vertices[Batch.Y1] = y1
                            vertices[Batch.C1] = color
                            vertices[Batch.U1] = u1
                            vertices[Batch.V1] = v1
                            vertices[Batch.X2] = x1
                            vertices[Batch.Y2] = y2
                            vertices[Batch.C2] = color
                            vertices[Batch.U2] = u1
                            vertices[Batch.V2] = v2
                            vertices[Batch.X3] = x2
                            vertices[Batch.Y3] = y2
                            vertices[Batch.C3] = color
                            vertices[Batch.U3] = u2
                            vertices[Batch.V3] = v2
                            vertices[Batch.X4] = x2
                            vertices[Batch.Y4] = y1
                            vertices[Batch.C4] = color
                            vertices[Batch.U4] = u2
                            vertices[Batch.V4] = v1
                            if (flipX)
                            {
                                var temp : Float = vertices[Batch.U1]
                                vertices[Batch.U1] = vertices[Batch.U3]
                                vertices[Batch.U3] = temp
                                temp = vertices[Batch.U2]
                                vertices[Batch.U2] = vertices[Batch.U4]
                                vertices[Batch.U4] = temp
                            }
                            if (flipY)
                            {
                                var temp : Float = vertices[Batch.V1]
                                vertices[Batch.V1] = vertices[Batch.V3]
                                vertices[Batch.V3] = temp
                                temp = vertices[Batch.V2]
                                vertices[Batch.V2] = vertices[Batch.V4]
                                vertices[Batch.V4] = temp
                            }
                            if (rotations != 0)
                            {
                                when (rotations)
                                {
                                    TiledMapTileLayer.Cell.ROTATE_90  ->
                                    {
                                        val tempV = vertices[Batch.V1]
                                        vertices[Batch.V1] = vertices[Batch.V2]
                                        vertices[Batch.V2] = vertices[Batch.V3]
                                        vertices[Batch.V3] = vertices[Batch.V4]
                                        vertices[Batch.V4] = tempV
                                        val tempU = vertices[Batch.U1]
                                        vertices[Batch.U1] = vertices[Batch.U2]
                                        vertices[Batch.U2] = vertices[Batch.U3]
                                        vertices[Batch.U3] = vertices[Batch.U4]
                                        vertices[Batch.U4] = tempU
                                    }
                                    TiledMapTileLayer.Cell.ROTATE_180 ->
                                    {
                                        var tempU = vertices[Batch.U1]
                                        vertices[Batch.U1] = vertices[Batch.U3]
                                        vertices[Batch.U3] = tempU
                                        tempU = vertices[Batch.U2]
                                        vertices[Batch.U2] = vertices[Batch.U4]
                                        vertices[Batch.U4] = tempU
                                        var tempV = vertices[Batch.V1]
                                        vertices[Batch.V1] = vertices[Batch.V3]
                                        vertices[Batch.V3] = tempV
                                        tempV = vertices[Batch.V2]
                                        vertices[Batch.V2] = vertices[Batch.V4]
                                        vertices[Batch.V4] = tempV
                                    }
                                    TiledMapTileLayer.Cell.ROTATE_270 ->
                                    {
                                        val tempV = vertices[Batch.V1]
                                        vertices[Batch.V1] = vertices[Batch.V4]
                                        vertices[Batch.V4] = vertices[Batch.V3]
                                        vertices[Batch.V3] = vertices[Batch.V2]
                                        vertices[Batch.V2] = tempV
                                        val tempU = vertices[Batch.U1]
                                        vertices[Batch.U1] = vertices[Batch.U4]
                                        vertices[Batch.U4] = vertices[Batch.U3]
                                        vertices[Batch.U3] = vertices[Batch.U2]
                                        vertices[Batch.U2] = tempU
                                    }
                                }
                            }
                            batch.draw(region.texture, vertices, 0, NUM_VERTICES)
                        }
                    }
                }
            }
        }

        batch.draw(entity, 32f * 3 + 16f, 16f * 1 + 8f)

        endRender()
    }
}
