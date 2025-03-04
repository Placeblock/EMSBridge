package de.codelix.emsbridge.command.impl;

import de.codelix.commandapi.core.parameter.impl.GreedyParameter;
import de.codelix.commandapi.paper.DefaultPaperSource;
import de.codelix.commandapi.paper.PlayerPaperCommand;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperLiteralBuilder;
import de.codelix.emsbridge.command.exceptions.MessageTooLongException;
import de.codelix.emsbridge.command.messages.EMSDesign;
import de.codelix.emsbridge.messages.Messages;
import de.codelix.emsbridge.service.TeamService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TeamMsgCommand extends PlayerPaperCommand {
    private final TeamService teamService;

    public TeamMsgCommand(Plugin plugin, TeamService teamService) {
        super(plugin, "teammsg", true, new EMSDesign<>());
        this.teamService = teamService;
    }

    @Override
    public void build(DefaultPaperLiteralBuilder<DefaultPaperSource, Player> builder) {
        builder.then(this.factory().argument("message", new GreedyParameter<>())
            .runPlayer(this::sendMessage)
        );
    }

    private void sendMessage(Player player, String message) {
        System.out.println(message);
        message = message.trim();
        if (message.isEmpty()) {
            player.sendMessage(this.getDesign().getHelpMessage(this, new DefaultPaperSource(player, null)));
            return;
        }
        if (message.length() > 2000) {
            player.sendMessage(this.getDesign().getMessages().getMessage(new MessageTooLongException(2000)));
            return;
        }
        try {
            this.teamService.sendMessage(player.getUniqueId(), message);
        } catch (RuntimeException ex) {
            player.sendMessage(Messages.ERROR_SEND_TEAM_MESSAGE);
        }
    }
}
