package de.codelix.emsbridge.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityPlayerMap {

    private final Map<UUID, Integer> mcToEMS = new HashMap<>();
    private final Map<Integer, UUID> eMSToMc = new HashMap<>();

    public void addPlayer(UUID player, Integer entityId) {
        mcToEMS.put(player, entityId);
        eMSToMc.put(entityId, player);
    }

    public void removePlayer(UUID player) {
        if (this.mcToEMS.containsKey(player)) {
            int entityId = this.mcToEMS.get(player);
            this.mcToEMS.remove(player);
            this.eMSToMc.remove(entityId);
        }
    }

    public Integer getEntityId(UUID player) {
        return this.mcToEMS.get(player);
    }

    public UUID getPlayerUUID(Integer entityId) {
        return this.eMSToMc.get(entityId);
    }

}
