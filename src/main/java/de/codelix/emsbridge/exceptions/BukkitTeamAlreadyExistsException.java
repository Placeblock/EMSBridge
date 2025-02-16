package de.codelix.emsbridge.exceptions;

public class BukkitTeamAlreadyExistsException extends RuntimeException {
  private final String name;

  public BukkitTeamAlreadyExistsException(String name) {
    super("Bukkit Team " + name + " already exists");
    this.name = name;
  }
}
