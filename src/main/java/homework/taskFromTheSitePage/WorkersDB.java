package homework.taskFromTheSitePage;


import java.sql.*;

public class WorkersDB {

    private static Connection connection;
    private static Statement statement;

    public static void main(String[] args) {
        try {
            connect();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS workers ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
                    "first_name TEXT," +
                    "last_name TEXT," +
                    "phone_number TEXT," +
                    "Email TEXT," +
                    "position TEXT," +
                    "payment INTEGER)");

//            addWorker("Lilu", "Dallas", "555-55-55", "fithElement@multipasport.com", "savior", 4500);
//            addWorker("Spiro", "Napolnosrakis", "111-11-11", "funnyNames@here.com", "clown", 3000);
//            addWorker("Vasya", "Progulov", "222-22-22", "imDontWantTo@work.com","cleaner", 1500);

//            deleteWorker("Vasya", "Progulov");

//            updatePaymentOnWorkersDB(4,5500);

            printWorker("Lilu", "Dallas");

            printAllWorkers();



        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addWorker(String firstName, String lastName, String phone,
                                 String email, String position, int payment) throws SQLException {
        statement.executeUpdate(String.format("INSERT INTO workers (first_name, last_name, phone_number, " +
                "Email, position, payment) VALUES('%s', '%s', '%s', '%s', '%s', '%d')",
                firstName, lastName, phone, email, position, payment));
    }

    public static void deleteWorker(String firstName, String lastName) throws SQLException {
        statement.executeUpdate(String.format("DELETE FROM workers WHERE first_name = '%s' AND " +
                "last_name = '%s'", firstName, lastName));
    }

    public static void updatePaymentOnWorkersDB(int id, int newPayment) throws SQLException {
        String upd = String.format("UPDATE workers SET payment = %d where id = %d", newPayment,id);
        statement.executeUpdate(upd);
    }

    public static void printWorker(String firstName, String lastName) throws SQLException {
        ResultSet res = statement.executeQuery(String.format("SELECT * FROM workers WHERE " +
                "first_name = '%s' AND last_name = '%s'", firstName, lastName));
        System.out.println("Data on employee:");
        System.out.format("id:%d | first name:%s | second name:%s | phone:%s | email:%s | position:%s | payment:%s",
                res.getInt(1),
                res.getString("first_name"),res.getString("last_name"),
                res.getString("phone_number"), res.getString("Email"),
                res.getString("position"), res.getString("payment")+ "\n");
    }

    public static void printAllWorkers() throws SQLException {
        ResultSet res = statement.executeQuery("SELECT * FROM workers");
        System.out.println("Data on all employees:");
        while (res.next()){
            System.out.format("id:%d | first name:%s | second name:%s | phone:%s | email:%s | position:%s | payment:%s",
                    res.getInt(1),
                    res.getString("first_name"),res.getString("last_name"),
                    res.getString("phone_number"), res.getString("Email"),
                    res.getString("position"), res.getString("payment")+ "\n");
        }
    }

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:employeesHW2.db");
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
