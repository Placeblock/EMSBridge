package de.codelix.emsbridge.service;

import de.codelix.emsbridge.exceptions.BukkitTeamNotExistsException;
import de.codelix.emsbridge.messages.Texts;
import de.codelix.entitymanagementsystem.EMS;
import de.codelix.entitymanagementsystem.models.MemberInvite;
import de.codelix.entitymanagementsystem.models.Team;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
public class TeamService {
    private final EMS ems;
    private final EntityService entityService;
    private final Scoreboard scoreboard;

    public Team createTeam(final String name, final float hue, UUID creator) {
        int entityId = this.entityService.getEntityId(creator);
        Team team = this.ems.createTeam(name, hue, entityId).join();
        this.updateOrCreateTeam(team);
        this.addPlayerToTeamLocal(creator, team);
        return team;
    }

    public void leaveTeam(final UUID playerUuid) {
        int entityId = this.entityService.getEntityId(playerUuid);
        this.ems.leaveTeam(entityId).join();
    }

    public void renameTeam(final int teamId, String newName) {
        this.ems.renameTeam(teamId, newName).join();
    }

    public void inviteEntity(UUID inviterUuid, int invitedId) {
        this.ems.createInvite(new MemberInvite()).join();
    }

    public void addPlayerToTeamLocal(UUID playerUuid, Team team) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerUuid);
        org.bukkit.scoreboard.Team bukkitTeam = this.scoreboard.getTeam(team.getName());
        if (bukkitTeam == null) {
            throw new BukkitTeamNotExistsException(team.getName());
        }
        bukkitTeam.addPlayer(player);
    }

    public void removePlayerFromTeamLocal(UUID playerUuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerUuid);
        org.bukkit.scoreboard.Team bukkitTeam = this.scoreboard.getPlayerTeam(player);
        bukkitTeam.removePlayer(player);
    }

    private void updateOrCreateTeam(Team team) {
        org.bukkit.scoreboard.Team bukkitTeam = this.scoreboard.getTeam(team.getName());
        if (bukkitTeam == null) {
            bukkitTeam = this.scoreboard.registerNewTeam(team.getName());
        }
        TextColor color = TextColor.color(HSVLike.hsvLike(team.getHue(), 0.3F, 0.5F));
        bukkitTeam.prefix(getTeamPrefix(team, color));
    }

    private static @NotNull TextComponent getTeamPrefix(Team team, TextColor color) {
        return Texts.text("<color:gray>[")
                .append(Component.text(team.getName(), color))
                .append(Texts.text("<color:gray>]"));
    }
}
