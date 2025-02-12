package de.codelix.emsbridge;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class EMSBridge extends JavaPlugin {

    public static EMSBridge INSTANCE;

    @Getter
    private final EntityMap entityMap = new EntityMap();

    @Override
    public void onEnable() {
        INSTANCE = this;
    }
}