package de.codelix.emsbridge.command.parameters;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.paper.DefaultPaperSource;
import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.command.exceptions.InviteNotExistsException;
import de.codelix.entitymanagementsystem.models.MemberInvite;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

import java.util.List;

public class EntityInviteParameter extends EntityParameter<MemberInvite> {

    @Override
    protected MemberInvite parse(ParseContext<DefaultPaperSource, TextComponent> ctx, ParsedCommand<DefaultPaperSource, TextComponent> cmd, Player player, int entityId) throws ParseException {
        List<MemberInvite> invites = EMSBridge.INSTANCE.getTeamService().getInvites(entityId);
        String name = ctx.getInput().poll();
        return invites.stream()
                .filter(i -> i.getInviter().getEntity().getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new InviteNotExistsException(name));
    }

    @Override
    protected List<String> getSuggestions(ParseContext<DefaultPaperSource, TextComponent> ctx, ParsedCommand<DefaultPaperSource, TextComponent> cmd, Player player, int entityId) {
        List<MemberInvite> invites = EMSBridge.INSTANCE.getTeamService().getInvites(entityId);
        return invites.stream().map(i -> i.getInviter().getEntity().getName()).toList();
    }
}
