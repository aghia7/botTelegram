package repositories;

import handlers.Language;
import models.User;
import repositories.interfaces.IDBRepository;
import repositories.interfaces.IUserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRepository implements IUserRepository {
    private final IDBRepository dbRepo = new DBRepository();

    @Override
    public Language getUserLanguage(int user_id) {
        String sql = "SELECT language FROM users WHERE id = " + user_id + " LIMIT 1";
        try {
            Statement stmt = dbRepo.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                Language language = Language.convert(rs.getString("language"));
                return language;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public void add(User user) {
        String sql = "INSERT INTO users(id, language) VALUES(?,?)";
        try {
            PreparedStatement stmt = dbRepo.getConnection().prepareStatement(sql);
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getLanguage().getLanguage());
            stmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET language = ? where id = ?";
        try {
            PreparedStatement stmt = dbRepo.getConnection().prepareStatement(sql);
            stmt.setString(1, user.getLanguage().getLanguage());
            stmt.setInt(2, user.getId());
            stmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
