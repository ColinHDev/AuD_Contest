package com.gatdsen.networking.data;

import com.gatdsen.simulation.GameState;

public record GameInformation(GameState state, boolean isDebug, long seed) implements CommunicatedInformation {
}
