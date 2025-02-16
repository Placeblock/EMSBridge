package de.codelix.emsbridge.command.impl;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.paper.DefaultPaperSource;
import de.codelix.commandapi.paper.PlayerPaperCommand;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperLiteralBuilder;
import de.codelix.emsbridge.command.parameters.EntityInviteParameter;
import de.codelix.emsbridge.command.parameters.EntityNotTeamParameter;
import de.codelix.emsbridge.command.parameters.TeamNameParameter;
import de.codelix.emsbridge.gui.ColorGUI;
import de.codelix.emsbridge.gui.NameInputGUI;
import de.codelix.emsbridge.messages.Messages;
import de.codelix.emsbridge.messages.Texts;
import de.codelix.emsbridge.service.EntityService;
import de.codelix.emsbridge.service.TeamService;
import de.codelix.entitymanagementsystem.models.Entity;
import de.codelix.entitymanagementsystem.models.MemberInvite;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TeamCommand extends PlayerPaperCommand {
    private final EntityService entityService;
    private final TeamService teamService;

    public TeamCommand(Plugin plugin, EntityService entityService, TeamService teamService) {
        super(plugin, "teams", false);
        this.entityService = entityService;
        this.teamService = teamService;
    }

    @Override
    public void build(DefaultPaperLiteralBuilder<DefaultPaperSource, Player> builder) {
        builder
        .then(this.factory().literal("create")
            .then(this.factory().argument("name", new TeamNameParameter())
                .runPlayer(this::createTeam)
            )
            .runPlayer(this::showCreateGUI)
        )
        .then(this.factory().literal("rename")
            .then(this.factory().argument("name", new TeamNameParameter())
                .runPlayer(this::renameTeam)
            )
            .runPlayer(this::showRenameGUI)
        )
        .then(this.factory().literal("recolor")
            .runPlayer(this::recolorTeam)
        )
        .then(this.factory().literal("invite")
            .then(this.factory().literal("accept")
                .then(this.factory().argument("invite", new EntityInviteParameter())
                    .runPlayer(this::acceptInvite)
                )
            )
            .then(this.factory().literal("decline")
                .then(this.factory().argument("invite", new EntityInviteParameter())
                    .runPlayer(this::declineInvite)
                )
            )
            .then(this.factory().argument("player", new EntityNotTeamParameter())
                .runPlayer(this::createInvite)
            )
        )
        .then(this.factory().literal("leave")
            .runPlayer(this::leaveTeam)
        );
    }

    private void createTeam(Player p, String name) {
        new ColorGUI(this.getPlugin(), Texts.text("Choose Team Color"), (hue) ->
            Bukkit.getScheduler().runTaskAsynchronously(this.getPlugin(), () -> {
                try {
                    this.teamService.createTeam(name, hue, p.getUniqueId());
                    p.sendMessage(Messages.TEAM_CREATED(name, hue));
                } catch (RuntimeException ex) {
                    p.sendMessage(Messages.ERROR_CREATE_TEAM);
                    throw ex;
                }
            }
        )).showPlayer(p);
    }

    private void showCreateGUI(Player p) {
        new NameInputGUI(this.getPlugin(), Texts.text("Enter Team Name"),
            p, name -> {
                try {
                    TeamNameParameter.validateName(name);
                } catch (ParseException e) {
                    p.sendMessage(this.getDesign().getMessages().getMessage(e));
                    p.sendMessage(Texts.text("<click:run_command:/team create><b><color:green>[TRY AGAIN]</color></b></click>"));
                    return;
                }
                this.createTeam(p, name);
        }).showPlayer(p);
    }

    private void renameTeam(Player p, String name) {
        Bukkit.getScheduler().runTaskAsynchronously(this.getPlugin(), () -> {
            try {
                this.teamService.renameTeam(p.getUniqueId(), name);
                p.sendMessage(Messages.TEAM_RENAMED(name));
            } catch (RuntimeException ex) {
                p.sendMessage(Messages.ERROR_RENAME_TEAM);
                throw ex;
            }
        });
    }

    private void showRenameGUI(Player p) {
        new NameInputGUI(this.getPlugin(), Texts.text("Enter new Team Name"),
            p, name -> {
            try {
                TeamNameParameter.validateName(name);
            } catch (ParseException e) {
                p.sendMessage(this.getDesign().getMessages().getMessage(e));
                p.sendMessage(Texts.text("<click:run_command:/team create><b><color:green>[TRY AGAIN]</color></b></click>"));
                return;
            }
            this.renameTeam(p, name);
        }).showPlayer(p);
    }

    private void leaveTeam(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(this.getPlugin(), () -> {
            try {
                this.teamService.leaveTeam(p.getUniqueId());
                p.sendMessage(Messages.TEAM_LEFT);
            } catch (RuntimeException ex) {
                p.sendMessage(Messages.ERROR_LEAVE_TEAM);
                throw ex;
            }
        });
    }

    private void recolorTeam(Player p) {
        new ColorGUI(this.getPlugin(), Texts.text("Choose new Team Color"), (hue) ->
            Bukkit.getScheduler().runTaskAsynchronously(this.getPlugin(), () -> {
                try {
                    this.teamService.recolorTeam(p.getUniqueId(), hue);
                    p.sendMessage(Messages.TEAM_RECOLORED(hue));
                } catch (RuntimeException ex) {
                    p.sendMessage(Messages.ERROR_RECOLOR_TEAM);
                    throw ex;
                }
            })
        ).showPlayer(p);
    }

    private void createInvite(Player p, Entity target) {
        Bukkit.getScheduler().runTaskAsynchronously(this.getPlugin(), () -> {
            try {
                this.teamService.inviteEntity(p.getUniqueId(), target.getId());
                p.sendMessage(Messages.INVITE_CREATED(target.getName()));
            } catch (RuntimeException ex) {
                p.sendMessage(Messages.ERROR_CREATE_INVITE);
                throw ex;
            }
        });
    }

    private void acceptInvite(Player p, MemberInvite invite) {
        Bukkit.getScheduler().runTaskAsynchronously(this.getPlugin(), () -> {
            try {
                this.teamService.acceptInvite(invite);
                Entity inviter = this.entityService.getEntity(invite.getInviterId());
                p.sendMessage(Messages.INVITE_ACCEPTED(inviter.getName()));
            } catch (RuntimeException ex) {
                p.sendMessage(Messages.ERROR_ACCEPT_INVITE);
                throw ex;
            }
        });
    }

    private void declineInvite(Player p, MemberInvite invite) {
        Bukkit.getScheduler().runTaskAsynchronously(this.getPlugin(), () -> {
            try {
                this.teamService.declineInvite(invite);
                Entity inviter = this.entityService.getEntity(invite.getInviterId());
                p.sendMessage(Messages.INVITE_DECLINED(inviter.getName()));
            } catch (RuntimeException ex) {
                p.sendMessage(Messages.ERROR_DECLINE_INVITE);
                throw ex;
            }
        });
    }


}
