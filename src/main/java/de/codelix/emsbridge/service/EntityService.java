package de.codelix.emsbridge.service;

import de.codelix.emsbridge.exceptions.*;
import de.codelix.emsbridge.messages.Texts;
import de.codelix.emsbridge.storage.EntityPlayerMap;
import de.codelix.emsbridge.storage.EntityPlayerRepository;
import de.codelix.entitymanagementsystem.EMS;
import de.codelix.entitymanagementsystem.models.Entity;
import de.codelix.entitymanagementsystem.models.Team;
import de.codelix.entitymanagementsystem.models.Token;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class EntityService {
    private final EntityPlayerRepository repo;
    private final EMS ems;
    private final EntityPlayerMap entityPlayerMap;

    public void addOnlinePlayer(UUID playerUuid, int entityId) {
        this.entityPlayerMap.addPlayer(playerUuid, entityId);
    }
    public void removeOnlinePlayer(UUID playerUuid) {
        this.entityPlayerMap.removePlayer(playerUuid);
    }

    public List<Entity> getEntities() {
        return this.ems.getEntities().join();
    }

    public Entity getEntity(final int id) {
        Entity entity;
        try {
            entity = this.ems.getEntity(id).join();
        } catch (RuntimeException e) {
            throw new EntityLoadException(id, e);
        }
        if (entity == null) {
            throw new EntityLoadException(id);
        }
        return entity;
    }

    public Entity getEntityByPlayerUuid(UUID playerUuid) {
        Integer entityId = this.getEntityIdNullable(playerUuid);
        if (entityId == null) return null;
        return this.getEntity(entityId);
    }

    public void renameEntity(UUID playerUuid, String newName) {
        int entityId = this.getEntityIdLocal(playerUuid);
        this.ems.renameEntity(entityId, newName).join();
    }

    public Entity createEntity(UUID playerUuid, String newName) {
        Integer existingEntityId = this.entityPlayerMap.getEntityId(playerUuid);
        if (existingEntityId != null) {
            throw new PlayerAlreadyRegisteredException(playerUuid);
        }
        Entity entity = this.ems.createEntity(newName).join();
        try {
            this.repo.savePlayerEntityId(playerUuid, entity.getId());
        } catch (SQLException e) {
            throw new LinkNotCreatedException(playerUuid, entity.getId());
        }
        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            this.renameEntityLocal(player, Texts.text(newName), null);
        }
        this.entityPlayerMap.addPlayer(playerUuid, entity.getId());
        return entity;
    }

    public void renameEntityLocal(int entityId, final Component entityName, final Team team) {
        UUID playerUuid = this.entityPlayerMap.getPlayerUUID(entityId);
        Player player = Bukkit.getPlayer(playerUuid);
        if (player == null) return;
        this.renameEntityLocal(player, entityName, team);
    }

    public void renameEntityLocal(final Player player, final Component entityName, final Team team) {
        player.displayName(entityName);
        if (team != null) {
            player.playerListName(TeamService.getTeamPrefix(team.getName(),
                TeamService.getTeamColor(team.getHue()))
                .append(entityName));
        } else {
            player.playerListName(entityName);
        }
    }

    public Integer getEntityIdNullable(UUID playerUuid) {
        Integer entityId;
        try {
            entityId = this.repo.getEntityIdByPlayerUuid(playerUuid);
        } catch (SQLException e) {
            throw new EntityIdLoadException(playerUuid);
        }
        return entityId;
    }

    public int getEntityIdLocal(UUID playerUuid) {
        Integer entityId = this.getEntityIdNullableLocal(playerUuid);
        if (entityId == null) {
            throw new PlayerNotRegisteredException(playerUuid);
        }
        return entityId;
    }

    public Integer getEntityIdNullableLocal(UUID playerUuid) {
        return this.entityPlayerMap.getEntityId(playerUuid);
    }

    public @NotNull UUID getPlayerUuidLocal(int entityId) {
        UUID playerUuid = this.getPlayerUuidNullableLocal(entityId);
        if (playerUuid == null) {
            throw new PlayerNotRegisteredException(playerUuid);
        }
        return playerUuid;
    }

    public @Nullable UUID getPlayerUuidNullableLocal(int entityId) {
        return this.entityPlayerMap.getPlayerUUID(entityId);
    }

    public Token requestToken(int entityId) {
        return this.ems.createToken(entityId).join();
    }
}
