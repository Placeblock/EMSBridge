package de.codelix.emsbridge.gui;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.emsbridge.command.messages.EMSMessages;
import de.codelix.emsbridge.command.parameters.EntityNameParameter;
import de.codelix.emsbridge.messages.Texts;
import de.placeblock.betterinventories.gui.impl.textinput.TextInputGUI;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class NameInputGUI {
    private final JavaPlugin plugin;
    private final Player p;
    private final String retryCommand;
    private final Consumer<String> callback;
    private final EMSMessages messages = new EMSMessages();

    public void show() {
        Bukkit.getScheduler().runTask(this.plugin, () -> new TextInputGUI.Builder<>(this.plugin, this.p)
                .title(Texts.text("Enter Player Name"))
                .onFinish(((finalText, abort) -> {
                    if (abort) return true;
                    try {
                        EntityNameParameter.validateName(finalText);
                    } catch (ParseException e) {
                        this.p.sendMessage(this.messages.getMessage(e));
                        this.p.sendMessage(Texts.text("<click:run_command:/"+this.retryCommand+"><b><color:green>[TRY AGAIN]</color></b></click>"));
                        return true;
                    }
                    this.callback.accept(finalText);
                    return true;
                }))
                .build()
                .showPlayer(this.p));
    }

}
