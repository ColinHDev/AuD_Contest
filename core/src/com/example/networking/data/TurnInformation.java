package com.example.networking.data;

import com.example.simulation.GameState;

public record TurnInformation(int turn, GameState gameState) implements CommunicatedInformation {
    
}
