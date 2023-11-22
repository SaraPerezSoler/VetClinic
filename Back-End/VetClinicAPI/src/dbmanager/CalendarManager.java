package dbmanager;

import java.io.IOException;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.JsonMappingException;

public abstract class CalendarManager {

	
	public abstract boolean newAppointmen (DateTime dateTime, String pet, String petName) throws NotAvailable, OutOfOpenRange, PastDate, JsonMappingException, IOException, Exception;
	
	
}
