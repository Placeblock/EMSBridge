package de.codelix.emsbridge.messages;

import de.codelix.emsbridge.service.TeamService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;

public class Messages {

    public static final Title TITLE = Title.title(Texts.text("<color:blue><b>Nostalgicraft"), Component.empty());

    public static final Component REPORT = Texts.text("<b><color:red>Please report this to the admin!</color></b>");
    public static final Component ERROR_LOAD_ENTITY = Texts.text("<color:red>Failed to load player data.<newline>").append(REPORT);

    public static Component ERROR_CHECK_REGISTERED(String name) {
        return Texts.text("<color:red>Could not check if you are already registered<color:gray>. " +
                "<color:blue><b><click:run_command:/register " + name + ">[RETRY]</click></b></color>");
    }

    public static final Component ERROR_ALREADY_REGISTERED = Texts.text("<color:gray>You are <color:red>already registered<color:gray>. " +
            "<color:blue><b><click:run_command:/account rename>[RENAME]</click></b></color>");

    public static final Component ERROR_CREATE_ENTITY = Texts.text("<color:gray>Could not create Account. ").append(REPORT);
    public static final Component ERROR_RENAME_ENTITY = Texts.text("<color:gray>Could not rename Account. ").append(REPORT);
    public static final Component ENTITY_CREATED = Texts.text("<color:gray>Du hast dich <color:green>erfolgreich angemeldet<color:gray>");

    public static final Component ERROR_SEND_TEAM_MESSAGE = Texts.text("<color:gray>Could not send Message. ").append(REPORT);

    public static final Component ERROR_NO_TEAM = Texts.text("<color:gray>You are <color:red>not in any team<color:gray>.");
    public static final Component ERROR_CREATE_TEAM = Texts.text("<color:red>Could not create Team<color:gray>. ").append(REPORT);
    public static Component TEAM_CREATED(String name, float hue) {
        TextColor color = TeamService.getTeamColor(hue);
        return Texts.text("<color:gray>Here we go! Team ").append(Component.text(name, color))
            .append(Texts.text(" <color:green>created<color:gray>."));
    }
    public static final Component ERROR_RENAME_TEAM = Texts.text("<color:gray>Could not rename Team<color:gray>. ").append(REPORT);
    public static Component TEAM_RENAMED(String name) {
        return Texts.text("<color:gray>No problem! <color:green>Renamed Team <color:gray>to <color:blue>" +
                name+"<color:gray>.");
    }
    public static final Component ERROR_RECOLOR_TEAM = Texts.text("<color:gray>Could not recolor Team<color:gray>. ").append(REPORT);
    public static Component TEAM_RECOLORED(float hue) {
        TextColor color = TeamService.getTeamColor(hue);
        return Texts.text("<color:gray>Well done! <color:green>Recolored Team<color:gray>. ")
                .append(Component.text("This is your new Team Color", color));
    }
    public static final Component ERROR_LEAVE_TEAM = Texts.text("<color:gray>Could not leave Team<color:gray>. ").append(REPORT);
    public static final Component TEAM_LEFT = Texts.text("<color:gray>You <color:green>left your team<color:gray>.");
    public static final Component ERROR_CREATE_INVITE = Texts.text("<color:gray>Could not create Invite<color:gray>. ").append(REPORT);
    public static Component INVITE_CREATED(String invitedName) {
        return Texts.text("<color:gray>You <color:green>invited " + invitedName + " <color:gray>to your team.");
    }
    public static final Component ERROR_ACCEPT_INVITE = Texts.text("<color:gray>Could not accept Invite<color:gray>. ").append(REPORT);
    public static Component INVITE_ACCEPTED(String inviterName) {
        return Texts.text("<color:gray>You <color:green>accepted <color:green>" +
                inviterName + "'s Invite<color:gray>.");
    }
    public static final Component ERROR_DECLINE_INVITE = Texts.text("<color:gray>Could not decline Invite. ").append(REPORT);
    public static Component INVITE_DECLINED(String inviterName) {
        return Texts.text("<color:gray>You <color:red>declined <color:blue>" +
                inviterName + "'s <color:gray>Invite.");
    }
    public static Component INVITE_CREATED_INVITED(String inviter) {
        return Texts.text("<color:blue>"+inviter+" <color:gray>invited you to <color:blue>join their Team<color:gray>. " +
                "<color:green><b><click:run_command:/team invite accept " + inviter + ">[ACCEPT]</click></b></color> " +
                "<color:red><b><click:run_command:/team invite decline " + inviter + ">[DECLINE]</click></b></color>");
    }
    public static Component INVITE_ACCEPTED_INVITER(String invitedName) {
        return Texts.text("<color:blue>" + invitedName + " <color:green>accepted <color:gray>your Team-Invite.");
    }
    public static Component INVITE_DECLINED_INVITER(String invitedName) {
        return Texts.text("<color:blue>" + invitedName + " <color:red>declined <color:gray>your Team-Invite.");
    }

    public static Component TEAM_MESSAGE(String player, String message) {
        return Texts.text("<color:green>TEAM <color:gray>| "+player+" > " + message);
    }

    public static Component PIN_MESSAGE(String pin) {
        return Texts.text("<color:green>Pin generated<color:gray>! <color:blue><click:copy_to_clipboard:" + pin + ">[COPY TO CLIPBOARD]</click></color>");
    }
    public static final Component ERROR_CREATE_PIN = Texts.text("<color:gray>Could not create Pin. ").append(REPORT);
}
