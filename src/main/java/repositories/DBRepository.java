package repositories;

import repositories.interfaces.IDBRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBRepository implements IDBRepository {
    @Override
    public Connection getConnection() throws SQLException {
        String connStr = "jdbc:postgresql://localhost:5432/botDB";
        return DriverManager.getConnection(connStr, "postgres", "0000");
    }
}
