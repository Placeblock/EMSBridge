package de.codelix.emsbridge.exceptions;

public class PlayerUuidLoadException extends RuntimeException {
    private final int entityId;
    public PlayerUuidLoadException(int entityId) {
        super("Could not load player uuid for entity with id" + entityId);
        this.entityId = entityId;
    }
    public PlayerUuidLoadException(int entityId, Throwable cause) {
        super("Could not load player uuid for entity with id" + entityId, cause);
        this.entityId = entityId;
    }
}
