package com.example.simulation;

import org.hamcrest.core.IsInstanceOf;

import java.io.Serializable;
import java.util.Arrays;

public class PlayerState implements Serializable {

    private final GameState gameState;
    private Tile[][] board;
    private int health;
    private int money;

    public int getHealth() {
        return health;
    }

    public PlayerState copy(GameState newGameState){
        return new PlayerState(this, newGameState);
    }

    /**
     * Creates a Deep-Copy of the player state
     */
    private PlayerState(PlayerState original, GameState gameState){
        this.gameState = gameState;
        board = new Tile[original.board.length][original.board[0].length];
        for (int i = 0; i < original.board.length; i++) {
            for (int j = 0; j < original.board[0].length; j++) {
                board[i][j] = original.board[i][j].copy();
            }
        }

        for (int i = 0; i < original.board.length; i++) {
            for (int j = 0; j < original.board.length; j++) {
                if (board[i][j] instanceof PathTile && ((PathTile) board[i][j]).next == null){
                    PathTile actual = (PathTile) board[i][j];
                    while (actual.prev!= null) {
                        actual.prev = (PathTile) board[((PathTile) original.board[actual.getPosition().x][actual.getPosition().y]).prev.getPosition().x][((PathTile) original.board[actual.getPosition().x][actual.getPosition().y]).prev.getPosition().y];
                        //TODO make it more simple
                        actual.prev.next = actual;
                        actual = actual.prev;
                    }
                        break;
                }
            }
        }
        health = original.health;
        money = original.money;

    }
    public PlayerState(GameState gameState, int health, int money){
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
