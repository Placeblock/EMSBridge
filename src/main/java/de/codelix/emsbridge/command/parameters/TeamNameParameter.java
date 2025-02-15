package de.codelix.emsbridge.command.parameters;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.paper.DefaultPaperSource;
import de.codelix.emsbridge.command.exceptions.NameOnlyLettersException;
import de.codelix.emsbridge.command.exceptions.NameTooShortException;
import net.kyori.adventure.text.TextComponent;

import java.util.List;

public class TeamNameParameter implements Parameter<String, DefaultPaperSource, TextComponent> {
    @Override
    public String parse(ParseContext<DefaultPaperSource, TextComponent> ctx, ParsedCommand<DefaultPaperSource, TextComponent> cmd) throws ParseException {
        String name = ctx.getInput().poll();
        if (name.length() < 3) throw new NameTooShortException(name, 3);
        if (name.length() > 8) throw new NameTooShortException(name, 8);
        if (!name.matches("[a-zA-Z]+")) throw new NameOnlyLettersException(name);
        return name;
    }

    @Override
    public List<String> getSuggestions(ParseContext<DefaultPaperSource, TextComponent> ctx, ParsedCommand<DefaultPaperSource, TextComponent> cmd) {
        return Parameter.super.getSuggestions(ctx, cmd);
    }
}
