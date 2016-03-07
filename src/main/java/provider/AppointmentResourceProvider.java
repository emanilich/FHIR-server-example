package provider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.dstu2.resource.Appointment;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;

// implement interface IResourceProvider
public class AppointmentResourceProvider implements IResourceProvider{
	
	// create hash map for in-memory persistence
	private Map<Long,Appointment> appointments = new HashMap<Long,Appointment>();
	// keeps track of the current appointment ID
	private long globalID = 1L;
	
	// Returns the runtime class of the Appointment object
	@Override
	public Class<Appointment> getResourceType() {
		return Appointment.class;
	}
	
	// create an appointment on server start up
	public AppointmentResourceProvider(){
		long id = globalID++;
		Appointment appointment = new Appointment();
		appointment.setId(Long.toString(id));
		appointment.setComment("comment" + id);
		this.appointments.put(id, appointment);
	}
	
	// receives an id and returns a single appointment
	// example url: GET http://localhost:8080/fhir/Appointment/1
	@Read
	public Appointment readAppointment(@IdParam IdDt theID){
		Appointment appointment = this.appointments.get(theID.getIdPartAsLong());
		return appointment;
	}
	
	// returns all appointments
	// example url: GET http://localhost:8080/fhir/Appointment
	@Search
	public List<Appointment> findAppointments(){
		// convert hash map into a list and return it
		LinkedList<Appointment> list = new LinkedList<Appointment>();
		for (Appointment appointment:this.appointments.values()){
			list.add(appointment);
		}
		return list;
	}
	
	// takes in passed appointment object
	// sets an ID and stores it in the hash map
	// example url: POST http://localhost:8080/fhir/Appointment
	@Create
	public MethodOutcome create(@ResourceParam Appointment appointment){
		System.out.println("================================");
		long id = globalID++;
		appointment.setId(Long.toString(id));
		this.appointments.put(id, appointment);
		MethodOutcome outcome = new MethodOutcome();
		outcome.setResource(appointment);
		outcome.setId(appointment.getId());
		System.out.println(this.appointments.values().size());
		return outcome;
	}
	
}