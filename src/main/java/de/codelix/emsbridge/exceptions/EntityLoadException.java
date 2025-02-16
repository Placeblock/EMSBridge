package de.codelix.emsbridge.exceptions;

import lombok.Getter;

@Getter
public class EntityLoadException extends RuntimeException {
    private final int entityId;
    public EntityLoadException(int entityId) {
        super("Could not load entity with entity id " + entityId);
        this.entityId = entityId;
    }
    public EntityLoadException(int entityId, Throwable cause) {
        super("Could not load entity with entity id" + entityId, cause);
        this.entityId = entityId;
    }
}
