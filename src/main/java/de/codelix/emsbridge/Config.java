package de.codelix.emsbridge;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class Config {
    private final DB db;
    private final Books books;
    private final LocalDateTime projectStart;

    public Config(ConfigurationSection section) {
        ConfigurationSection db = section.getConfigurationSection("db");
        if (db == null) {
            throw new IllegalArgumentException("Database configuration not found");
        }
        ConfigurationSection books = section.getConfigurationSection("books");
        if (books == null) {
            throw new IllegalArgumentException("Books configuration not found");
        }
        this.db = new DB(db);
        this.books = new Books(books);
        if (!section.contains("project-start")) {
            throw new IllegalArgumentException("Project start not found in config");
        }
        String serializedProjectStart = section.getString("project-start");
        if (serializedProjectStart == null) {
            throw new IllegalArgumentException("Project start not found in config");
        }
        this.projectStart = LocalDateTime.parse(serializedProjectStart, DateTimeFormatter.ISO_DATE_TIME);
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
    public static class DB {
        private final String host;
        private final String port;
        private final String database;
        private final String username;
        private final String password;

        public DB(ConfigurationSection section) {
            this(
                    section.getString("host"),
                    section.getString("port"),
                    section.getString("database"),
                    section.getString("username"),
                    section.getString("password")
            );
        }

        public DB(String host, String port, String database, String username, String password) {
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

}
