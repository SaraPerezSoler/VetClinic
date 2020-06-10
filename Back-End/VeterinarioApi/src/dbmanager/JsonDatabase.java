package dbmanager;

public class JsonDatabase {

	private String jdbc;
	private String driver;
	private String host;
	private int port;
	private String database;
	private String user;
	private String password;
	private String sslFactory;
	public String getJdbc() {
		return jdbc;
	}
	public void setJdbc(String jdbc) {
		this.jdbc = jdbc;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSslFactory() {
		return sslFactory;
	}
	public void setSslFactory(String sslFactory) {
		this.sslFactory = sslFactory;
	}
	
	
}
