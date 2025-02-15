package de.codelix.emsbridge.messages;

import net.kyori.adventure.text.Component;

public class Messages {

    public static final Component REPORT = Texts.text("<b><color:red>Please report this to the admin!</color></b>");
    public static final Component ERROR_LOAD_ENTITY = Texts.text("<color:red>Failed to load player data.<newline>").append(REPORT);

    public static Component ERROR_CHECK_REGISTERED(String name) {
        return Texts.text("<color:red>Could not check if you are already registered<color:gray>. " +
                "<color:green><b><click:run_command:/register " + name + ">[RETRY]</click></b></color>");
    }

    public static final Component ERROR_ALREADY_REGISTERED = Texts.text("<color:gray>You are <color:red>already registered<color:gray>. " +
            "<color:green><b><click:run_command:/account rename>[RENAME]</click></b></color>");

    public static final Component ERROR_CREATE_ENTITY = Texts.text("<color:gray>Could not create Account. ").append(REPORT);

    public static final Component ERROR_RENAME_ENTITY = Texts.text("<color:gray>Could not rename Account. ").append(REPORT);

    public static Component ENTITY_CREATED(String name) {
        return Texts.text("<color:gray>Du hast dich <color:green>erfolgreich angemeldet<color:gray>");
    }
}
