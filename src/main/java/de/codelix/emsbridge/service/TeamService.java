package de.codelix.emsbridge.service;

import de.codelix.emsbridge.exceptions.NoTeamException;
import de.codelix.emsbridge.messages.Texts;
import de.codelix.entitymanagementsystem.EMS;
import de.codelix.entitymanagementsystem.dto.TeamCreateData;
import de.codelix.entitymanagementsystem.models.Entity;
import de.codelix.entitymanagementsystem.models.Member;
import de.codelix.entitymanagementsystem.models.MemberInvite;
import de.codelix.entitymanagementsystem.models.Team;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TeamService {
    private final EMS ems;
    private final EntityService entityService;
    private final Scoreboard scoreboard;

    public Team getTeam(UUID playerUuid) {
        Integer entityId = this.entityService.getEntityIdNullable(playerUuid);
        if (entityId == null) return null;
        return this.getTeam(entityId);
    }
    public Team getTeamByLocalPlayerUuid(UUID playerUuid) {
        Integer entityId = this.entityService.getEntityIdNullableLocal(playerUuid);
        if (entityId == null) return null;
        return this.getTeam(entityId);
    }
    public Team getTeam(int entityId) {
        return this.ems.getTeamByEntityId(entityId).join();
    }
    public List<MemberInvite> getInvites(int entityId) {
        return this.ems.getInvitesByEntityId(entityId).join();
    }
    public List<Member> getMembers() {
        return this.ems.getMembers().join();
    }
    public Member getMember(int memberId) {
        return this.ems.getMember(memberId).join();
    }
    public Member getMemberByEntityId(int entityId) {
        return this.ems.getMemberByEntityId(entityId).join();
    }

    public TeamCreateData createTeam(final String name, final float hue, UUID creator) {
        int entityId = this.entityService.getEntityIdLocal(creator);
        return this.ems.createTeam(name, hue, entityId).join();
    }

    public void leaveTeam(final UUID playerUuid) {
        int entityId = this.entityService.getEntityIdLocal(playerUuid);
        this.ems.leaveTeam(entityId).join();
    }

    public void renameTeam(final UUID playerUuid, String newName) {
        int entityId = this.entityService.getEntityIdLocal(playerUuid);
        Member member = this.ems.getMemberByEntityId(entityId).join();
        if (member == null) {
            throw new NoTeamException();
        }
        this.ems.renameTeam(member.getTeamId(), newName).join();
    }

    public void recolorTeam(final UUID playerUuid, float hue) {
        int entityId = this.entityService.getEntityIdLocal(playerUuid);
        Member member = this.ems.getMemberByEntityId(entityId).join();
        this.ems.recolorTeam(member.getTeamId(), hue).join();
    }

    public void inviteEntity(UUID inviterUuid, int invitedId) {
        int inviterId = this.entityService.getEntityIdLocal(inviterUuid);
        Integer inviterMemberId = this.getMemberByEntityId(inviterId).getId();
        if (inviterMemberId == null) {
            throw new NoTeamException();
        }
        this.ems.createInvite(MemberInvite.builder()
                .inviterId(inviterMemberId)
                .invitedId(invitedId)
                .build())
        .join();
    }

    public void acceptInvite(MemberInvite invite) {
        this.ems.acceptInvite(invite.getId()).join();
    }

    public void declineInvite(MemberInvite invite) {
        this.ems.declineInvite(invite.getId()).join();
    }

    public void addPlayerToTeamLocal(int entityId, int teamId) {
        Team team = this.ems.getTeam(teamId).join();
        this.addPlayerToTeamLocal(entityId, team);
    }
    public void addPlayerToTeamLocal(int entityId, Team team) {
        UUID playerUuid = this.entityService.getPlayerUuidNullableLocal(entityId);
        if (playerUuid == null) return;
        Player player = Bukkit.getPlayer(playerUuid);
        if (player == null) return;
        this.createOrUpdatePlayerBukkitTeam(entityId, team, player);
    }

    private void createOrUpdatePlayerBukkitTeam(int entityId, Team team, Player player) {
        String teamName = getPlayerBukkitTeamName(entityId);
        org.bukkit.scoreboard.Team bukkitTeam = this.scoreboard.getTeam(teamName);
        if (bukkitTeam == null) {
            bukkitTeam = this.scoreboard.registerNewTeam(teamName);
        }
        bukkitTeam.prefix(TeamService.getTeamPrefix(team.getName(), TeamService.getTeamColor(team.getHue())));
        bukkitTeam.addPlayer(player);
        this.entityService.renameEntityLocal(player, player.displayName(), team);
    }

    public void removePlayerFromTeamLocal(int entityId) {
        Entity entity = this.entityService.getEntity(entityId);
        org.bukkit.scoreboard.Team bukkitTeam = this.scoreboard.getTeam(getPlayerBukkitTeamName(entityId));
        if (bukkitTeam == null) return;
        bukkitTeam.unregister();
        this.entityService.renameEntityLocal(entityId, Texts.text(entity.getName()), null);
    }

    public void updateTeamLocal(Team team) {
        List<Member> members = this.ems.getMembers(team.getId()).join();
        for (Member member : members) {
            UUID playerUuid = this.entityService.getPlayerUuidNullableLocal(member.getEntityId());
            if (playerUuid == null) continue;
            Player player = Bukkit.getPlayer(playerUuid);
            this.createOrUpdatePlayerBukkitTeam(member.getEntityId(), team, player);
        }
    }

    public static @NotNull TextColor getTeamColor(float hue) {
        return TextColor.color(HSVLike.hsvLike(hue, 0.7F, 0.5F));
    }

    public static @NotNull TextComponent getTeamPrefix(String name, TextColor color) {
        return Texts.text("<color:gray>[")
                .append(Component.text(name, color))
                .append(Texts.text("<color:gray>] "));
    }

    private static @NotNull String getPlayerBukkitTeamName(int entityId) {
        return "team-" + entityId;
    }
}
