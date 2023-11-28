package com.example.simulation;

import java.io.Serializable;

public class PlayerState implements Serializable {

    private final GameState gameState;
    private Tile[][] board;

    private int health;
    private int money;
    private int enemyIndex;
    private int index;


    public int getHealth() {
        return health;
    }

    PlayerState copy(GameState newGameState){
        return new PlayerState(this, newGameState);
    }

    /**
     * Creates a Deep-Copy of the player state
     */
    private PlayerState(PlayerState original, GameState gameState){
        this.gameState = gameState;
        int boardX = gameState.getBoardSizeX();
        int boardY = gameState.getBoardSizeY();

        board = new Tile[boardX][boardY];
        for (int i = 0; i < boardX; i++) {
            for (int j = 0; j < boardY; j++) {
                if(original.board[i][j] != null){
                    board[i][j] = original.board[i][j].copy();
                }
            }
        }

        for (int i = 0; i < boardX; i++) {
            for (int j = 0; j < boardX; j++) {
                if (board[i][j] instanceof PathTile actual){
                    PathTile originalPT = (PathTile) original.board[i][j];
                    PathTile next = null;
                    if (originalPT.next != null){
                        IntVector2 nextPos = originalPT.next.getPosition();
                        next = (PathTile) board[nextPos.x][nextPos.y];
                    }
                    actual.setNext(next);

                }
            }
        }
        health = original.health;
        money = original.money;
    }


    public Tile[][] getMap(){
        return board;
    }

    private Tile[][] getEnemyMap(){
        return gameState.playerStates[enemyIndex].getMap();
    }


    public PlayerState(GameState gameState, int health, int money){
        this.index = index;
        this.enemyIndex = index == 0 ? 1 : 0;
        this.gameState = gameState;
        int width = gameState.getBoardSizeX();
        int height = gameState.getBoardSizeY();
        board = new Tile[width][height];
        this.health = health;
        this.money = money;

        for (int i = 0; i <width; i++) {
            for (int j = 0; j < height; j++) {
                if (gameState.map[i][j].ordinal() >= GameState.MapTileType.PATH_RIGHT.ordinal()){
                    board[i][j] = new PathTile(i, j);
                }
            }
        }

        IntRectangle mapOutline = new IntRectangle(0,0,width, height);
        for (int i = 0; i <width; i++) {
            for (int j = 0; j < height; j++) {
                if (gameState.map[i][j].ordinal() >= GameState.MapTileType.PATH_RIGHT.ordinal()){
                   IntVector2 destination = new IntVector2(i, j);
                   switch (gameState.map[i][j]){
                       case PATH_RIGHT -> destination.add(1,0);
                       case PATH_DOWN -> destination.add(0,-1);
                       case PATH_LEFT -> destination.add(-1,0);
                       case PATH_UP -> destination.add(0,1);
                   }
                   if (mapOutline.contains(destination.toFloat())){
                       ((PathTile) board[i][j]).setNext((PathTile) board[destination.x][destination.y]);
                   }
                }
            }
        }

    }




}
