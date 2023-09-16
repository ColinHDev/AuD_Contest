package com.example.simulation.action;


/**
 * Special type of {@link Action} indicating that the game has ended
 */
public class GameOverAction extends Action{

    private final int team;

    /**
     * Stores the event of the game ending
     * @param team  index of the winning team
     */
    public GameOverAction(int team) {
        super(0f);
        this.team = team;
    }

    /**
     * @return index of the winning team
     */
    public int getTeam() {
        return team;
    }
       @Override

    public String toString() {

        String output = "GameOver, Team: " + team;

        return output;

    }

}
