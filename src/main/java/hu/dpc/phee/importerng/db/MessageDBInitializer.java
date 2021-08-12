package hu.dpc.phee.importerng.db;

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
        String createMessageTableString = "DROP TABLE IF EXISTS MESSAGE; CREATE TABLE MESSAGE"
                + "(KEY bigint, "
                + "VALUETYPE varchar, "
                + "TIMESTAMP bigint, "
                + "RECORDTYPE varchar, "
                + "VALUE varchar, "
                + "INTENT varchar)";

        String createProcessDefinitionTableString = "DROP TABLE IF EXISTS PROCESSDEFINITION; CREATE TABLE PROCESSDEFINITION"
                + "(KEY bigint, "
                + "TIMESTAMP bigint, "
                + "VERSION int, "
                + "RESOURCENAME varchar, "
                + "BPMNPROCESSID varchar, "
                + "PROCESSDEFINITIONKEY varchar)";

        try (Connection con = DriverManager.getConnection(url, username, password);
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(createMessageTableString);
            stmt.executeUpdate(createProcessDefinitionTableString);
            System.out.println("MESSAGE and PROCESSDEFINITION tables are created successfully");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
    }
}