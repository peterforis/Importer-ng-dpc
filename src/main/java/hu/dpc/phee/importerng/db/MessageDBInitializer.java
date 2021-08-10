package hu.dpc.phee.importerng.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MessageDBInitializer {

    public static void main(String args[]) {
        // TODO: move it to config file
        String url = "jdbc:postgresql://localhost:5432/testdb";
        String username = "postgres";
        String password = "postgres";

        // TODO: change this, it RECREATES db!
        String createString = "DROP TABLE IF EXISTS MESSAGES; CREATE TABLE MESSAGES "
                + "(id bigint, "
                + "VALUETYPE varchar, "
                + "TIMESTAMP bigint, "
                + "RECORDTYPE varchar, "
                + "VALUE varchar, "
                + "INTENT varchar)";

        // Create database connection, then a statement and execute it
        try (Connection con = DriverManager.getConnection(url, username, password);
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(createString);
            System.out.println("MESSAGES table is successfully created");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
    }
}