package de.codelix.emsbridge.exceptions;

import lombok.Getter;

@Getter
public class BukkitTeamNotExistsException extends RuntimeException {
    private final String name;

    public BukkitTeamNotExistsException(String name) {
        super("Team " + name + " has no bukkit equivalent team");
        this.name = name;
    }
}
