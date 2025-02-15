package de.codelix.emsbridge.command.parameters;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.parser.Source;
import de.codelix.emsbridge.command.exceptions.NameOnlyLettersException;
import de.codelix.emsbridge.command.exceptions.NameTooLongException;
import de.codelix.emsbridge.command.exceptions.NameTooShortException;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntityNameParameter<S extends Source<TextComponent>> implements Parameter<String, S, TextComponent> {
    @Override
    public String parse(ParseContext<S, TextComponent> ctx, ParsedCommand<S, TextComponent> cmd) throws ParseException {
        String name = ctx.getInput().poll();
        return validateName(name);
    }

    public static @NotNull String validateName(String name) throws ParseException {
        if (name.length() < 3) throw new NameTooShortException(name, 3);
        if (name.length() > 20) throw new NameTooLongException(name, 20);
        if (!name.matches("[a-zA-Z]+")) throw new NameOnlyLettersException(name);
        return name;
    }

    @Override
    public List<String> getSuggestions(ParseContext<S, TextComponent> ctx, ParsedCommand<S, TextComponent> cmd) {
        return Parameter.super.getSuggestions(ctx, cmd);
    }
}
