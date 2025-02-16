package de.codelix.emsbridge.gui;

import de.placeblock.betterinventories.content.item.GUIButton;
import de.placeblock.betterinventories.gui.impl.ChestGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.HSVLike;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class ColorGUI extends ChestGUI {
    private final Consumer<Float> callback;
    /**
     * Creates a new ColorGUI
     *
     * @param plugin      The plugin
     * @param title       The title of the GUI
     */
    public ColorGUI(Plugin plugin, TextComponent title, Consumer<Float> callback) {
        super(plugin, title, false, 6, 6);
        this.callback = callback;
        this.addColorButtons();
        this.update();
    }

    private void addColorButtons() {
        for (float i = 0; i < 1; i+=1/54F) {
            GUIButton colorButton = this.getColorButton(i);
            this.canvas.addItemEmptySlot(colorButton);
        }
    }

    private GUIButton getColorButton(float hue) {
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        TextColor color = TextColor.color(HSVLike.hsvLike(hue, 1F, 0.5F));
        meta.displayName(Component.text("[FARBE AUSWÄHLEN]", color).decoration(TextDecoration.ITALIC, false));
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DYE);
        meta.setColor(Color.fromRGB(color.red(), color.green(), color.blue()));
        ItemStack copy = item.clone();
        copy.setItemMeta(meta);
        return new GUIButton.Builder(this)
                .itemStack(copy)
                .onClick((c) -> {
                    this.callback.accept(hue);
                    c.player().closeInventory();
                })
                .build();
    }
}
