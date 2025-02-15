package de.codelix.emsbridge.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class LinkNotCreatedException extends RuntimeException {
    private final UUID playerUuid;
    private final int entityId;
    public LinkNotCreatedException(UUID playerUuid, int entityId) {
        super("Could not create link for player " + playerUuid + " with entity " + entityId);
        this.playerUuid = playerUuid;
        this.entityId = entityId;
    }
}
