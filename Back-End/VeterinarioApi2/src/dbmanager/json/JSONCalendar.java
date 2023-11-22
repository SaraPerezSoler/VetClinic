package dbmanager.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dbmanager.Appointment;
import dbmanager.CalendarManager;
import dbmanager.NotAvailable;
import dbmanager.OutOfOpenRange;
import dbmanager.PastDate;

public class JSONCalendar extends CalendarManager{

	private static ObjectMapper mapper;
	private static Calendar db;
	private static File file;
	private static JSONCalendar calendar;
	public static String DATABASE_FILE = "calendar.json";
	
	public static CalendarManager getCalendarManager (String fileName) throws InstantiationException, IllegalAccessException, JsonParseException, JsonMappingException, IOException {
		if (calendar==null) {
			calendar = new JSONCalendar(fileName);
		}
		return calendar;
	}
	public JSONCalendar(String fileName) throws InstantiationException, IllegalAccessException, JsonParseException, JsonMappingException, IOException {
		if (fileName != null) {
			DATABASE_FILE = fileName;
		}
		
		mapper = new ObjectMapper().
				configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		file = new File(DATABASE_FILE);
		if (!file.exists()) {
			throw new FileNotFoundException("The file "+DATABASE_FILE+" does not exists");
		}
		db = mapper.readValue(file, Calendar.class);
	}
	
	public boolean newAppointmen (DateTime dateTime, String pet, String petName) throws NotAvailable, SQLException, OutOfOpenRange, PastDate, JsonGenerationException, JsonMappingException, IOException {
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
			
		Appointment appointmenst = db.getAppointment(date);
		if (appointmenst == null) {
			boolean ret = db.insertAppointment(appointment);
			mapper.writeValue(file, db);
			return ret;
		}else {
			throw new NotAvailable();
		}
	}
	
	
}
