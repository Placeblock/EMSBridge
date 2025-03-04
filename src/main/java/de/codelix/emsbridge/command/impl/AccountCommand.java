package de.codelix.emsbridge.command.impl;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.paper.DefaultPaperSource;
import de.codelix.commandapi.paper.PlayerPaperCommand;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperLiteralBuilder;
import de.codelix.emsbridge.command.parameters.EntityNameParameter;
import de.codelix.emsbridge.gui.NameInputGUI;
import de.codelix.emsbridge.messages.Messages;
import de.codelix.emsbridge.messages.Texts;
import de.codelix.emsbridge.service.EntityService;
import de.codelix.entitymanagementsystem.models.Token;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
            .runPlayer(ep -> Bukkit.getScheduler().runTask(this.getPlugin(), () -> new NameInputGUI(this.getPlugin(),
                    Texts.text("Enter new Name"),
                    ep.getPlayer(), name -> {
                        try {
                            EntityNameParameter.validateName(name);
                        } catch (ParseException e) {
                            ep.sendMessage(this.getDesign().getMessages().getMessage(e));
                            ep.sendMessage(Texts.text("<click:run_command:/account rename><b><color:green>[TRY AGAIN]</color></b></click>"));
                            return;
                        }
                        Bukkit.getScheduler().runTaskAsynchronously(this.getPlugin(), () ->
                            this.rename(ep, name));
                        ep.closeInventory();
                    }
                ).showPlayer(ep))
            )
        )
        .then(this.factory().literal("link")
            .description("Links another account to your Nostalgicraft Account (For example Discord)")
            .runPlayer(p -> {
                Integer entityId = this.entityService.getEntityIdNullableLocal(p.getUniqueId());
                Token token;
                try {
                    token = this.entityService.requestToken(entityId);
                } catch (RuntimeException e) {
                    p.sendMessage(Messages.ERROR_CREATE_PIN);
                    throw e;
                }
                p.sendMessage(Messages.PIN_MESSAGE(token.getPin()));
            })
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
