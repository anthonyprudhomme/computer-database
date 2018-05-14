package persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Singleton
public class JdbcConnection {

	private static JdbcConnection instance = null;
	private Connection connection;

	// Called only once
	private JdbcConnection(){
		String url ="jdbc:mysql://localhost/computer-database-db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";

		String username = "admincdb";
		String password = "qwerty1234";

		System.out.println("Connecting database...");

		try {
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot connect the database!", e);
		}
	}

	public static JdbcConnection getInstance() {
		if(instance == null) {
			instance = new JdbcConnection();
		}
		return instance;
	}
	
	public static Connection getConnection(){
		return JdbcConnection.getInstance().connection;
	}

}
