package de.codelix.emsbridge.command.parameters;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.paper.DefaultPaperSource;
import de.codelix.entitymanagementsystem.models.MemberInvite;
import net.kyori.adventure.text.TextComponent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TeamInvitesParameter implements Parameter<MemberInvite, DefaultPaperSource, TextComponent> {
    @Override
    public MemberInvite parse(ParseContext<DefaultPaperSource, TextComponent> parseContext, ParsedCommand<DefaultPaperSource, TextComponent> parsedCommand) throws ParseException {
        return null;
    }

    @Override
    public CompletableFuture<List<String>> getSuggestionsAsync(ParseContext<DefaultPaperSource, TextComponent> ctx, ParsedCommand<DefaultPaperSource, TextComponent> cmd) {
        return Parameter.super.getSuggestionsAsync(ctx, cmd);
    }
}
