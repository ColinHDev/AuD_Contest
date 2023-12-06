package com.gatdsen.manager.player;

import com.gatdsen.manager.Controller;
import com.gatdsen.manager.StaticGameState;
import com.gatdsen.simulation.PathTile;
import com.gatdsen.simulation.Tile;
import com.gatdsen.simulation.Tower;

import java.util.Random;

public class TestBot extends Bot{
    @Override
    public String getName() {
        return "ChristmasBot";
    }

    @Override
    public int getMatrikel() {
        return 691337;
    }

    @Override
    public String getStudentName() {
        return "Santa Claus";
    }

    @Override
    public void init(StaticGameState state) {
        System.out.println("TestBot init");
    }

    @Override
    public void executeTurn(StaticGameState state, Controller controller) {
        System.out.println("Turn: " + state.getTurn());
        Tile[][] board = state.getMyPlayerState().getBoard();
        for (int i = 0; i < state.getBoardSizeX(); i++) {
            for (int j = 0; j < state.getBoardSizeY(); j++) {
                if (board[i][j] instanceof PathTile) {
                    controller.placeTower(i, j, Tower.TowerType.BASIC_TOWER);
                    System.out.println("TestBot tried to place tower at path " + i + ", " + j);
                } else if (board[i][j] instanceof Tower) {
                    System.out.println("TestBot found tower at " + i + ", " + j);
                    controller.upgradeTower(i,j);
                    System.out.println("TestBot tried to upgrade tower at " + i + ", " + j);
                }
                if (board[i + 1][j + 1] instanceof PathTile) {
                    controller.placeTower(i, j, Tower.TowerType.BASIC_TOWER);
                    System.out.println("TestBot tried to place tower near path at " + (i+1) + ", " + (j+1));
                }
            }
        }
        System.out.println("TestBot finished turn\n");
    }
}
