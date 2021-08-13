package hu.dpc.phee.importerng.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void main(String args[]) {
        // TODO: move it to config file
        String url = "jdbc:postgresql://localhost:5432/testdb";
        String username = "postgres";
        String password = "postgres";

        // TODO: change this, it RECREATES db!
        String createEventTableString = "DROP TABLE IF EXISTS EVENT; CREATE TABLE EVENT"
                + "(KEY bigint, "
                + "PROCESSDEFINITIONKEY bigint, "
                + "VERSION int, "
                + "TIMESTAMP bigint, "
                + "EVENTTEXT varchar)";
        String createProcessDefinitionTableString = "DROP TABLE IF EXISTS PROCESSDEFINITION; CREATE TABLE PROCESSDEFINITION"
                + "(ID bigint, "
                + "PROCESSDEFINITIONKEY varchar,"
                + "VERSION int, "
                + "BPMNPROCESSID varchar,"
                + "RESOURCENAME varchar)";


        try (Connection con = DriverManager.getConnection(url, username, password);
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(createEventTableString);
            stmt.executeUpdate(createProcessDefinitionTableString);
            System.out.println("EVENT and PROCESSDEFINITION tables are created successfully");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
    }
}