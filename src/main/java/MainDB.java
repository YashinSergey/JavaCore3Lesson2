import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class MainDB {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement pstmt;

    public static void main(String[] args) {

        try {
            connect();

//            ResultSet rs = stmt.executeQuery("SELECT * FROM students where id > 0");
//            ResultSetMetaData rsmd = rs.getMetaData();
//
//            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
//                System.out.println(rsmd.getColumnName(i) + " " + rsmd.getColumnType(i));
//            }
//
//            while (rs.next()) {
//                System.out.println(rs.getInt(1) + " " + rs.getString("name"));
//            }

//            stmt.executeUpdate("CREATE TABLE students " +
//                    "( id INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    "name TEXT," +
//                    "score TEXT)");




//            connection.setAutoCommit(false);
//
//            long t = System.currentTimeMillis();
//            for (int i = 0; i < 1000; i++) {
//                stmt.addBatch("INSERT INTO students (name, score)\n" +
//                        "values ('unknow', 100)");
//            }
//            stmt.executeBatch();
//
//            connection.setAutoCommit(true);

//            System.out.println(System.currentTimeMillis() - t);



//            pstmt = connection.prepareStatement("INSERT INTO students (name, score)\n" +
//                    "values (?,?)");
//
//            for (int i = 0; i < 10; i++) {
//                pstmt.setString(1, "Bob" + (i));
//                pstmt.setString(2, " " + (i * 10));
//                pstmt.addBatch();
//            }
//            pstmt.executeBatch();


//            stmt.executeUpdate("INSERT INTO students (name, score)\n" +
//                    "values ('Bob1', 10)");
//
//            Savepoint sp = connection.setSavepoint();
//
//
//            stmt.executeUpdate("INSERT INTO students (name, score)\n" +
//                    "values ('Bob2', 20)");
//
//            connection.rollback(sp);
//            connection.setAutoCommit(true);
//
//            stmt.executeUpdate("INSERT INTO students (name, score)\n" +
//                    "values ('Bob3', 30)");

            try {
                readFile();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // DELETE - удаляет
            // DROP - удаляет таблицу
            // TRUNCATE - удаляет (транзакция)

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // work to db

    }


    public static void readFile() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("D:\\Users\\Artem\\Desktop\\test\\update.txt");
        Scanner scanner = new Scanner(fileInputStream);

        while (scanner.hasNext()) {
            String[] mass = scanner.nextLine().split(" ");
            try {
                updateDB(mass[0], mass[1]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateDB(String id, String newValue) throws SQLException {
        String sql = String.format("UPDATE students SET score = %s where id = %s", newValue, id);
        stmt.executeUpdate(sql);
    }

    public static void connect() throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:usersDB.db");
        stmt = connection.createStatement();

    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
