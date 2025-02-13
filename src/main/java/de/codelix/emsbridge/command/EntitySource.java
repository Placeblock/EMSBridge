package de.codelix.emsbridge.command;

import de.codelix.commandapi.paper.PaperSource;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;

public class EntitySource extends PaperSource<EntityPlayer> {
    public EntitySource(EntityPlayer player, CommandSender console) {
        super(player, console);
    }

    @Override
    public boolean hasPermissionPlayer(String s) {
        return this.getPlayer().getPlayer().hasPermission(s);
    }

    @Override
    public void sendMessagePlayer(TextComponent textComponent) {
        this.getPlayer().getPlayer().sendMessage(textComponent);
    }
}
