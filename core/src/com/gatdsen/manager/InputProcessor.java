package com.gatdsen.manager;

import com.gatdsen.manager.player.HumanPlayer;

public interface InputProcessor {


	void activateTurn(HumanPlayer currentPlayer);

	void endTurn();

}
