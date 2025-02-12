package de.codelix.emsbridge;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public class Config {
    private final DB db;

    public Config(DB db) {
        this.db = db;
    }

    public Config(ConfigurationSection section) {
        ConfigurationSection db = section.getConfigurationSection("db");
        if (db == null) {
            throw new IllegalArgumentException("Database configuration not found");
        }
        this.db = new DB(db);
    }

    @Getter
    public static class DB {
        private final String host;
        private final String port;
        private final String database;
        private final String username;
        private final String password;
        private final Boolean createForeignKeyEMS;

        public DB(ConfigurationSection section) {
            this(
                    section.getString("host"),
                    section.getString("port"),
                    section.getString("database"),
                    section.getString("username"),
                    section.getString("password"),
                    section.getBoolean("createForeignKeyEMS")
            );
        }

        public DB(String host, String port, String database, String username, String password, Boolean createForeignKeyEMS) {
            this.host = host;
            this.port = port;
            this.database = database;
            this.username = username;
            this.password = password;
            this.createForeignKeyEMS = createForeignKeyEMS;


            if (this.host == null) {
                throw new IllegalArgumentException("Missing required configuration: host");
            }
            if (this.port == null) {
                throw new IllegalArgumentException("Missing required configuration: port");
            }
            if (this.database == null) {
                throw new IllegalArgumentException("Missing required configuration: database");
            }
            if (this.username == null) {
                throw new IllegalArgumentException("Missing required configuration: username");
            }
            if (this.password == null) {
                throw new IllegalArgumentException("Missing required configuration: password");
            }
            if (this.createForeignKeyEMS == null) {
                throw new IllegalArgumentException("Missing required configuration: createForeignKeyEMS");
            }
        }
    }

}
