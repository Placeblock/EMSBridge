package de.codelix.emsbridge.command.impl;

import de.codelix.commandapi.core.parameter.impl.WordParameter;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperLiteralBuilder;
import de.codelix.emsbridge.command.EntityCommand;
import de.codelix.emsbridge.command.EntityPlayer;
import de.codelix.emsbridge.command.EntitySource;
import de.codelix.emsbridge.command.parameters.EntityInviteParameter;
import de.codelix.emsbridge.command.parameters.EntityNotTeamParameter;
import de.codelix.entitymanagementsystem.models.Entity;
import de.codelix.entitymanagementsystem.models.MemberInvite;
import org.bukkit.plugin.Plugin;

public class TeamCommand extends EntityCommand {
    public TeamCommand(Plugin plugin) {
        super(plugin, "team", false);
    }

    @Override
    public void build(DefaultPaperLiteralBuilder<EntitySource, EntityPlayer> builder) {
        builder
        .then(this.factory().literal("invite")
            .then(this.factory().literal("accept")
                .then(this.factory().argument("invite", new EntityInviteParameter())
                    .runPlayer((EntityPlayer p, MemberInvite invite) -> {

                    })
                )
            )
            .then(this.factory().literal("decline")
                .then(this.factory().argument("invite", new EntityInviteParameter())
                    .runPlayer((EntityPlayer p, MemberInvite invite) -> {

                    })
                )
            )
            .then(this.factory().argument("player", new EntityNotTeamParameter())
                .runPlayer((EntityPlayer p, Entity target) -> {

                })
            )
        )
        .then(this.factory().literal("leave")
            .runPlayer((EntityPlayer p) -> {

            })
        )
        .then(this.factory().literal("create")
            .then(this.factory().argument("name", new WordParameter<>())
                .runPlayer((EntityPlayer p, String name) -> {

                })
            )
        )
        .then(this.factory().literal("rename")
            .then(this.factory().argument("name", new WordParameter<>())
                .runPlayer((EntityPlayer p, String name) -> {

                })
            )
        )
        .then(this.factory().literal("recolor")
            .runPlayer(p -> {

            })
        );
    }
}
