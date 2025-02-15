package de.codelix.emsbridge.command.impl;

import de.codelix.commandapi.paper.DefaultPaperSource;
import de.codelix.commandapi.paper.PlayerPaperCommand;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperLiteralBuilder;
import de.codelix.emsbridge.command.messages.EMSDesign;
import de.codelix.emsbridge.command.parameters.EntityNameParameter;
import de.codelix.emsbridge.exceptions.EntityLoadException;
import de.codelix.emsbridge.exceptions.PlayerAlreadyRegisteredException;
import de.codelix.emsbridge.gui.NameInputGUI;
import de.codelix.emsbridge.messages.Messages;
import de.codelix.emsbridge.service.EntityService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class RegisterCommand extends PlayerPaperCommand {
    private final JavaPlugin plugin;
    private final EntityService entityService;

    public RegisterCommand(JavaPlugin plugin, EntityService entityService) {
        super(plugin, "register", true, new EMSDesign<>());
        this.plugin = plugin;
        this.entityService = entityService;
    }

    @Override
    public void build(DefaultPaperLiteralBuilder<DefaultPaperSource, Player> builder) {
        builder
            .description("Creates a new Nostalgicraft Account")
            .then(this.factory().argument("name", new EntityNameParameter<>())
                    .runPlayer((Player p, String name) -> this.register(p, name))
            )
            .runPlayer(p -> new NameInputGUI(this.plugin, p, "register", name -> this.register(p, name)).show());
    }

    private void register(Player p, String name) {
        try {
            this.entityService.createEntity(p.getUniqueId(), name);
            p.sendMessage(Messages.ENTITY_CREATED(name));
        } catch (EntityLoadException e) {
            p.sendMessage(Messages.ERROR_CHECK_REGISTERED(name));
            throw e;
        } catch (PlayerAlreadyRegisteredException e) {
            p.sendMessage(Messages.ERROR_ALREADY_REGISTERED);
        } catch (RuntimeException e) {
            p.sendMessage(Messages.ERROR_CREATE_ENTITY);
            throw e;
        }
    }
}
