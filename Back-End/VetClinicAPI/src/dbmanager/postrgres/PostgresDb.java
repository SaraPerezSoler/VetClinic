package dbmanager.postrgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dbmanager.Appointment;

public class PostgresDb {

	private final String insert = "insert into appointments (date_time, pet, petName) values (?, ?, ?)";
	private final String select = "SELECT * FROM appointments WHERE date_time = ?";

	public enum SSLMODE {
		DISABLE("disable"), ALLOW("allow"), PREFER("prefer"), REQUIRE("require"), VERIFYCA("verify-ca"),
		VERIFYFULL("verify-full");

		private String name;

		private SSLMODE(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	Connection con = null;


	public PostgresDb(String jdbc, String driver, String host, int port, String database, String user, String password, SSLMODE sslmode, boolean ssl, String sslFactory)
			throws InstantiationException, IllegalAccessException {
		try {
			String sslText="";
			if (ssl) {
				sslText ="&ssl=true&sslfactory="+sslFactory;
			}else {
				sslText = "&ssl=false";
			}
			String url = jdbc+"://" + host + ":" + port + "/" + database + "?sslmode=" + sslmode
					+ sslText;
			
			System.out.println(url);
			Class.forName(driver).newInstance();
			con = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException ex) {
			System.out.println("Error al registrar el driver: " + ex);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean insertAppointment(Appointment appointment) throws SQLException {
		PreparedStatement pstm = con.prepareStatement(insert);
		Timestamp date1= new Timestamp(appointment.getDate().getTime());
		pstm.setTimestamp(1, date1);
		pstm.setString(2, appointment.getPet());
		pstm.setString(3, appointment.getPetName());

		if (!pstm.execute() && pstm.getUpdateCount() == 1) {
			return true;
		}
		return false;

	}

	public boolean insertAppointment(String date, int availability, String pet) throws SQLException, ParseException {
		PreparedStatement pstm = con.prepareStatement(insert);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp date1= new Timestamp(format.parse(date).getTime());
		pstm.setTimestamp(1, date1);
		pstm.setInt(2, availability);
		pstm.setString(3, pet);

		if (!pstm.execute() && pstm.getUpdateCount() == 1) {
			return true;
		}
		return false;

	}

	public List<Appointment> getAppointment(Date date) throws SQLException {
		PreparedStatement pstm = con.prepareStatement(select);
		Timestamp date1= new Timestamp(date.getTime());
		pstm.setTimestamp(1, date1);
		ResultSet rs = pstm.executeQuery();
		List<Appointment> appointments = new ArrayList<Appointment>();
		while(rs.next()) {
			Appointment ap = new Appointment();
			ap.setDate(rs.getTimestamp("date_time"));
			ap.setPet(rs.getString("pet"));
			ap.setPetName(rs.getString("petname"));
			appointments.add(ap);
		}
		return appointments;
	}

}
