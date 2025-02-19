package de.codelix.emsbridge.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.codelix.emsbridge.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EntityPlayerRepository {
    private final HikariDataSource ds;

    public EntityPlayerRepository(Config.SqlDB sqlDbConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + sqlDbConfig.getHost() + ":" + sqlDbConfig.getPort() + "/" + sqlDbConfig.getDatabase());
        config.setUsername(sqlDbConfig.getUsername());
        config.setPassword(sqlDbConfig.getPassword());
        this.ds = new HikariDataSource(config);
        try {
            this.createTable();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not initialize Database", e);
        }
    }

    private void createTable() throws SQLException {
        try(Connection con = this.ds.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement("""
                CREATE TABLE IF NOT EXISTS ems_mc_players (
                    player_uuid VARCHAR(36) NOT NULL UNIQUE,
                    entity_id BIGINT(20) UNSIGNED NOT NULL,
                    PRIMARY KEY (entity_id),
                    FOREIGN KEY (entity_id) REFERENCES entities(id) ON DELETE CASCADE
                );
            """)) {
                stmt.execute();
            }
        }
    }

    public void savePlayerEntityId(UUID playerUuid, int entityId) throws SQLException {
        try(Connection con = this.ds.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement("""
            INSERT INTO ems_mc_players (player_uuid, entity_id) VALUES (?, ?)
        """)) {
                stmt.setString(1, playerUuid.toString());
                stmt.setInt(2, entityId);
                stmt.execute();
            }
        }
    }

    public Integer getEntityIdByPlayerUuid(UUID playerUuid) throws SQLException {
        try(Connection con = this.ds.getConnection()) {
            try(PreparedStatement stmt = con.prepareStatement("""
            SELECT entity_id FROM ems_mc_players WHERE player_uuid = ?
        """)) {
                stmt.setString(1, playerUuid.toString());
                ResultSet resultSet = stmt.executeQuery();
                if (!resultSet.next()) {
                    return null;
                }
                return resultSet.getInt("entity_id");
            }
        }
    }

    public UUID getPlayerUuidByEntityId(int entityId) throws SQLException {
        try(Connection con = this.ds.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement("""
            SELECT player_uuid FROM ems_mc_players WHERE entity_id = ?
        """)) {
                stmt.setInt(1, entityId);
                ResultSet resultSet = stmt.executeQuery();
                if (!resultSet.next()) {
                    return null;
                }
                return UUID.fromString(resultSet.getString("player_uuid"));
            }
        }
    }

}
