package dbmanager.json;

import java.util.Date;
import java.util.Map;

import dbmanager.Appointment;

public class Calendar {

	private Map<Date, Appointment> appointments;
	
	
	public Calendar() {
		// TODO Auto-generated constructor stub
	}

	public Map<Date, Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(Map<Date, Appointment> appointments) {
		this.appointments = appointments;
	}

	public Appointment getAppointment(Date date) {
		return appointments.get(date);
	}

	public boolean insertAppointment(Appointment appointment) {
		appointments.put(appointment.getDate(), appointment);
		return true;
	}
}
