package de.codelix.emsbridge.command.parameters;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.paper.DefaultPaperSource;
import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.command.exceptions.InvalidEntityException;
import de.codelix.emsbridge.command.exceptions.NoTeamException;
import de.codelix.emsbridge.command.exceptions.SameTeamException;
import de.codelix.entitymanagementsystem.models.Entity;
import de.codelix.entitymanagementsystem.models.Member;
import de.codelix.entitymanagementsystem.models.Team;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

import java.util.List;

public class EntityNotTeamParameter extends EntityParameter<Entity> {

    @Override
    protected Entity parse(ParseContext<DefaultPaperSource, TextComponent> ctx, ParsedCommand<DefaultPaperSource, TextComponent> cmd, Player player, int entityId) throws ParseException {
        System.out.println("PARSE");
        List<Entity> entities = EMSBridge.INSTANCE.getEntityService().getEntities();
        List<Member> members = EMSBridge.INSTANCE.getTeamService().getMembers();
        Team ownTeam = EMSBridge.INSTANCE.getTeamService().getTeam(entityId);
        if (ownTeam == null) {
            throw new NoTeamException();
        }
        String name = ctx.getInput().poll();
        Entity entity = entities.stream().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        if (entity == null) {
            throw new InvalidEntityException(name);
        }
        Member member = members.stream()
                .filter(m -> entity.getId().equals(m.getEntityId()) && ownTeam.getId().equals(m.getTeamId()))
                .findFirst().orElse(null);
        if (member != null) {
            throw new SameTeamException(entity);
        }
        return entity;
    }

    @Override
    protected List<String> getSuggestions(ParseContext<DefaultPaperSource, TextComponent> ctx, ParsedCommand<DefaultPaperSource, TextComponent> cmd, Player player, int entityId) {
        List<Entity> entities = EMSBridge.INSTANCE.getEntityService().getEntities();
        List<Member> members = EMSBridge.INSTANCE.getTeamService().getMembers();
        Team ownTeam = EMSBridge.INSTANCE.getTeamService().getTeam(entityId);
        if (ownTeam == null) {
            return List.of();
        }
        return entities.stream()
            .filter(e -> members.stream()
                    .filter(m -> e.getId().equals(m.getEntityId()) && ownTeam.getId().equals(m.getTeamId()))
                    .findFirst().isEmpty())
            .map(Entity::getName)
            .toList();
    }
}
