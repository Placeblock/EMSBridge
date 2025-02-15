package de.codelix.emsbridge.exceptions;

import java.util.UUID;

public class EntityLoadException extends RuntimeException {
    private final UUID playerUuid;
    public EntityLoadException(UUID playerUuid) {
        super("Could not load entity for Player with UUID " + playerUuid);
        this.playerUuid = playerUuid;
    }
    public EntityLoadException(UUID playerUuid, Throwable cause) {
        super("Could not load entity for Player with UUID" + playerUuid, cause);
        this.playerUuid = playerUuid;
    }
}
