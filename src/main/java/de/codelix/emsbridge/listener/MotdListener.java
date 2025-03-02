package de.codelix.emsbridge.listener;

import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.messages.Texts;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class MotdListener implements Listener {

    @EventHandler
    public void on(ServerListPingEvent event) {
        Date now = Date.from(LocalDateTime.now(ZoneId.of("UTC")).toInstant(ZoneOffset.UTC));
        Date projectStart = EMSBridge.INSTANCE.getCfg().getProjectStart();
        if (now.compareTo(projectStart) < 0) {
            Duration timeUntilProjectStart = Duration.between(now.toInstant(), projectStart.toInstant());
            long seconds = timeUntilProjectStart.getSeconds();
            long minutes = seconds / 60; seconds %= 60;
            long hours = minutes / 60; minutes %= 60;
            long days = hours / 24; hours %= 24;
            String format = String.format("<color:blue><b>Nostalgicraft</b>\n<color:gray>Adventure starting soon... <b>%d Days, %02d:%02d:%02d</b>", days, hours, minutes, seconds);
            event.motd(Texts.text(format));
            return;
        }

        event.motd(Texts.text("<color:blue><b>Nostalgicraft</b> The adventure has started.\n<color:gray><b>Come, fellow adventurer!</b>"));
    }

}
