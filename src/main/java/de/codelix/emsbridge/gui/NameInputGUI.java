package de.codelix.emsbridge.gui;

import de.placeblock.betterinventories.gui.impl.textinput.TextInputGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class NameInputGUI extends TextInputGUI {
    private final Consumer<String> callback;

    public NameInputGUI(Plugin plugin, TextComponent title, Player player,
                        Consumer<String> callback) {
        super(plugin, title, false, player, "",
                (_, _) -> true, (_) -> {}, Component::text);
        this.callback = callback;
    }

    @Override
    public boolean onFinish(String text, boolean abort) {
        if (abort) return true;
        this.callback.accept(text);
        return true;
    }

}
