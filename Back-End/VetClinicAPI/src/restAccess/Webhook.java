package restAccess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dbmanager.CalendarManager;
import dbmanager.NotAvailable;
import dbmanager.OutOfOpenRange;
import dbmanager.PastDate;
import dbmanager.json.JSONCalendar;

public interface Webhook {
	
	public static final String TIME_PARAMETER = "time";
	public static final String DATE_PARAMETER = "date";
	public static final String PET_PARAMETER = "pet";
	public static final String PET_NAME_PARAMETER = "petName";
	
	default String readIncomingData(InputStream incomingData) {
		String text = "";
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				text += line + "\n";
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
	
	default String makeAnAppointment(DateTime dateAux, DateTime timeAux, String pet, String petName) throws InstantiationException, IllegalAccessException, SQLException, JsonParseException, JsonMappingException, IOException {
		
		DateTime date = new DateTime(dateAux.getYear(), dateAux.getMonthOfYear(), dateAux.getDayOfMonth(), timeAux.getHourOfDay(), timeAux.getMinuteOfHour());
		
		String text = "";
		String hourString = date.getHourOfDay()+"";
		if (date.getHourOfDay()<10) {
			hourString="0"+hourString;
		}
		String minString = date.getMinuteOfHour()+"";
		if (date.getMinuteOfHour()<10) {
			minString= "0"+minString;
		}
		try {
			if (JSONCalendar.getCalendarManager(null).newAppointmen(date, pet, petName)) {
				text = "Got it. I have your appointment scheduled on " + date.getDayOfMonth() + "/" + date.getMonthOfYear()
						+ "/" + date.getYear() + " at " + hourString + ":" + minString
						+ " for your " + pet +" "+petName+ ".";
			}else {
				text = "There was a problem making the appointment. Try it again later";
			}
		} catch (NotAvailable e) {
			text = "The appointment on " + date.getDayOfMonth() + "/" + date.getMonthOfYear()
			+ "/" + date.getYear() + " at " + hourString + ":" + minString + " is not available. Try with other date";
		} catch (OutOfOpenRange e) {
			text = "We are closed at "+hourString + ":" + minString+". Try between 9:00 and 18:00";
		}catch (PastDate e) {
			text = "I can't schedule an appointment on a past date";
		}catch (Exception e) {
			text = "An error occurred while processing your request, please try again later. ";
		}

		
		return text;
	}
}
