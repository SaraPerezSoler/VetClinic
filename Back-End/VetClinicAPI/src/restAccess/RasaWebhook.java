package restAccess;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Path("/Rasa")
public class RasaWebhook implements Webhook{
	@GET
	@Path("hello/{name}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String sayHello(@PathParam("name") String name) {
		return "Hello " + name + " Rasa";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, MediaType.TEXT_PLAIN })
	public Response editProject(@Context ServletContext context, InputStream incomingData) throws JsonParseException, JsonMappingException, IOException {
		String data = readIncomingData(incomingData);

		System.out.println(data);
		JSONObject object = new JSONObject(data);
		
		System.out.println(object);
		
		try {
			return processRequest(context, object);
		} catch (Exception e) {

			return javax.ws.rs.core.Response.ok(e.getMessage(), MediaType.TEXT_PLAIN)
					.build();
		}
	}

	private Response processRequest(ServletContext context, JSONObject object) throws Exception {
			
		String dateString = getParameter(DATE_PARAMETER, object);
		String time = getParameter(TIME_PARAMETER, object);
		String pet = getParameter(PET_PARAMETER, object);
		String petName = getParameter(PET_NAME_PARAMETER, object);
		
		System.out.println("DateTime: "+dateString +" "+time);
		
		//SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		
		DateTime dateAux = parserDate(dateString);
		DateTime timeAux = parserTime(time);
		
		String text = makeAnAppointment(dateAux, timeAux, pet, petName);
		
		return Response.ok(text, MediaType.TEXT_PLAIN).build();
	}
	
	private String getParameter (String parameterName, JSONObject object) throws Exception {
		if (!object.has(parameterName)) {
			throw new Exception("This intent doesn't has the " + parameterName);
		}
		return  object.getString(parameterName);
	}
	
	private DateTime parserDate(String dateString) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			
			DateTime dateAux = new DateTime(dateFormatter.parse(dateString).getTime());
			return dateAux;
		}catch (Exception e) {
			if (dateString.equalsIgnoreCase("today")) {
				DateTime dateAux = new DateTime();
				return dateAux;
			}else if (dateString.equalsIgnoreCase("tomorrow")) {
				Date date = new Date();
			    Date tomorrow = new Date(date.getTime() + 24 * 60 * 60 * 1000);
			    DateTime dateAux = new DateTime(tomorrow.getTime());
				return dateAux;
			}else if (dateString.equalsIgnoreCase("Monday")){
				LocalDate ld = LocalDate.now();
				System.out.println(ld);
				ld = ld.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
				System.out.println(ld);
				DateTime dateAux = new DateTime(ld.toEpochDay());
				return dateAux;
			}else if (dateString.equalsIgnoreCase("Tuesday")){
				LocalDate ld = LocalDate.now();
				System.out.println(ld);
				ld = ld.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
				System.out.println(ld);
				DateTime dateAux = new DateTime(ld.toEpochDay());
				return dateAux;
			}else if (dateString.equalsIgnoreCase("Wednesday")){
				LocalDate ld = LocalDate.now();
				System.out.println(ld);
				ld = ld.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
				System.out.println(ld);
				DateTime dateAux = new DateTime(ld.toEpochDay());
				return dateAux;
			}else if (dateString.equalsIgnoreCase("thursday")){
				LocalDate ld = LocalDate.now();
				System.out.println(ld);
				ld = ld.with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
				System.out.println(ld);
				DateTime dateAux = new DateTime(ld.toEpochDay());
				return dateAux;
			}else if (dateString.equalsIgnoreCase("friday")){
				LocalDate ld = LocalDate.now();
				System.out.println(ld);
				ld = ld.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
				System.out.println(ld);
				DateTime dateAux = new DateTime(ld.toEpochDay());
				return dateAux;
			}else if (dateString.equalsIgnoreCase("saturday")){
				LocalDate ld = LocalDate.now();
				System.out.println(ld);
				ld = ld.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
				System.out.println(ld);
				DateTime dateAux = new DateTime(ld.toEpochDay());
				return dateAux;
			}else if (dateString.equalsIgnoreCase("Sunday")){
				LocalDate ld = LocalDate.now();
				System.out.println(ld);
				ld = ld.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
				System.out.println(ld);
				DateTime dateAux = new DateTime(ld.toEpochDay());
				return dateAux;
			}else {
				DateTime dateAux = new DateTime();
				return dateAux;
			}
		}
	}
	
	private DateTime parserTime(String time) throws ParseException {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			
			DateTime dateAux = new DateTime(dateFormatter.parse(time).getTime());
			return dateAux;
		}catch (Exception e) {
			try {
				SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");
				
				DateTime dateAux = new DateTime(dateFormatter.parse(time).getTime());
				return dateAux;
			}catch (Exception e1) {
				SimpleDateFormat dateFormatter = new SimpleDateFormat("HH");
				
				DateTime dateAux = new DateTime(dateFormatter.parse(time).getTime());
				return dateAux;
			}
		}
	}

}
