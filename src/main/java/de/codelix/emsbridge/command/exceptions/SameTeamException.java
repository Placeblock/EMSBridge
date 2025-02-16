package de.codelix.emsbridge.command.exceptions;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.entitymanagementsystem.models.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SameTeamException extends ParseException {
    private final Entity entity;
}
