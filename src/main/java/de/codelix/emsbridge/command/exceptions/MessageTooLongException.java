package de.codelix.emsbridge.command.exceptions;

import de.codelix.commandapi.core.exception.ParseException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageTooLongException extends ParseException {
    private final int maxLength;
}
