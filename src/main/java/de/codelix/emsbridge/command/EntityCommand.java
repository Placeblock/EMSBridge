package de.codelix.emsbridge.command;

import de.codelix.commandapi.paper.DefaultPaperCommand;
import de.codelix.emsbridge.EntityMap;
import de.codelix.emsbridge.command.exceptions.PlayerNotRegisteredException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EntityCommand extends DefaultPaperCommand<EntitySource, EntityPlayer> {
    private final EntityMap entityMap;

    @Override
    protected EntitySource createSource(EntityPlayer entityPlayer, CommandSender commandSender) {
        return null;
    }

    @Override
    protected EntityPlayer getPlayer(Player player) {
        Integer entityId = entityMap.getEntityId(player.getUniqueId());
        if (entityId == null) {
            throw new PlayerNotRegisteredException();
        }
        return null;
    }
}
