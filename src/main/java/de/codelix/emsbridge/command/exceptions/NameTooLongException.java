package de.codelix.emsbridge.command.exceptions;

import de.codelix.commandapi.core.exception.ParseException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NameTooLongException extends ParseException {
    private final String name;
    private final int maxLength;
}
