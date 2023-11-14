package com.example.networking.data;

import com.example.simulation.PlayerState;

public record TurnInformation(PlayerState state) implements CommunicatedInformation {
    
}
