package de.codelix.emsbridge.exceptions;

import lombok.Getter;

@Getter
public class InvalidEntityIdException extends RuntimeException {
    private final int entityId;

    public InvalidEntityIdException(int entityId) {
        super("Tried to access data by using invalid entity id: " + entityId);
        this.entityId = entityId;
    }
}
