package Lesson_8.server;

import javafx.util.Pair;

import java.sql.*;
import java.util.HashSet;

public class AuthService {
    private static Connection connection;
    private static Statement stmt;

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            stmt = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUser(String login, String pass, String nick) {
        try {
            String query = "INSERT INTO users (login, password, nickname) VALUES (?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, login);
            ps.setInt(2, pass.hashCode());
            ps.setString(3, nick);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Pair<Integer, String> getNickByLoginAndPass(String login, String pass) {
        try {
            ResultSet rs = stmt.executeQuery("select id, nickname, password from users where login = '" + login + "'");
            int myHash = pass.hashCode(); // 137
            if (rs.next()) {
                String nick = rs.getString("nickname");
                int dbHash  = rs.getInt("password");
                int id      = rs.getInt("id");
                if (myHash == dbHash) {
                    return new Pair<Integer, String>(id, nick);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void putNickToBlackList(int _id, String _blockedUser){
        try {
            ResultSet rs      = stmt.executeQuery("select id from users where nickname = '" + _blockedUser + "'");
            int blockedUserId = -1;
            if(rs.next()){
                blockedUserId = rs.getInt("id");
            }
            String query = "insert into black_list  VALUES (?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, _id);
            ps.setInt(2, blockedUserId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static HashSet<String> loadBlacklist(int _userId) {
        String query = "select u.nickname\n" +
                "  from users u \n" +
                "  join black_list b\n" +
                "    on b.blocked_user_id = u.id\n" +
                "   and b.user_id = ?";
        HashSet<String> blackList = new HashSet<>();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, _userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                blackList.add(rs.getString("nickname"));
            }

            return blackList;

        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }
}
