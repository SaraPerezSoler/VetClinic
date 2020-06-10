package dbmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import dbmanager.PostgresDb.SSLMODE;

public class DbTest {
	
	public static String DATABASE_FILE = "database.json";

	static String [][] appointments = {{"2017-04-01 09:00:00", "0" ,"-"}
	                                  ,{"2017-04-01 10:00:00", "0" ,"-"}
	                                  ,{"2017-04-01 11:00:00", "0" ,"-"}
	                                  ,{"2017-04-01 12:00:00", "0" ,"-"}
	                                  ,{"2017-04-01 13:00:00", "0" ,"-"}
	                                  ,{"2017-04-01 14:00:00", "0" ,"-"}
	                                  ,{"2017-04-01 15:00:00", "0" ,"-"}
	                                  ,{"2017-04-01 16:00:00", "0" ,"-"}
	                                  ,{"2017-04-01 17:00:00", "0" ,"-"}
	                                  ,{"2017-04-01 18:00:00", "0" ,"-"}
	                                  ,{"2017-04-02 09:00:00", "0" ,"-"}
	                                  ,{"2017-04-02 10:00:00", "0" ,"-"}
	                                  ,{"2017-04-02 11:00:00", "0" ,"-"}
	                                  ,{"2017-04-02 12:00:00", "0" ,"-"}
	                                  ,{"2017-04-02 13:00:00", "0" ,"-"}
	                                  ,{"2017-04-02 14:00:00", "0" ,"-"}
	                                  ,{"2017-04-02 15:00:00", "0" ,"-"}
	                                  ,{"2017-04-02 16:00:00", "0" ,"-"}
	                                  ,{"2017-04-02 17:00:00", "0" ,"-"}
	                                  ,{"2017-04-02 18:00:00", "0" ,"-"}
	                                  ,{"2017-04-03 09:00:00", "0" ,"-"}
	                                  ,{"2017-04-03 10:00:00", "0" ,"-"}
	                                  ,{"2017-04-03 11:00:00", "0" ,"-"}
	                                  ,{"2017-04-03 12:00:00", "0" ,"-"}
	                                  ,{"2017-04-03 13:00:00", "0" ,"-"}
	                                  ,{"2017-04-03 14:00:00", "0" ,"-"}
	                                  ,{"2017-04-03 15:00:00", "0" ,"-"}
	                                  ,{"2017-04-03 16:00:00", "0" ,"-"}
	                                  ,{"2017-04-03 17:00:00", "0" ,"-"}
	                                  ,{"2017-04-03 18:00:00", "0" ,"-"}
	                                  ,{"2017-04-04 09:00:00", "0" ,"-"}
	                                  ,{"2017-04-04 10:00:00", "0" ,"-"}
	                                  ,{"2017-04-04 11:00:00", "0" ,"-"}
	                                  ,{"2017-04-04 12:00:00", "0" ,"-"}
	                                  ,{"2017-04-04 13:00:00", "0" ,"-"}
	                                  ,{"2017-04-04 14:00:00", "0" ,"-"}
	                                  ,{"2017-04-04 15:00:00", "0" ,"-"}
	                                  ,{"2017-04-04 16:00:00", "0" ,"-"}
	                                  ,{"2017-04-04 17:00:00", "0" ,"-"}
	                                  ,{"2017-04-04 18:00:00", "0" ,"-"}};
	public static void main(String[] args) throws NumberFormatException, SQLException, InstantiationException, IllegalAccessException, ParseException, JsonParseException, JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper().
				configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		if (args.length > 1) {
			for (int i =0; i < args.length; i++) {
				String s = args[i];
				if (s.equals("-f") || s.equals("--file")) {
					DATABASE_FILE = args[i+1];
				}
			}
		}
		File file = new File(DATABASE_FILE);
		if (!file.exists()) {
			throw new FileNotFoundException("The file "+DATABASE_FILE+" does not exists");
		}
		JsonDatabase database = mapper.readValue(file, JsonDatabase.class);
	
		PostgresDb db = new PostgresDb(database.getJdbc(), database.getDriver(), database.getHost(), database.getPort(), database.getDatabase(),database.getUser(), database.getPassword(), 
				SSLMODE.REQUIRE, database.getSslFactory()!=null, database.getSslFactory());
		
		for (String [] row : appointments) {
			db.insertAppointment(row[0], Integer.parseInt(row[1]), row[2]);
		}
		
		for (String [] row : appointments) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DateTime dateAux = new DateTime(formatter.parse(row[0]).getTime());
			System.out.println(db.getAppointment(dateAux.toDate()));
		}
	}
}
