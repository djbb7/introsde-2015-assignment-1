package marshalling;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import marshalling.generated.HealthProfileType;
import marshalling.generated.PeopleType;
import marshalling.generated.PersonType;

/**
 * Performs marshalling of Java PeopleType objects into XML/JSON files.
 * @author Daniel Bruzual (https://github.com/djbb7)
 *
 */
public class JAXBMarshaller {
	
	private Marshaller marshaller; 
	private marshalling.generated.ObjectFactory factory;
	private PeopleType people;
	
	/**
	 * Set up people database and JAXB objects.
	 */
	public void initialize(){
		try{
			JAXBContext jaxbContext = JAXBContext.newInstance("marshalling.generated");
			marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
			factory = new marshalling.generated.ObjectFactory();
	
			people = factory.createPeopleType();
	
			//initialize DB with sample data
			//Generate people from famous actors
			PersonType person1 = factory.createPersonType();
			person1.setFirstname("George");
			person1.setLastname("Cloney");
			person1.setId(new BigInteger("0001"));
			person1.setBirthdate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(1961, 5, 6)));
	
			PersonType person2 = factory.createPersonType();
			person2.setFirstname("Bradley");
			person2.setLastname("Cooper");
			person2.setId(new BigInteger("0002"));
			person2.setBirthdate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(1975, 1, 5)));
			
			PersonType person3 = factory.createPersonType();
			person3.setFirstname("Tom");
			person3.setLastname("Cruise");
			person3.setId(new BigInteger("0002"));
			person3.setBirthdate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(1962, 7, 3)));
			
			//HealthProfile for the different actors
			HealthProfileType healthProfile1 = factory.createHealthProfileType();
			healthProfile1.setHeight(new BigDecimal("1.80"));
			healthProfile1.setWeight(new BigDecimal("76"));
			healthProfile1.setBmi(new BigDecimal("26.78"));
			healthProfile1.setLastupdate(DatatypeFactory.newInstance().newXMLGregorianCalendar(2013, 3, 7, 12, 43, 00, 0, 0));
			person1.setHealthprofile(healthProfile1);
			
			HealthProfileType healthProfile2 = factory.createHealthProfileType();
			healthProfile2.setHeight(new BigDecimal("1.85"));
			healthProfile2.setWeight(new BigDecimal("80"));
			healthProfile2.setBmi(new BigDecimal("26.78"));
			healthProfile2.setLastupdate(DatatypeFactory.newInstance().newXMLGregorianCalendar(2014, 3, 5, 12, 43, 00, 0, 0));
			person2.setHealthprofile(healthProfile2);
			
			HealthProfileType healthProfile3 = factory.createHealthProfileType();
			healthProfile3.setHeight(new BigDecimal("1.70"));
			healthProfile3.setWeight(new BigDecimal("65"));
			healthProfile3.setBmi(new BigDecimal("26.78"));
			healthProfile3.setLastupdate(DatatypeFactory.newInstance().newXMLGregorianCalendar(2012, 2, 4, 12, 43, 00, 0, 0));
			person3.setHealthprofile(healthProfile3);
	
			people.getPerson().add(person1);
			people.getPerson().add(person2);
			people.getPerson().add(person3);
		}  catch (DatatypeConfigurationException e) {
			System.out.println(e.toString());
		} catch (JAXBException e) {
			System.out.println(e.toString());

		}
	}
	public void generateXMLDocument(File xmlDocument) {
		try {
			JAXBElement<PeopleType> peopleElement = factory.createPeople(people);
			marshaller.marshal(peopleElement,
					new FileOutputStream(xmlDocument));
			marshaller.marshal(peopleElement, System.out);

		} catch (IOException e){
			System.out.println(e.toString());
		} catch (JAXBException e) {
			System.out.println(e.toString());

		}

	}

	public void generateJSONDocument(File jsonDocument){
		try {

			// Jackson Object Mapper 
			ObjectMapper mapper = new ObjectMapper();

			// Adding the Jackson Module to process JAXB annotations
			JaxbAnnotationModule module = new JaxbAnnotationModule();

			// configure as necessary
			mapper.registerModule(module);
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			

			String result = mapper.writeValueAsString(people);
			System.out.println(result);
			mapper.writeValue(jsonDocument, people);

		} catch (IOException e){
			System.out.println(e.toString());
		} 

		
	}

	public static void main(String[] argv) {
		String saveDocument = argv[0];
		
		String command = argv[1];
		if(command == null){
			System.err.println("No command provided. Must pass one of:");
			System.err.println("\tXML: to generate an XML file");
			System.err.println("JSON: to generate a JSON file");
			return;
		}
		JAXBMarshaller jaxbMarshaller = new JAXBMarshaller();
		jaxbMarshaller.initialize();

		if(command.equals("XML")){
			jaxbMarshaller.generateXMLDocument(new File(saveDocument));
		} else if (command.equals("JSON")){
			jaxbMarshaller.generateJSONDocument(new File(saveDocument));
		}
	}
}
