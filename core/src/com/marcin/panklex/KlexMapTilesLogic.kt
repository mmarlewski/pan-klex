package com.marcin.panklex

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.entities.*

class KlexMapTilesLogic(val tiles: KlexTiles, val map: KlexMap, val level: KlexLevel)
{
    fun setUpMap(currentLevel: Int)
    {
        for (i in 0..level.width - 1)
        {
            for (j in 0..level.height - 1)
            {
                val blockType =
                        if (level.map[currentLevel][j][i].rock != Block.Empty) level.map[currentLevel][j][i].rock else Block.Metal
                val upBlockType = if (j < level.height - 1) level.map[currentLevel][j + 1][i].rock else Block.Metal
                val aboveBlockType = if (currentLevel < level.levels - 1) level.map[currentLevel + 1][j][i].rock else Block.Metal

                val isRock = level.map[currentLevel][j][i].rock != Block.Empty
                val isUpLeftRock = !(j < level.height - 1 && i > 0 && level.map[currentLevel][j + 1][i - 1].rock == Block.Empty)
                val isUpRock = !(j < level.height - 1 && level.map[currentLevel][j + 1][i].rock == Block.Empty)
                val isUpRightRock =
                        !(j < level.height - 1 && i < level.width - 1 && level.map[currentLevel][j + 1][i + 1].rock == Block.Empty)
                val isRightRock = !(i < level.width - 1 && level.map[currentLevel][j][i + 1].rock == Block.Empty)
                val isDownRightRock = !(j > 0 && i < level.width - 1 && level.map[currentLevel][j - 1][i + 1].rock == Block.Empty)
                val isDownRock = !(j > 0 && level.map[currentLevel][j - 1][i].rock == Block.Empty)
                val isDownLeftRock = !(j > 0 && i > 0 && level.map[currentLevel][j - 1][i - 1].rock == Block.Empty)
                val isLeftRock = !(i > 0 && level.map[currentLevel][j][i - 1].rock == Block.Empty)

                val isAboveRock = !(currentLevel < level.levels - 1 && level.map[currentLevel + 1][j][i].rock == Block.Empty)
                val isAboveUpLeftRock =
                        !(currentLevel < level.levels - 1 && j < level.height - 1 && i > 0 && level.map[currentLevel + 1][j + 1][i - 1].rock == Block.Empty)
                val isAboveUpRock =
                        !(currentLevel < level.levels - 1 && j < level.height - 1 && level.map[currentLevel + 1][j + 1][i].rock == Block.Empty)
                val isAboveUpRightRock =
                        !(currentLevel < level.levels - 1 && j < level.height - 1 && i < level.width - 1 && level.map[currentLevel + 1][j + 1][i + 1].rock == Block.Empty)
                val isAboveRightRock =
                        !(currentLevel < level.levels - 1 && i < level.width - 1 && level.map[currentLevel + 1][j][i + 1].rock == Block.Empty)
                val isAboveDownRightRock =
                        !(currentLevel < level.levels - 1 && j > 0 && i < level.width - 1 && level.map[currentLevel + 1][j - 1][i + 1].rock == Block.Empty)
                val isAboveDownRock =
                        !(currentLevel < level.levels - 1 && j > 0 && level.map[currentLevel + 1][j - 1][i].rock == Block.Empty)
                val isAboveDownLeftRock =
                        !(currentLevel < level.levels - 1 && j > 0 && i > 0 && level.map[currentLevel + 1][j - 1][i - 1].rock == Block.Empty)
                val isAboveLeftRock =
                        !(currentLevel < level.levels - 1 && i > 0 && level.map[currentLevel + 1][j][i - 1].rock == Block.Empty)

                val holeTile = when
                {
                    isRock                          -> tiles.emptyTile
                    !isUpRock                       -> when (upBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneHole_none
                        Block.DamagedStone   -> tiles.damStoneHole_none
                        Block.Brick          -> tiles.brickHole_none
                        Block.Metal          -> tiles.metalHole_none
                        else                 -> tiles.exampleHole_none
                    }
                    !isUpLeftRock && !isUpRightRock -> when (upBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneHole_l_m_r
                        Block.DamagedStone   -> tiles.damStoneHole_l_m_r
                        Block.Brick          -> tiles.brickHole_l_m_r
                        Block.Metal          -> tiles.metalHole_l_m_r
                        else                 -> tiles.exampleHole_l_m_r
                    }
                    !isUpLeftRock && isUpRightRock  -> when (upBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneHole_l_m
                        Block.DamagedStone   -> tiles.damStoneHole_l_m
                        Block.Brick          -> tiles.brickHole_l_m
                        Block.Metal          -> tiles.metalHole_l_m
                        else                 -> tiles.exampleHole_l_m
                    }
                    isUpLeftRock && isUpRightRock   -> when (upBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneHole_m
                        Block.DamagedStone   -> tiles.damStoneHole_m
                        Block.Brick          -> tiles.brickHole_m
                        Block.Metal          -> tiles.metalHole_m
                        else                 -> tiles.exampleHole_m
                    }
                    isUpLeftRock && !isUpRightRock  -> when (upBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneHole_m_r
                        Block.DamagedStone   -> tiles.damStoneHole_m_r
                        Block.Brick          -> tiles.brickHole_m_r
                        Block.Metal          -> tiles.metalHole_m_r
                        else                 -> tiles.exampleHole_m_r
                    }
                    else                            -> tiles.emptyTile
                }

                val ul = !isUpLeftRock || (isUpLeftRock && isAboveUpLeftRock)
                val u = !isUpRock || (isUpRock && isAboveUpRock)
                val ur = !isUpRightRock || (isUpRightRock && isAboveUpRightRock)
                val r = !isRightRock || (isRightRock && isAboveRightRock)
                val dr = !isDownRightRock || (isDownRightRock && isAboveDownRightRock)
                val d = !isDownRock || (isDownRock && isAboveDownRock)
                val dl = !isDownLeftRock || (isDownLeftRock && isAboveDownLeftRock)
                val l = !isLeftRock || (isLeftRock && isAboveLeftRock)

                val baseTile = when
                {
                    !isRock                                         -> tiles.emptyTile
                    ul && !u && !ur && !r && !dr && !d && !dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul
                        Block.DamagedStone   -> tiles.damStoneBase_ul
                        Block.Brick          -> tiles.brickBase_ul
                        Block.Metal          -> tiles.metalBase_ul
                        else                 -> tiles.exampleBase_ul
                    }
                    !ul && !u && ur && !r && !dr && !d && !dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur
                        Block.DamagedStone   -> tiles.damStoneBase_ur
                        Block.Brick          -> tiles.brickBase_ur
                        Block.Metal          -> tiles.metalBase_ur
                        else                 -> tiles.exampleBase_ur
                    }
                    !ul && !u && !ur && !r && dr && !d && !dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_dr
                        Block.DamagedStone   -> tiles.damStoneBase_dr
                        Block.Brick          -> tiles.brickBase_dr
                        Block.Metal          -> tiles.metalBase_dr
                        else                 -> tiles.exampleBase_dr
                    }
                    !ul && !u && !ur && !r && !dr && !d && dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_dl
                        Block.DamagedStone   -> tiles.damStoneBase_dl
                        Block.Brick          -> tiles.brickBase_dl
                        Block.Metal          -> tiles.metalBase_dl
                        else                 -> tiles.exampleBase_dl
                    }
                    ul && !u && ur && !r && !dr && !d && !dl && !l  -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur
                        Block.DamagedStone   -> tiles.damStoneBase_ul_ur
                        Block.Brick          -> tiles.brickBase_ul_ur
                        Block.Metal          -> tiles.metalBase_ul_ur
                        else                 -> tiles.exampleBase_ul_ur
                    }
                    !ul && !u && ur && !r && dr && !d && !dl && !l  -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur_dr
                        Block.DamagedStone   -> tiles.damStoneBase_ur_dr
                        Block.Brick          -> tiles.brickBase_ur_dr
                        Block.Metal          -> tiles.metalBase_ur_dr
                        else                 -> tiles.exampleBase_ur_dr
                    }
                    !ul && !u && !ur && !r && dr && !d && dl && !l  -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_dr_dl
                        Block.DamagedStone   -> tiles.damStoneBase_dr_dl
                        Block.Brick          -> tiles.brickBase_dr_dl
                        Block.Metal          -> tiles.metalBase_dr_dl
                        else                 -> tiles.exampleBase_dr_dl
                    }
                    ul && !u && !ur && !r && !dr && !d && dl && !l  -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ul_dl
                        Block.Brick          -> tiles.brickBase_ul_dl
                        Block.Metal          -> tiles.metalBase_ul_dl
                        else                 -> tiles.exampleBase_ul_dl
                    }
                    ul && !u && ur && !r && dr && !d && !dl && !l   -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_dr
                        Block.DamagedStone   -> tiles.damStoneBase_ul_ur_dr
                        Block.Brick          -> tiles.brickBase_ul_ur_dr
                        Block.Metal          -> tiles.metalBase_ul_ur_dr
                        else                 -> tiles.exampleBase_ul_ur_dr
                    }
                    !ul && !u && ur && !r && dr && !d && dl && !l   -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur_dr_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ur_dr_dl
                        Block.Brick          -> tiles.brickBase_ur_dr_dl
                        Block.Metal          -> tiles.metalBase_ur_dr_dl
                        else                 -> tiles.exampleBase_ur_dr_dl
                    }
                    ul && !u && !ur && !r && dr && !d && dl && !l   -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_dr_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ul_dr_dl
                        Block.Brick          -> tiles.brickBase_ul_dr_dl
                        Block.Metal          -> tiles.metalBase_ul_dr_dl
                        else                 -> tiles.exampleBase_ul_dr_dl
                    }
                    ul && !u && ur && !r && !dr && !d && dl && !l   -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ul_ur_dl
                        Block.Brick          -> tiles.brickBase_ul_ur_dl
                        Block.Metal          -> tiles.metalBase_ul_ur_dl
                        else                 -> tiles.exampleBase_ul_ur_dl
                    }
                    u && r && !d && !dl && !l                       -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_r_dr
                        Block.DamagedStone   -> tiles.damStoneBase_ul_u_ur_r_dr
                        Block.Brick          -> tiles.brickBase_ul_u_ur_r_dr
                        Block.Metal          -> tiles.metalBase_ul_u_ur_r_dr
                        else                 -> tiles.exampleBase_ul_u_ur_r_dr
                    }
                    !ul && !u && r && d && !l                       -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur_r_dr_d_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ur_r_dr_d_dl
                        Block.Brick          -> tiles.brickBase_ur_r_dr_d_dl
                        Block.Metal          -> tiles.metalBase_ur_r_dr_d_dl
                        else                 -> tiles.exampleBase_ur_r_dr_d_dl
                    }
                    !u && !ur && !r && d && l                       -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_dr_d_dl_l
                        Block.DamagedStone   -> tiles.damStoneBase_ul_dr_d_dl_l
                        Block.Brick          -> tiles.brickBase_ul_dr_d_dl_l
                        Block.Metal          -> tiles.metalBase_ul_dr_d_dl_l
                        else                 -> tiles.exampleBase_ul_dr_d_dl_l
                    }
                    u && !r && !dr && !d && l                       -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_dl_l
                        Block.DamagedStone   -> tiles.damStoneBase_ul_u_ur_dl_l
                        Block.Brick          -> tiles.brickBase_ul_u_ur_dl_l
                        Block.Metal          -> tiles.metalBase_ul_u_ur_dl_l
                        else                 -> tiles.exampleBase_ul_u_ur_dl_l
                    }
                    u && r && !d && dl && !l                        -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_r_dr_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ul_u_ur_r_dr_dl
                        Block.Brick          -> tiles.brickBase_ul_u_ur_r_dr_dl
                        Block.Metal          -> tiles.metalBase_ul_u_ur_r_dr_dl
                        else                 -> tiles.exampleBase_ul_u_ur_r_dr_dl
                    }
                    ul && !u && r && d && !l                        -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_r_dr_d_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ul_ur_r_dr_d_dl
                        Block.Brick          -> tiles.brickBase_ul_ur_r_dr_d_dl
                        Block.Metal          -> tiles.metalBase_ul_ur_r_dr_d_dl
                        else                 -> tiles.exampleBase_ul_ur_r_dr_d_dl
                    }
                    !u && ur && !r && d && l                        -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_dr_d_dl_l
                        Block.DamagedStone   -> tiles.damStoneBase_ul_ur_dr_d_dl_l
                        Block.Brick          -> tiles.brickBase_ul_ur_dr_d_dl_l
                        Block.Metal          -> tiles.metalBase_ul_ur_dr_d_dl_l
                        else                 -> tiles.exampleBase_ul_ur_dr_d_dl_l
                    }
                    u && !r && dr && !d && l                        -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_dr_dl_l
                        Block.DamagedStone   -> tiles.damStoneBase_ul_u_ur_dr_dl_l
                        Block.Brick          -> tiles.brickBase_ul_u_ur_dr_dl_l
                        Block.Metal          -> tiles.metalBase_ul_u_ur_dr_dl_l
                        else                 -> tiles.exampleBase_ul_u_ur_dr_dl_l
                    }
                    u && r && d && !l                               -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_r_dr_d_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ul_u_ur_r_dr_d_dl
                        Block.Brick          -> tiles.brickBase_ul_u_ur_r_dr_d_dl
                        Block.Metal          -> tiles.metalBase_ul_u_ur_r_dr_d_dl
                        else                 -> tiles.exampleBase_ul_u_ur_r_dr_d_dl
                    }
                    !u && r && d && l                               -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_r_dr_d_dl_l
                        Block.DamagedStone   -> tiles.damStoneBase_ul_ur_r_dr_d_dl_l
                        Block.Brick          -> tiles.brickBase_ul_ur_r_dr_d_dl_l
                        Block.Metal          -> tiles.metalBase_ul_ur_r_dr_d_dl_l
                        else                 -> tiles.exampleBase_ul_ur_r_dr_d_dl_l
                    }
                    u && !r && d && l                               -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_dr_d_dl_l
                        Block.DamagedStone   -> tiles.damStoneBase_ul_u_ur_dr_d_dl_l
                        Block.Brick          -> tiles.brickBase_ul_u_ur_dr_d_dl_l
                        Block.Metal          -> tiles.metalBase_ul_u_ur_dr_d_dl_l
                        else                 -> tiles.exampleBase_ul_u_ur_dr_d_dl_l
                    }
                    u && r && !d && l                               -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_r_dr_dl_l
                        Block.DamagedStone   -> tiles.damStoneBase_ul_u_ur_r_dr_dl_l
                        Block.Brick          -> tiles.brickBase_ul_u_ur_r_dr_dl_l
                        Block.Metal          -> tiles.metalBase_ul_u_ur_r_dr_dl_l
                        else                 -> tiles.exampleBase_ul_u_ur_r_dr_dl_l
                    }
                    u && !r && !dr && !d && !dl && !l               -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur
                        Block.DamagedStone   -> tiles.damStoneBase_ul_u_ur
                        Block.Brick          -> tiles.brickBase_ul_u_ur
                        Block.Metal          -> tiles.metalBase_ul_u_ur
                        else                 -> tiles.exampleBase_ul_u_ur
                    }
                    !ul && !u && r && !d && !dl && !l               -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur_r_dr
                        Block.DamagedStone   -> tiles.damStoneBase_ur_r_dr
                        Block.Brick          -> tiles.brickBase_ur_r_dr
                        Block.Metal          -> tiles.metalBase_ur_r_dr
                        else                 -> tiles.exampleBase_ur_r_dr
                    }
                    !ul && !u && !ur && !r && d && !l               -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_dr_d_dl
                        Block.DamagedStone   -> tiles.damStoneBase_dr_d_dl
                        Block.Brick          -> tiles.brickBase_dr_d_dl
                        Block.Metal          -> tiles.metalBase_dr_d_dl
                        else                 -> tiles.exampleBase_dr_d_dl
                    }
                    !u && !ur && !r && !d && !dr && l               -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_dl_l
                        Block.DamagedStone   -> tiles.damStoneBase_ul_dl_l
                        Block.Brick          -> tiles.brickBase_ul_dl_l
                        Block.Metal          -> tiles.metalBase_ul_dl_l
                        else                 -> tiles.exampleBase_ul_dl_l
                    }
                    u && !r && !dr && !d && dl && !l                -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ul_u_ur_dl
                        Block.Brick          -> tiles.brickBase_ul_u_ur_dl
                        Block.Metal          -> tiles.metalBase_ul_u_ur_dl
                        else                 -> tiles.exampleBase_ul_u_ur_dl
                    }
                    ul && !u && r && !dl && !d && !l                -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_r_dr
                        Block.DamagedStone   -> tiles.damStoneBase_ul_ur_r_dr
                        Block.Brick          -> tiles.brickBase_ul_ur_r_dr
                        Block.Metal          -> tiles.metalBase_ul_ur_r_dr
                        else                 -> tiles.exampleBase_ul_ur_r_dr
                    }
                    !ul && !u && ur && !r && d && !l                -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur_dr_d_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ur_dr_d_dl
                        Block.Brick          -> tiles.brickBase_ur_dr_d_dl
                        Block.Metal          -> tiles.metalBase_ur_dr_d_dl
                        else                 -> tiles.exampleBase_ur_dr_d_dl
                    }
                    !u && !ur && !r && !d && dr && l                -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_dr_dl_l
                        Block.DamagedStone   -> tiles.damStoneBase_ul_dr_dl_l
                        Block.Brick          -> tiles.brickBase_ul_dr_dl_l
                        Block.Metal          -> tiles.metalBase_ul_dr_dl_l
                        else                 -> tiles.exampleBase_ul_dr_dl_l
                    }
                    u && !r && dr && !d && !dl && !l                -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_dr
                        Block.DamagedStone   -> tiles.damStoneBase_ul_u_ur_dr
                        Block.Brick          -> tiles.brickBase_ul_u_ur_dr
                        Block.Metal          -> tiles.metalBase_ul_u_ur_dr
                        else                 -> tiles.exampleBase_ul_u_ur_dr
                    }
                    !ul && !u && r && !d && dl && !l                -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur_r_dr_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ur_r_dr_dl
                        Block.Brick          -> tiles.brickBase_ur_r_dr_dl
                        Block.Metal          -> tiles.metalBase_ur_r_dr_dl
                        else                 -> tiles.exampleBase_ur_r_dr_dl
                    }
                    ul && !u && !ur && !r && d && !l                -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_dr_d_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ul_dr_d_dl
                        Block.Brick          -> tiles.brickBase_ul_dr_d_dl
                        Block.Metal          -> tiles.metalBase_ul_dr_d_dl
                        else                 -> tiles.exampleBase_ul_dr_d_dl
                    }
                    !u && ur && !r && !d && !dr && l                -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_dl_l
                        Block.DamagedStone   -> tiles.damStoneBase_ul_ur_dl_l
                        Block.Brick          -> tiles.brickBase_ul_ur_dl_l
                        Block.Metal          -> tiles.metalBase_ul_ur_dl_l
                        else                 -> tiles.exampleBase_ul_ur_dl_l
                    }
                    u && !r && dr && !d && dl && !l                 -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_dr_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ul_u_ur_dr_dl
                        Block.Brick          -> tiles.brickBase_ul_u_ur_dr_dl
                        Block.Metal          -> tiles.metalBase_ul_u_ur_dr_dl
                        else                 -> tiles.exampleBase_ul_u_ur_dr_dl
                    }
                    ul && !u && r && !d && dl && !l                 -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_r_dr_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ul_ur_r_dr_dl
                        Block.Brick          -> tiles.brickBase_ul_ur_r_dr_dl
                        Block.Metal          -> tiles.metalBase_ul_ur_r_dr_dl
                        else                 -> tiles.exampleBase_ul_ur_r_dr_dl
                    }
                    ul && !u && ur && !r && d && !l                 -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_dr_d_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ul_ur_dr_d_dl
                        Block.Brick          -> tiles.brickBase_ul_ur_dr_d_dl
                        Block.Metal          -> tiles.metalBase_ul_ur_dr_d_dl
                        else                 -> tiles.exampleBase_ul_ur_dr_d_dl
                    }
                    !u && ur && !r && !d && dr && l                 -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_dr_dl_l
                        Block.DamagedStone   -> tiles.damStoneBase_ul_ur_dr_dl_l
                        Block.Brick          -> tiles.brickBase_ul_ur_dr_dl_l
                        Block.Metal          -> tiles.metalBase_ul_ur_dr_dl_l
                        else                 -> tiles.exampleBase_ul_ur_dr_dl_l
                    }
                    u && !r && d && !l                              -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_dr_d_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ul_u_ur_dr_d_dl
                        Block.Brick          -> tiles.brickBase_ul_u_ur_dr_d_dl
                        Block.Metal          -> tiles.metalBase_ul_u_ur_dr_d_dl
                        else                 -> tiles.exampleBase_ul_u_ur_dr_d_dl
                    }
                    !u && r && !d && l                              -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_r_dr_dl_l
                        Block.DamagedStone   -> tiles.damStoneBase_ul_ur_r_dr_dl_l
                        Block.Brick          -> tiles.brickBase_ul_ur_r_dr_dl_l
                        Block.Metal          -> tiles.metalBase_ul_ur_r_dr_dl_l
                        else                 -> tiles.exampleBase_ul_ur_r_dr_dl_l
                    }
                    ul && !u && !ur && !r && dr && !d && !dl && !l  -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_dr
                        Block.DamagedStone   -> tiles.damStoneBase_ul_dr
                        Block.Brick          -> tiles.brickBase_ul_dr
                        Block.Metal          -> tiles.metalBase_ul_dr
                        else                 -> tiles.exampleBase_ul_dr
                    }
                    !ul && !u && ur && !r && !dr && !d && dl && !l  -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ur_dl
                        Block.Brick          -> tiles.brickBase_ur_dl
                        Block.Metal          -> tiles.metalBase_ur_dl
                        else                 -> tiles.exampleBase_ur_dl
                    }
                    ul && !u && ur && !r && dr && !d && dl && !l    -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_dr_dl
                        Block.DamagedStone   -> tiles.damStoneBase_ul_ur_dr_dl
                        Block.Brick          -> tiles.brickBase_ul_ur_dr_dl
                        Block.Metal          -> tiles.metalBase_ul_ur_dr_dl
                        else                 -> tiles.exampleBase_ul_ur_dr_dl
                    }
                    u && r && d && l                                -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_r_dr_d_dl_l
                        Block.DamagedStone   -> tiles.damStoneBase_ul_u_ur_r_dr_d_dl_l
                        Block.Brick          -> tiles.brickBase_ul_u_ur_r_dr_d_dl_l
                        Block.Metal          -> tiles.metalBase_ul_u_ur_r_dr_d_dl_l
                        else                 -> tiles.exampleBase_ul_u_ur_r_dr_d_dl_l
                    }
                    !u && !r && !d && !l                            -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_none
                        Block.DamagedStone   -> tiles.damStoneBase_none
                        Block.Brick          -> tiles.brickBase_none
                        Block.Metal          -> tiles.metalBase_none
                        else                 -> tiles.exampleBase_none
                    }
                    else                                            -> tiles.emptyTile
                }

                val blockTile = when
                {
                    isAboveRock -> tiles.cover_all
                    else        -> tiles.emptyTile
                }

                val wallTile = when
                {
                    !isAboveRock || isAboveDownRock       -> tiles.emptyTile
                    isAboveLeftRock && isAboveRightRock   -> when (aboveBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneWall_m
                        Block.DamagedStone   -> tiles.damStoneWall_m
                        Block.Brick          -> tiles.brickWall_m
                        Block.Metal          -> tiles.metalWall_m
                        else                 -> tiles.exampleWall_m
                    }
                    !isAboveLeftRock && isAboveRightRock  -> when (aboveBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneWall_l_m
                        Block.DamagedStone   -> tiles.damStoneWall_l_m
                        Block.Brick          -> tiles.brickWall_l_m
                        Block.Metal          -> tiles.metalWall_l_m
                        else                 -> tiles.exampleWall_l_m
                    }
                    isAboveLeftRock && !isAboveRightRock  -> when (aboveBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneWall_m_r
                        Block.DamagedStone   -> tiles.damStoneWall_m_r
                        Block.Brick          -> tiles.brickWall_m_r
                        Block.Metal          -> tiles.metalWall_m_r
                        else                 -> tiles.exampleWall_m_r
                    }
                    !isAboveLeftRock && !isAboveRightRock -> when (aboveBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneWall_l_m_r
                        Block.DamagedStone   -> tiles.damStoneWall_l_m_r
                        Block.Brick          -> tiles.brickWall_l_m_r
                        Block.Metal          -> tiles.metalWall_l_m_r
                        else                 -> tiles.exampleWall_l_m_r
                    }
                    else                                  -> tiles.emptyTile
                }

                val coverTile = when
                {
                    (isAboveDownRock || j == 0) -> tiles.cover_half
                    else                        -> tiles.emptyTile
                }

                val entity = level.map[currentLevel][j][i].entity
                val entityPosition = Vector3(i.toFloat(), j.toFloat(), currentLevel.toFloat())

                val entityTile = when (entity)
                {
                    null                    -> tiles.emptyTile
                    is EntityPlayer         -> tiles.player
                    is EntityContainer      -> tiles.container
                    is EntityVendingMachine -> tiles.vendingMachine
                    is EntityTeleporter     -> tiles.teleporter
                    is EntityFlag           -> tiles.flag
                    is EntityElevator       ->
                    {
                        if (entity.positions.contains(entityPosition)) tiles.elevator
                        else tiles.emptyTile
                    }
                    is EntityStairs         -> when
                    {
                        entityPosition.equals(entity.position) -> when (entity.direction)
                        {
                            Direction.Up    -> tiles.stairs_a_u
                            Direction.Right -> tiles.stairs_a_r
                            Direction.Down  -> tiles.stairs_a_d
                            Direction.Left  -> tiles.stairs_a_l
                            else            -> tiles.emptyTile
                        }

                        entityPosition.equals(entity.upperEnd) -> when (entity.direction)
                        {
                            Direction.Up    -> tiles.stairs_b_u
                            Direction.Right -> tiles.stairs_b_r
                            Direction.Down  -> tiles.stairs_b_d
                            Direction.Left  -> tiles.stairs_b_l
                            else            -> tiles.emptyTile
                        }
                        else                                   -> tiles.emptyTile
                    }
                    is EntityPoweredGate    -> when
                    {
                        entityPosition.equals(entity.gatePosition)       -> if (entity.isGateVertical)
                        {
                            if (entity.firstPartDirection == Direction.Down && entity.secondPartDirection == Direction.Up)
                            {
                                when
                                {
                                    !entity.isFirstPartPowered && !entity.isSecondPartPowered -> tiles.emptyTile
                                    entity.isFirstPartPowered && !entity.isSecondPartPowered  -> tiles.poweredGate_gate_ver_u
                                    !entity.isFirstPartPowered && entity.isSecondPartPowered  -> tiles.poweredGate_gate_ver_d
                                    entity.isFirstPartPowered && entity.isSecondPartPowered   -> tiles.poweredGate_gate_ver_u_d
                                    else                                                      -> tiles.emptyTile
                                }
                            }
                            else
                            {
                                when
                                {
                                    !entity.isFirstPartPowered && !entity.isSecondPartPowered -> tiles.emptyTile
                                    entity.isFirstPartPowered && !entity.isSecondPartPowered  -> tiles.poweredGate_gate_ver_d
                                    !entity.isFirstPartPowered && entity.isSecondPartPowered  -> tiles.poweredGate_gate_ver_u
                                    entity.isFirstPartPowered && entity.isSecondPartPowered   -> tiles.poweredGate_gate_ver_u_d
                                    else                                                      -> tiles.emptyTile
                                }
                            }
                        }
                        else
                        {
                            if (entity.firstPartDirection == Direction.Right && entity.secondPartDirection == Direction.Left)
                            {
                                when
                                {
                                    !entity.isFirstPartPowered && !entity.isSecondPartPowered -> tiles.emptyTile
                                    entity.isFirstPartPowered && !entity.isSecondPartPowered  -> tiles.poweredGate_gate_hor_l
                                    !entity.isFirstPartPowered && entity.isSecondPartPowered  -> tiles.poweredGate_gate_hor_r
                                    entity.isFirstPartPowered && entity.isSecondPartPowered   -> tiles.poweredGate_gate_hor_l_r
                                    else                                                      -> tiles.emptyTile
                                }
                            }
                            else
                            {
                                when
                                {
                                    !entity.isFirstPartPowered && !entity.isSecondPartPowered -> tiles.emptyTile
                                    entity.isFirstPartPowered && !entity.isSecondPartPowered  -> tiles.poweredGate_gate_hor_r
                                    !entity.isFirstPartPowered && entity.isSecondPartPowered  -> tiles.poweredGate_gate_hor_l
                                    entity.isFirstPartPowered && entity.isSecondPartPowered   -> tiles.poweredGate_gate_hor_l_r
                                    else                                                      -> tiles.emptyTile
                                }
                            }
                        }

                        entityPosition.equals(entity.firstPartPosition)  -> if (entity.isFirstPartPowered)
                        {
                            when (entity.firstPartDirection)
                            {
                                Direction.Up    -> tiles.poweredGate_part_pow_l_r
                                Direction.Right -> tiles.poweredGate_part_pow_r
                                Direction.Down  -> tiles.poweredGate_part_pow_l_r
                                Direction.Left  -> tiles.poweredGate_part_pow_l
                                else            -> tiles.emptyTile
                            }
                        }
                        else
                        {
                            when (entity.firstPartDirection)
                            {
                                Direction.Up    -> tiles.poweredGate_part_unp_l_r
                                Direction.Right -> tiles.poweredGate_part_unp_r
                                Direction.Down  -> tiles.poweredGate_part_unp_l_r
                                Direction.Left  -> tiles.poweredGate_part_unp_l
                                else            -> tiles.emptyTile
                            }
                        }
                        entityPosition.equals(entity.secondPartPosition) -> if (entity.isSecondPartPowered)
                        {
                            when (entity.secondPartDirection)
                            {
                                Direction.Up    -> tiles.poweredGate_part_pow_l_r
                                Direction.Right -> tiles.poweredGate_part_pow_r
                                Direction.Down  -> tiles.poweredGate_part_pow_l_r
                                Direction.Left  -> tiles.poweredGate_part_pow_l
                                else            -> tiles.emptyTile
                            }
                        }
                        else
                        {
                            when (entity.secondPartDirection)
                            {
                                Direction.Up    -> tiles.poweredGate_part_unp_l_r
                                Direction.Right -> tiles.poweredGate_part_unp_r
                                Direction.Down  -> tiles.poweredGate_part_unp_l_r
                                Direction.Left  -> tiles.poweredGate_part_unp_l
                                else            -> tiles.emptyTile
                            }
                        }
                        else                                             -> tiles.emptyTile
                    }
                    else                    -> tiles.emptyTile
                }

                map.setTile(MapLayer.Hole, i, j, holeTile)
                map.setTile(MapLayer.Base, i, j, baseTile)
                map.setTile(MapLayer.Block, i, j, blockTile)
                map.setTile(MapLayer.Wall, i, j, wallTile)
                map.setTile(MapLayer.Entity, i, j, entityTile)
                map.setTile(MapLayer.Cover, i, j, coverTile)
            }
        }

        map.setDimensions(level.width, level.height)
    }
}
