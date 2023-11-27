package com.example.manager;

import com.example.manager.player.HumanPlayer;

public interface InputProcessor {


	void activateTurn(HumanPlayer currentPlayer);

	void endTurn();

}
