package com.example.networking.data;

import com.example.simulation.GameState;

public record GameInformation(GameState state, boolean isDebug) implements CommunicatedInformation {
}