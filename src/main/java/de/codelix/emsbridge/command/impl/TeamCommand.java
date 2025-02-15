package de.codelix.emsbridge.command.impl;

import de.codelix.commandapi.paper.DefaultPaperSource;
import de.codelix.commandapi.paper.PlayerPaperCommand;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperLiteralBuilder;
import de.codelix.emsbridge.command.parameters.EntityInviteParameter;
import de.codelix.emsbridge.command.parameters.EntityNotTeamParameter;
import de.codelix.emsbridge.command.parameters.TeamNameParameter;
import de.codelix.entitymanagementsystem.models.Entity;
import de.codelix.entitymanagementsystem.models.MemberInvite;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TeamCommand extends PlayerPaperCommand {
    public TeamCommand(Plugin plugin) {
        super(plugin, "team", false);
    }

    @Override
    public void build(DefaultPaperLiteralBuilder<DefaultPaperSource, Player> builder) {
        builder
        .then(this.factory().literal("create")
            .then(this.factory().argument("name", new TeamNameParameter())
                .runPlayer((Player p, String name) -> {

                })
            )
        )
        .then(this.factory().literal("rename")
            .then(this.factory().argument("name", new TeamNameParameter())
                .runPlayer((Player p, String name) -> {

                })
            )
        )
        .then(this.factory().literal("recolor")
            .runPlayer(p -> {

            })
        )
        .then(this.factory().literal("invite")
            .then(this.factory().literal("accept")
                .then(this.factory().argument("invite", new EntityInviteParameter())
                    .runPlayer((Player p, MemberInvite invite) -> {

                    })
                )
            )
            .then(this.factory().literal("decline")
                .then(this.factory().argument("invite", new EntityInviteParameter())
                    .runPlayer((Player p, MemberInvite invite) -> {

                    })
                )
            )
            .then(this.factory().argument("player", new EntityNotTeamParameter())
                .runPlayer((Player p, Entity target) -> {

                })
            )
        )
        .then(this.factory().literal("leave")
            .runPlayer((Player p) -> {

            })
        );
    }
}
