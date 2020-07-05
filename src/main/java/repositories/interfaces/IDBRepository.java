package repositories.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDBRepository {
    Connection getConnection() throws SQLException;
}
