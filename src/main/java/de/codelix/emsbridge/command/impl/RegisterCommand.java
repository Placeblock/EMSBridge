package de.codelix.emsbridge.command.impl;

import de.codelix.commandapi.core.parameter.impl.WordParameter;
import de.codelix.commandapi.paper.DefaultPaperSource;
import de.codelix.commandapi.paper.PlayerPaperCommand;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperLiteralBuilder;
import de.codelix.emsbridge.command.messages.EMSDesign;
import de.codelix.emsbridge.util.Util;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public class RegisterCommand extends PlayerPaperCommand {
    private final JavaPlugin plugin;

    public RegisterCommand(JavaPlugin plugin) {
        super(plugin, "register", false, new EMSDesign<>());
        this.plugin = plugin;
    }

    @Override
    public void build(DefaultPaperLiteralBuilder<DefaultPaperSource, Player> builder) {
        builder
        .then(this.factory().argument("name", new WordParameter<>())
            .runPlayer((Player p, String name) -> {

            })
        )
        .runPlayer(p -> {
            AnvilGUI gui = new AnvilGUI.Builder()
                    .title("Enter Project Name")
                    .preventClose()
                    .text("Your Name")
                    .plugin(this.plugin)
                    .onClick((slot, stateSnapshot) -> {
                        if (slot != AnvilGUI.Slot.OUTPUT) {
                            return Collections.emptyList();
                        }
                        String name = stateSnapshot.getText();
                        try {
                            Util.validateEntityName(name);
                            return List.of(AnvilGUI.ResponseAction.close());
                        } catch (IllegalArgumentException _) {
                            return Collections.emptyList();
                        }
                    })
                    .open(p);
            }
        );
    }
}
