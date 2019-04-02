package homework.chat.authorization;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationService implements Authorization {

    private static Connection connection;
    private static Statement statement;

    public Map<String, String> users = new HashMap<>();

    public AuthorizationService() {
        try {
            connect();
            ResultSet res = statement.executeQuery("SELECT * FROM users");
            while (res.next()) {
                users.put(res.getString("nickname"), res.getString("password"));
            }
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        users.put("john", "123");
//        users.put("valery", "345");
//        users.put("bob", "567");
//        users.put("jenifer", "789");
    }

    @Override
    public boolean authUser(String nickname, String password) {
        String psw = users.get(nickname);
        return psw != null && psw.equals(password);
    }

    public static void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:chatUsers.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
