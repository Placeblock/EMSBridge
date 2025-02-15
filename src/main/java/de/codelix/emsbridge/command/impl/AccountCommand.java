package de.codelix.emsbridge.command.impl;

import de.codelix.commandapi.paper.DefaultPaperSource;
import de.codelix.commandapi.paper.PlayerPaperCommand;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperLiteralBuilder;
import de.codelix.emsbridge.command.parameters.EntityNameParameter;
import de.codelix.emsbridge.gui.NameInputGUI;
import de.codelix.emsbridge.messages.Messages;
import de.codelix.emsbridge.service.EntityService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class AccountCommand extends PlayerPaperCommand {
    private final EntityService entityService;

    public AccountCommand(Plugin plugin, EntityService service) {
        super(plugin, "account", true);
        this.entityService = service;
    }

    @Override
    public void build(DefaultPaperLiteralBuilder<DefaultPaperSource, Player> builder) {
        builder
        .then(this.factory().literal("rename")
            .description("Set a new name for your Nostalgicraft Account")
            .then(this.factory().argument("name", new EntityNameParameter<>())
                .runPlayer(this::rename)
            )
            .runPlayer(ep -> new NameInputGUI((JavaPlugin) this.getPlugin(),
                ep.getPlayer(), "account rename", name -> this.rename(ep, name))
                .show())
        );
    }

    private void rename(Player p, String name) {
        try {
            this.entityService.renameEntity(p.getUniqueId(), name);
        } catch (RuntimeException e) {
            p.sendMessage(Messages.ERROR_RENAME_ENTITY);
            throw e;
        }
    }
}
