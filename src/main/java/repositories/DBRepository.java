package repositories;

import repositories.interfaces.IDBRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBRepository implements IDBRepository {
    @Override
    public Connection getConnection() throws SQLException {
        String connStr = "jdbc:postgresql://ec2-34-202-88-122.compute-1.amazonaws.com:5432/deilolkg1haoba";
        return DriverManager.getConnection(connStr, "xlwunupaccsujy", "0125c7e91b51e0a005d66536fc8bfb553c102ad1e2167e752764f59f6e3635d4");
    }
}
