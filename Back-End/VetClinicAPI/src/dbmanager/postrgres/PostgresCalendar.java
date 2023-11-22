package dbmanager.postrgres;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dbmanager.Appointment;
import dbmanager.CalendarManager;
import dbmanager.JsonDatabase;
import dbmanager.NotAvailable;
import dbmanager.OutOfOpenRange;
import dbmanager.PastDate;
import dbmanager.postrgres.PostgresDb.SSLMODE;

public class PostgresCalendar extends CalendarManager{

	private PostgresDb db = null;
	private static PostgresCalendar calendar;
	public static String DATABASE_FILE = "database.json";
	
	public static CalendarManager getCalendarManager (String fileName) throws InstantiationException, IllegalAccessException, JsonParseException, JsonMappingException, IOException {
		if (calendar==null) {
			calendar = new PostgresCalendar(fileName);
		}
		return calendar;
	}
	public PostgresCalendar(String fileName) throws InstantiationException, IllegalAccessException, JsonParseException, JsonMappingException, IOException {
		if (fileName != null) {
			DATABASE_FILE = fileName;
		}
		
		ObjectMapper mapper = new ObjectMapper().
				configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		File file = new File(DATABASE_FILE);
		if (!file.exists()) {
			throw new FileNotFoundException("The file "+DATABASE_FILE+" does not exists");
		}
		JsonDatabase database = mapper.readValue(file, JsonDatabase.class);
	
		db = new PostgresDb(database.getJdbc(), database.getDriver(), database.getHost(), database.getPort(), database.getDatabase(),database.getUser(), database.getPassword(), 
				SSLMODE.REQUIRE, database.getSslFactory()!=null, database.getSslFactory());
	}
	
	public boolean newAppointmen (DateTime dateTime, String pet, String petName) throws NotAvailable, SQLException, OutOfOpenRange, PastDate {
		DateTime today = new DateTime();
		if (dateTime.compareTo(today)<0) {
			throw new PastDate();
		}
		System.out.println(dateTime.getHourOfDay());
		if (dateTime.getHourOfDay()<9 || dateTime.getHourOfDay()>=18) {
			throw new OutOfOpenRange();
		}
		
		Date date = dateTime.toDate();
		Appointment appointment = new Appointment(date, pet, petName);
			
		List<Appointment> appointmenst = db.getAppointment(date);
		if (appointmenst.isEmpty()) {
			return db.insertAppointment(appointment);
		}else {
			throw new NotAvailable();
		}
	}
	
	
}
