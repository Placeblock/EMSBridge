package de.codelix.emsbridge.command;

import de.codelix.commandapi.minecraft.exception.InvalidPlayerException;
import de.codelix.commandapi.paper.DefaultPaperCommand;
import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.EntityMap;
import de.codelix.emsbridge.command.messages.EMSDesign;
import de.codelix.entitymanagementsystem.models.Entity;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class EntityCommand extends DefaultPaperCommand<EntitySource, EntityPlayer> {
    public EntityCommand(Plugin plugin, String label, boolean async) {
        super(plugin, label, async, new EMSDesign<>());
    }

    @Override
    protected EntitySource createSource(EntityPlayer entityPlayer, CommandSender commandSender) {
        return new EntitySource(entityPlayer, commandSender);
    }

    @Override
    protected EntityPlayer getPlayer(Player player) {
        EntityMap entityMap = EMSBridge.INSTANCE.getEntityMap();
        Integer entityId = entityMap.getEntityId(player.getUniqueId());
        if (entityId == null) {
            throw new InvalidPlayerException("");
        }
        Entity entity = entityMap.getEntity(entityId);
        return new EntityPlayer(entity, player);
    }
}
