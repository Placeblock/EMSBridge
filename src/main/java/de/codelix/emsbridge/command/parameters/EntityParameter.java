package de.codelix.emsbridge.command.parameters;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.paper.DefaultPaperSource;
import de.codelix.emsbridge.EMSBridge;
import de.codelix.emsbridge.command.exceptions.PlayerNotRegisteredException;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class EntityParameter<T> implements Parameter<T, DefaultPaperSource, TextComponent> {
    @Override
    public T parse(ParseContext<DefaultPaperSource, TextComponent> ctx, ParsedCommand<DefaultPaperSource, TextComponent> cmd) throws ParseException {
        DefaultPaperSource source = ctx.getSource();
        Player player = source.getPlayer();
        if (player == null) {
            throw new PlayerNotRegisteredException();
        }
        UUID playerUuid = player.getUniqueId();
        Integer entityId = EMSBridge.INSTANCE.getEntityService().getEntityIdNullableLocal(playerUuid);
        if (entityId == null) {
            throw new PlayerNotRegisteredException();
        }
        return this.parse(ctx, cmd, player, entityId);
    }

    protected abstract T parse(ParseContext<DefaultPaperSource, TextComponent> ctx,
                      ParsedCommand<DefaultPaperSource, TextComponent> cmd,
                      Player player,
                      int entityId) throws ParseException;

    @Override
    public CompletableFuture<List<String>> getSuggestionsAsync(ParseContext<DefaultPaperSource, TextComponent> ctx, ParsedCommand<DefaultPaperSource, TextComponent> cmd) {
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(EMSBridge.INSTANCE, () -> {
            DefaultPaperSource source = ctx.getSource();
            Player player = source.getPlayer();
            if (player == null) {
                future.complete(List.of());
                return;
            }
            UUID playerUuid = player.getUniqueId();
            Integer entityId = EMSBridge.INSTANCE.getEntityService().getEntityIdNullableLocal(playerUuid);
            if (entityId == null) {
                future.complete(List.of());
                return;
            }
            future.complete(this.getSuggestions(ctx, cmd, player, entityId));
        });
        return future;
    }

    protected abstract List<String> getSuggestions(ParseContext<DefaultPaperSource, TextComponent> ctx,
                                                   ParsedCommand<DefaultPaperSource, TextComponent> cmd,
                                                   Player player,
                                                   int entityId);
}
