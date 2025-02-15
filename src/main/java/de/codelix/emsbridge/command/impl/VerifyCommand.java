package de.codelix.emsbridge.command.impl;

import de.codelix.commandapi.paper.DefaultPaperSource;
import de.codelix.commandapi.paper.PlayerPaperCommand;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperLiteralBuilder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class VerifyCommand extends PlayerPaperCommand {
    public VerifyCommand(Plugin plugin) {
        super(plugin, "verify", true);
    }

    @Override
    public void build(DefaultPaperLiteralBuilder<DefaultPaperSource, Player> builder) {

    }
}
