package de.codelix.emsbridge.minimessage;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;

public class Texts {

    @Getter
    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder().tags(
        TagResolver.builder().resolver(StandardTags.defaults()).build()
    ).build();

    public static TextComponent text(String text) {
        return Component.empty().append(MINI_MESSAGE.deserialize(text));
    }

}
