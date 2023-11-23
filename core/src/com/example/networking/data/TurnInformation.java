package com.example.networking.data;

import com.example.simulation.GameState;

public record TurnInformation(GameState state) implements CommunicatedInformation {
    
}
