package com.gatdsen.networking.data;

import com.gatdsen.simulation.GameState;

public record TurnInformation(GameState state) implements CommunicatedInformation {
    
}
