package repositories;

import repositories.interfaces.IDBRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBRepository implements IDBRepository {
    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(System.getenv("DB_URL"));
    }
}
