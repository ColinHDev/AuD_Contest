package com.example.networking.data;

import com.example.simulation.GameState;

public record GameInformation(GameState state, boolean isDebug, long seed) implements CommunicatedInformation {
}
