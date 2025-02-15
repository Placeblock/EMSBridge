package de.codelix.emsbridge.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.codelix.emsbridge.Config;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class DataSource {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private HikariDataSource ds;

    public DataSource(Config.DB dbConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + dbConfig.getDatabase());
        config.setUsername(dbConfig.getUsername());
        config.setPassword(dbConfig.getPassword());
        this.ds = new HikariDataSource(config);
    }

    public void executeAsync(ExceptionConsumer<Connection> consumer, Consumer<Exception> ex) {
        this.executor.execute(() -> {
            try (Connection con = this.ds.getConnection()) {
                consumer.accept(con);
            } catch (Exception e) {
                ex.accept(e);
            }
        });
    }

    public void execute(ExceptionConsumer<Connection> consumer, Consumer<Exception> ex) {
        try (Connection con = this.ds.getConnection()) {
            consumer.accept(con);
        } catch (Exception e) {
            ex.accept(e);
        }
    }
}
