package de.codelix.emsbridge.command.impl;

import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperLiteralBuilder;
import de.codelix.emsbridge.command.EntityCommand;
import de.codelix.emsbridge.command.EntityPlayer;
import de.codelix.emsbridge.command.EntitySource;
import org.bukkit.plugin.Plugin;

public class VerifyCommand extends EntityCommand {
    public VerifyCommand(Plugin plugin) {
        super(plugin, "verify", true);
    }

    @Override
    public void build(DefaultPaperLiteralBuilder<EntitySource, EntityPlayer> builder) {

    }
}
