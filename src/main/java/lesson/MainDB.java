import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class MainDB {

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement prStatement;

    public static void main(String[] args) {
        try {
            connect();

//            ResultSet res = statement.executeQuery("SELECT * FROM students WHERE id > 1");
//            ResultSetMetaData resmd = res.getMetaData();
//            System.out.println(resmd.getColumnName(1) + " " + resmd.getColumnName(2) + " " +
//                    resmd.getColumnName(3));
//
//            for (int i = 1; i <= resmd.getColumnCount(); i++) {
//                System.out.println(resmd.getColumnName(i) + " " + resmd.getColumnTypeName(i));
//            }
//
//            while (res.next()){
//                System.out.println(String.format("%d %s %d",res.getInt(1),
//                        res.getString("name"), res.getInt("score")));
//            }

//            int update = statement.executeUpdate("INSERT INTO students (name, score) VALUES('bob5', '250')");
//            System.out.println(update); // Выводит на экран количество изменений, внесённых в БД
//            int delete = statement.executeUpdate("DELETE FROM students WHERE id > 3");
//            System.out.println(delete);
//            int drop = statement.executeUpdate("DROP TABLE IF EXISTS students");

//            statement.executeUpdate("CREATE TABLE IF NOT EXISTS  students " +
//                    "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
//                    "name TEXT, " +
//                    "score TEXT)");


//            connection.setAutoCommit(false);
////            long t = System.currentTimeMillis();
////            for (int i = 0; i < 1000; i++) {
//////                statement.executeUpdate("INSERT INTO students (name, score)" +
//////                        "VALUES('LiluDalas', 'multiPasport')");
////                statement.addBatch("INSERT INTO students (name, score)" +
////                        "VALUES('LiluDalas', 'multiPasport')");
////            }
////            statement.executeBatch();
////            connection.setAutoCommit(true);
////            System.out.println(System.currentTimeMillis() - t);

//            prStatement = connection.prepareStatement("INSERT INTO students (name, score)" +
//                            "VALUES(?,?)");
//
//            for (int i = 0; i < 10; i++) {
//                prStatement.setString(1, "Bob" + i);
//                prStatement.setString(2,"" + (i * 10));
//                prStatement.addBatch();
//            }
//            prStatement.executeBatch();


//            statement.executeUpdate("INSERT INTO students (name, score)" +
//                            "VALUES('Bob1',10)");
//
//            Savepoint savepoint = connection.setSavepoint();
//
//            statement.executeUpdate("INSERT INTO students (name, score)" +
//                            "VALUES('Bob2',20)");
//
//            connection.rollback(savepoint); // при вызове Savepoint() отключается AutoCommit
//            connection.setAutoCommit(true);
//
//            statement.executeUpdate("INSERT INTO students (name, score)" +
//                            "VALUES('Bob3',30)");

            readFile();


            // DELETE - удаляет наполнение таблицы (очищает таблицу)
            // DROP - удаляет таблицу и её элементы
            // TRUNCATE - удаляет наполнение таблицы (одной транзакцией) // в sqlite нет этой команды

        } catch (ClassNotFoundException | SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void readFile() throws FileNotFoundException, SQLException {
            FileInputStream fileInputStream = new FileInputStream("F:\\Java\\test.txt");
        Scanner scanner = new Scanner(fileInputStream);
        while (scanner.hasNext()){
            String[] arr = scanner.nextLine().split(" ");
            updateDB(arr[0],arr[1]);
        }
    }

    public static void updateDB(String id, String newValue) throws SQLException {
        String str = String.format("UPDATE students SET score = %s where id = %s", newValue,id);
        statement.executeUpdate(str);
    }


    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:usersDB.db");
        statement = connection.createStatement();

    }

    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
