package de.codelix.emsbridge.listener;

import de.codelix.emsbridge.messages.Texts;
import de.codelix.emsbridge.service.EntityService;
import de.codelix.emsbridge.service.TeamService;
import de.codelix.entitymanagementsystem.EMSListener;
import de.codelix.entitymanagementsystem.dto.TeamCreateData;
import de.codelix.entitymanagementsystem.models.Entity;
import de.codelix.entitymanagementsystem.models.Member;
import de.codelix.entitymanagementsystem.models.Team;
import de.codelix.entitymanagementsystem.realtime.*;
import lombok.RequiredArgsConstructor;

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
