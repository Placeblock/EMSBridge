package de.codelix.emsbridge.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PlayerAlreadyRegisteredException extends RuntimeException {
    private final UUID playerUuid;
    public PlayerAlreadyRegisteredException(UUID playerUuid) {
        super("Player " + playerUuid + " is already registered");
        this.playerUuid = playerUuid;
    }
}
