package de.codelix.emsbridge.command;

import de.codelix.entitymanagementsystem.models.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public class EntityPlayer {
    private final Entity entity;
    private final Player player;
}
