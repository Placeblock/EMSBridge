package de.codelix.emsbridge.listener;

import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.messages.Texts;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class MotdListener implements Listener {

    @EventHandler
    public void on(ServerListPingEvent event) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));


        LocalDateTime projectStart = EMSBridge.INSTANCE.getCfg().getProjectStart();
        if (now.isBefore(projectStart)) {
            Duration timeUntilProjectStart = Duration.between(now, projectStart);
            long seconds = timeUntilProjectStart.getSeconds();
            long minutes = seconds / 60; seconds %= 60;
            long hours = minutes / 60; minutes %= 60;
            long days = hours / 24; hours %= 24;
            String format = String.format("<color:blue><b>Nostalgicraft</b>\n<color:gray>Adventure starting soon... <b>%d Days, %d:%d:%d</b>", days, hours, minutes, seconds);
            event.motd(Texts.text(format));
        }

        event.motd(Texts.text("<color:blue><b>Nostalgicraft</b>\n<color:gray>The adventure has started. <b>Come, fellow adventurer!</b>"));
    }

}
