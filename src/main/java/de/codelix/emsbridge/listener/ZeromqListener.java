package de.codelix.emsbridge.listener;

import de.codelix.emsbridge.service.EntityService;
import de.codelix.entitymanagementsystem.EMSListener;
import de.codelix.entitymanagementsystem.models.Entity;
import de.codelix.entitymanagementsystem.realtime.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ZeromqListener extends EMSListener {
    private final EntityService entityService;

    @Override
    protected void onEntityRename(EntityRenameAction action) {
        Entity entity = action.getData();
        Integer entityId = entity.getId();
        this.entityService.renameEntityLocal(entityId, entity.getName());
    }

    @Override
    protected void onMemberCreate(MemberCreateAction action) {

    }

    @Override
    protected void onMemberRemove(MemberRemoveAction action) {

    }

    @Override
    protected void onTeamCreate(TeamCreateAction action) {

    }

    @Override
    protected void onTeamRename(TeamRenameAction action) {

    }

    @Override
    protected void onTeamRecolor(TeamRecolorAction action) {

    }
}
