package de.codelix.emsbridge.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class EntityIdLoadException extends RuntimeException {
    private final UUID playerUuid;
    public EntityIdLoadException(final UUID playerUuid) {
        super("Could not load entity id for player " + playerUuid);
        this.playerUuid = playerUuid;
    }
}
