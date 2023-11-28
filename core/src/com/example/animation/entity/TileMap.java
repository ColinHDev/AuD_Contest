package com.example.animation.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.example.simulation.GameState;
import com.example.simulation.IntVector2;
import com.example.simulation.PathTile;
import com.example.simulation.Tile;
import com.example.ui.assets.AssetContainer;

public class TileMap extends Entity {

    public static final int TILE_TYPE_NONE = -1;
    private static final int NUM_NON_PATH_TILES = 2;

    private static final int UP = 1;
    private static final int RIGHT = 2;
    private static final int DOWN = 3;
    private static final int LEFT = 4;

    /*
     * indices:
     * 0 - null
     * 1 - up
     * 2 - right
     * 3 - down
     * 4 - left
     *
     * kombination der indices als start und ende des Pfads
     * -100 bedeutet kein Pfad -> error
     */
    private static final int[][] pathTypes = new int[][]{
            {-100, 0, 1, 2, 3}, //Spawns
            {4, -100, 5, 6, 7}, //Start von oben
            {8, 5, -100, 9, 10}, //Start von rechts
            {11, 6, 9, -100, 12}, //Start von unten
            {13, 7, 10, 12, -100} //Start von links
    };
    private int[][] tiles;
    private int sizeX;
    private int sizeY;

    private int tileSize = 12;

    public TileMap(GameState state, int player) {
        sizeX = state.getBoardSizeX();
        sizeY = state.getBoardSizeY();
        if (AssetContainer.IngameAssets.tileTextures.length > 0)
            tileSize = AssetContainer.IngameAssets.tileTextures[0].getRegionWidth();
        this.tiles = new int[sizeX][sizeY];
        Tile[][] board = state.getPlayerBoard(player);
        GameState.MapTileType[][] map = state.getMap();
        for (int i = 0; i < sizeX; i++)
            for (int j = 0; j < sizeY; j++) {
                if (map[i][j].ordinal() >= 2) tiles[i][j] = idPathTile(board[i][j]);
                else tiles[i][j] = map[i][j].ordinal();
            }
    }

    private int idPathTile(Tile tile) {
        PathTile cur = (PathTile) tile;
        if (cur == null) return TILE_TYPE_NONE;
        int from;
        int to;

        Tile prev = cur.getPrev();
        from = getRelPos(cur, prev);


        Tile next = cur.getNext();
        to = getRelPos(cur, next);

        return pathTypes[from][to];
    }

    private int getRelPos(PathTile cur, Tile other) {
        int pos;
        if (other == null) pos = 0;
        else {
            // Position von other relativ zu cur bestimmen
            IntVector2 difNext = other.getPosition().sub(cur.getPosition());
            switch (difNext.x) {
                case 0:
                    if (difNext.y == 1) pos = UP;
                    else pos = DOWN;
                    break;
                case 1:
                    pos = RIGHT;
                    break;
                case -1:
                    pos = LEFT;
                    break;
                default:
                    pos = 0;
            }
        }
        return pos + NUM_NON_PATH_TILES;
    }


    @Override
    public void draw(Batch batch, float deltaTime, float parentAlpha) {
        super.draw(batch, deltaTime, parentAlpha);
        Vector2 pos = new Vector2(0, 0);
        //Vector2 pos = new Vector2(sizeX/2f * tileSize, sizeY/2f * tileSize).add(getPos());
        for (int i = 0; i < sizeX; i++)
            for (int j = 0; j < sizeY; j++) {
                int type = tiles[i][j];
                if (type != TILE_TYPE_NONE) {
                    batch.draw(AssetContainer.IngameAssets.tileTextures[type], pos.x + i * tileSize, pos.y + j * tileSize);
                }
            }
    }


    public int getTile(IntVector2 pos) {
        return getTile(pos.x, pos.y);
    }

    public int getTile(int x, int y) {
        return tiles[x][y];
    }

    public void setTile(IntVector2 pos, int value) {
        setTile(pos.x, pos.y, value);
    }

    public void setTile(int x, int y, int value) {
        tiles[x][y] = value;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
}
