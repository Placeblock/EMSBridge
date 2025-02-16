package de.codelix.emsbridge.listener;

import de.codelix.emsbridge.messages.Messages;
import de.codelix.emsbridge.messages.Texts;
import de.codelix.emsbridge.service.EntityService;
import de.codelix.emsbridge.service.TeamService;
import de.codelix.entitymanagementsystem.EMSListener;
import de.codelix.entitymanagementsystem.dto.TeamCreateData;
import de.codelix.entitymanagementsystem.models.Entity;
import de.codelix.entitymanagementsystem.models.Member;
import de.codelix.entitymanagementsystem.models.MemberInvite;
import de.codelix.entitymanagementsystem.models.Team;
import de.codelix.entitymanagementsystem.realtime.*;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
public class ZeromqListener extends EMSListener {
    private final EntityService entityService;
    private final TeamService teamService;

    @Override
    protected void onEntityRename(EntityRenameAction action) {
        Entity entity = action.getData();
        Integer entityId = entity.getId();
        Team team = this.teamService.getTeam(entityId);
        this.entityService.renameEntityLocal(entityId, Texts.text(entity.getName()), team);
    }

    @Override
    protected void onMemberCreate(MemberCreateAction action) {
        Member member = action.getData();
        this.teamService.addPlayerToTeamLocal(member.getEntityId(), member.getTeamId());
    }

    @Override
    protected void onMemberRemove(MemberRemoveAction action) {
        int entityId = action.getData().getEntityId();
        this.teamService.removePlayerFromTeamLocal(entityId);
    }

    @Override
    protected void onMemberInvite(MemberInviteAction action) {
        MemberInvite invite = action.getData();
        int inviterMemberId = invite.getInviterId();
        int invitedId = invite.getInvitedId();
        Entity invited = this.entityService.getEntity(invitedId);
        Member inviter = this.teamService.getMember(inviterMemberId);
        UUID invitedUuid = this.entityService.getPlayerUuidNullableLocal(invited.getId());
        if (invitedUuid == null) return;
        Player invitedPlayer = Bukkit.getPlayer(invitedUuid);
        if (invitedPlayer == null) return;
        invitedPlayer.sendMessage(Messages.INVITE_CREATED_INVITED(inviter.getEntity().getName()));
    }

    @Override
    protected void onInviteDecline(InviteDeclineAction action) {
        sendInviteProcessedMessage(action.getData(), false);
    }

    @Override
    protected void onInviteAccept(InviteAcceptAction action) {
        sendInviteProcessedMessage(action.getData(), true);
    }

    private void sendInviteProcessedMessage(MemberInvite invite, boolean accept) {
        String invitedName = invite.getInvited().getName();
        Integer inviterId = invite.getInviter().getEntityId();
        UUID inviterUuid = this.entityService.getPlayerUuidNullableLocal(inviterId);
        if (inviterUuid == null) return;
        Player inviterPlayer = Bukkit.getPlayer(inviterUuid);
        if (inviterPlayer == null) return;
        inviterPlayer.sendMessage(accept ? Messages.INVITE_ACCEPTED_INVITER(invitedName)
                : Messages.INVITE_DECLINED_INVITER(invitedName));
    }

    @Override
    protected void onTeamCreate(TeamCreateAction action) {
        TeamCreateData team = action.getData();
        Member member = team.getMember();
        this.teamService.addPlayerToTeamLocal(member.getEntityId(), team.getTeam());
    }

    @Override
    protected void onTeamRename(TeamRenameAction action) {
        this.teamService.updateTeamLocal(action.getData());
    }

    @Override
    protected void onTeamRecolor(TeamRecolorAction action) {
        this.teamService.updateTeamLocal(action.getData());
    }
}
