package de.codelix.emsbridge.command.messages;

import de.codelix.commandapi.core.message.CommandMessages;
import de.codelix.emsbridge.command.exceptions.NameOnlyLettersException;
import de.codelix.emsbridge.command.exceptions.NameTooLongException;
import de.codelix.emsbridge.command.exceptions.NameTooShortException;
import de.codelix.emsbridge.command.exceptions.PlayerNotRegisteredException;
import de.codelix.emsbridge.messages.Texts;
import net.kyori.adventure.text.TextComponent;

public class EMSMessages extends CommandMessages<TextComponent> {
    public EMSMessages() {
        add(NameOnlyLettersException.class, (_) ->
                Texts.text("<color:gray>Your name may <color:red>only contain letters<color:gray>."));
        add(NameTooLongException.class, (ex) ->
                Texts.text("<color:gray>The name is <color:red>too long<color:gray>. Maximum: <color:red>" + ex.getMaxLength() + " <color:gray>letters."));
        add(NameTooShortException.class, (ex) ->
                Texts.text("<color:gray>The name is <color:red>too short. <color:gray>Minimum: <color:red>"+ex.getMinLength()+" <color:gray>letters."));
        add(PlayerNotRegisteredException.class, (ex) ->
                Texts.text("<color:gray>You can use this feature <color:red>only if you are registered<color:gray>. <click:run_command:/register><color:green><b>[REGISTER]</b></color></click>"));
    }
}
