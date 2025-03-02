package de.codelix.emsbridge;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class Config {
    private final SqlDB sqlDb;
    private final InfluxDB influxDb;
    private final Books books;

    public Config(ConfigurationSection section) {
        ConfigurationSection sqlDb = section.getConfigurationSection("sqldb");
        if (sqlDb == null) {
            throw new IllegalArgumentException("Database configuration not found");
        }
        ConfigurationSection books = section.getConfigurationSection("books");
        if (books == null) {
            throw new IllegalArgumentException("Books configuration not found");
        }
        ConfigurationSection influxDb = section.getConfigurationSection("influxdb");
        if (influxDb == null) {
            throw new IllegalArgumentException("Influx Db configuration not found");
        }
        this.sqlDb = new SqlDB(sqlDb);
        this.books = new Books(books);
        this.influxDb = new InfluxDB(influxDb);
    }

    public static class Books {
        private final Map<String, Book> books = new HashMap<>();

        public Books(final ConfigurationSection section) {
            for (String key : section.getKeys(false)) {
                this.books.put(key, new Book(section.getConfigurationSection(key)));
            }
        }

        public Book getBook(final String key) {
            return this.books.get(key);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Book {
        private final String title;
        private final String author;
        private final List<String> pages;

        public Book(ConfigurationSection section) {
            this(
                    section.getString("title"),
                    section.getString("author"),
                    section.getStringList("pages")
            );
        }
    }

    @Getter
    public static class SqlDB {
        private final String host;
        private final String port;
        private final String database;
        private final String username;
        private final String password;

        public SqlDB(ConfigurationSection section) {
            this(
                    section.getString("host"),
                    section.getString("port"),
                    section.getString("database"),
                    section.getString("username"),
                    section.getString("password")
            );
        }

        public SqlDB(String host, String port, String database, String username, String password) {
            this.host = host;
            this.port = port;
            this.database = database;
            this.username = username;
            this.password = password;

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
        }
    }

    @Getter
    public static class InfluxDB {
        private final String host;
        private final String port;
        private final String organization;
        private final String token;
        private final String bucket;

        public InfluxDB(ConfigurationSection section) {
            this(
                section.getString("host"),
                section.getString("port"),
                section.getString("organization"),
                section.getString("token"),
                section.getString("bucket")
            );
        }

        public InfluxDB(String host, String port, String organization, String token, String bucket) {
            this.host = host;
            this.port = port;
            this.organization = organization;
            this.token = token;
            this.bucket = bucket;


            if (this.host == null) {
                throw new IllegalArgumentException("Missing required configuration: host");
            }
            if (this.port == null) {
                throw new IllegalArgumentException("Missing required configuration: port");
            }
            if (this.organization == null) {
                throw new IllegalArgumentException("Missing required configuration: organization");
            }
            if (this.token == null) {
                throw new IllegalArgumentException("Missing required configuration: token");
            }
            if (this.bucket == null) {
                throw new IllegalArgumentException("Missing required configuration: bucket");
            }
        }
    }

}
