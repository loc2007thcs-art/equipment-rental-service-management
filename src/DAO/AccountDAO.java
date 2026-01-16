package DAO;
import java.sql.Connection;
import java.sql.*;
import Entity.Account;
public class AccountDAO {
    public boolean checkUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM Account WHERE Username = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean insertAccount(Account acc) {
        String sql ="INSERT INTO Account (FullName, Username, Password) VALUES(?,?,?)";
        try(Connection con=Database.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, acc.getFullName());
            ps.setString(2, acc.getUserName());
            String hashedPassword = PasswordHasher.hashPassword(acc.getPassword());
            ps.setString(3, hashedPassword);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return false;
    }
    public Account login(String user, String pass){
        String sql = "SELECT * FROM Account WHERE Username = ? AND Password = ?";
        try(Connection con = Database.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, user);
            String hashedPassword = PasswordHasher.hashPassword(pass);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Account acc = new Account();
                acc.setFullName(rs.getString("FullName"));
                acc.setUserName(rs.getString("Username"));
            return acc;
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean updatePassword(String username, String newPassword) {
    String sql = "UPDATE Account SET Password = ? WHERE Username = ?";
    try (Connection con = Database.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        String hashedPassword = PasswordHasher.hashPassword(newPassword);
        ps.setString(1, hashedPassword);
        ps.setString(2, username);
        return ps.executeUpdate() > 0;  
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
    }        
}

