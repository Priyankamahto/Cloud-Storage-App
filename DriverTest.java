public class DriverTest {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver found in classpath!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver NOT found in classpath!");
            System.out.println("Error: " + e.getMessage());
        }
    }
} 