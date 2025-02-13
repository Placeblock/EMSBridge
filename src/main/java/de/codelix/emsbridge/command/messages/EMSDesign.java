package de.codelix.emsbridge.command.messages;

import de.codelix.commandapi.adventure.AdventureDesign;
import de.codelix.commandapi.adventure.AdventureSource;
import org.bukkit.command.CommandSender;

public class EMSDesign<S extends AdventureSource<P, CommandSender>, P> extends AdventureDesign<S> {
    public EMSDesign() {
        super(new EMSMessages());
    }
}
