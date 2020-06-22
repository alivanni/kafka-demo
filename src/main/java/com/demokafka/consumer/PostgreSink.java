package com.demokafka.consumer;

import com.demokafka.model.MetricEvent;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSink {

    private final Logger logger = Logger.getLogger(PostgreSink.class.getName());

    private String uri;
    private String user;
    private String password;


    public PostgreSink(String uri, String user, String password) {
        this.uri = uri;
        this.user = user;
        this.password = password;

        createTables();
    }

    private void createTables() {
        String query = "CREATE TABLE IF NOT EXISTS metrics (\n" +
                "    id serial PRIMARY KEY,\n" +
                "    type VARCHAR(255),\n" +
                "    value VARCHAR(255),\n" +
                "    timestamp int\n" +
                ");\n";

        try (Connection connection = DriverManager.getConnection(this.uri, this.user, this.password);
             Statement statement = connection.createStatement()) {

            statement.execute(query);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error during creating metrics table", ex);
        }
    }

    public void storeEvent(MetricEvent metricEvent) {
        String query = "INSERT INTO metrics(type, value, timestamp) VALUES(?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(this.uri, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, metricEvent.getMetricType().name());
            preparedStatement.setString(2, metricEvent.getValue());
            preparedStatement.setLong(3, metricEvent.getTimestamp());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "Error during inserting metrics event", ex);
        }
    }
}
