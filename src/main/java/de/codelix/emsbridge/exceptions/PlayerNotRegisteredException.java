package de.codelix.emsbridge.exceptions;

import java.util.UUID;

/**
 * Thrown for example when trying to unregister an unregistered player
 */
public class PlayerNotRegisteredException extends RuntimeException {
    public PlayerNotRegisteredException(UUID playerUuid) {
        super("The player " + playerUuid + " is not registered.");
    }
}
