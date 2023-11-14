package com.example.networking.data;

import com.example.simulation.PlayerState;

public record GameInformation(PlayerState state, boolean isDebug) implements CommunicatedInformation {
}
