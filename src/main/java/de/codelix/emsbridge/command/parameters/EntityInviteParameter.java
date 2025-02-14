package de.codelix.emsbridge.command.parameters;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.emsbridge.command.EntitySource;
import de.codelix.entitymanagementsystem.models.MemberInvite;
import net.kyori.adventure.text.TextComponent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EntityInviteParameter implements Parameter<MemberInvite, EntitySource, TextComponent> {
    @Override
    public MemberInvite parse(ParseContext<EntitySource, TextComponent> parseContext, ParsedCommand<EntitySource, TextComponent> parsedCommand) throws ParseException {
        return null;
    }

    @Override
    public CompletableFuture<List<String>> getSuggestionsAsync(ParseContext<EntitySource, TextComponent> ctx, ParsedCommand<EntitySource, TextComponent> cmd) {
        return Parameter.super.getSuggestionsAsync(ctx, cmd);
    }
}
