package marshalling;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import marshalling.generated.PeopleType;
import marshalling.generated.PersonType;

import org.xml.sax.SAXException;

public class JAXBUnMarshaller {
	public void unMarshall(File xmlDocument) {
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance("marshalling.generated");

			Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = schemaFactory.newSchema(new File(
					"xml/people.xsd"));
			unMarshaller.setSchema(schema);
			CustomValidationEventHandler validationEventHandler = new CustomValidationEventHandler();
			unMarshaller.setEventHandler(validationEventHandler);

			@SuppressWarnings("unchecked")
			JAXBElement<PeopleType> peopleElement = (JAXBElement<PeopleType>) unMarshaller
					.unmarshal(xmlDocument);

			PeopleType people = peopleElement.getValue();

			List<PersonType> personList = people.getPerson();
			
			System.out.println ("--------------------------------------------------------------------------------------------------------------------");
			System.out.format("%5s%20s%20s%15s%15s%15s%10s%10s", "ID", "First Name", "Last Name", "Birthdate", "Last Update", "Weight", "Height", "BMI");
			System.out.println ("\n--------------------------------------------------------------------------------------------------------------------");

			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

			for (int i = 0; i < personList.size(); i++) {

				PersonType person = (PersonType) personList.get(i);
				String prettyBirthdate = "";
				String prettyUpdate = "";
				try {
					prettyBirthdate =fmt.format(fmt.parse(person.getBirthdate().toString()));
					prettyUpdate = fmt.format(fmt.parse(person.getHealthprofile().getLastupdate().toString()));
				} catch (ParseException e){
				}
					System.out.println(String.format("%5d%20s%20s%15s%15s%15s%10s%10s", 
						person.getId(), 
						person.getFirstname(), 
						person.getLastname(), 
						prettyBirthdate, 
						prettyUpdate, 
						person.getHealthprofile().getWeight(),
						person.getHealthprofile().getHeight(), 
						person.getHealthprofile().getBmi()));

			}
		} catch (JAXBException e) {
			System.out.println(e.toString());
		} catch (SAXException e) {
			System.out.println(e.toString());
		}
	}

	public static void main(String[] argv) {
		String inputXMLFile = argv[0];
		
		if(inputXMLFile == null){
			System.err.println("No input XML file to unmarshall was specified.");
			return;
		}
		File xmlDocument = new File(inputXMLFile);
		JAXBUnMarshaller jaxbUnmarshaller = new JAXBUnMarshaller();

		jaxbUnmarshaller.unMarshall(xmlDocument);

	}

	class CustomValidationEventHandler implements ValidationEventHandler {
		public boolean handleEvent(ValidationEvent event) {
			if (event.getSeverity() == ValidationEvent.WARNING) {
				return true;
			}
			if ((event.getSeverity() == ValidationEvent.ERROR)
					|| (event.getSeverity() == ValidationEvent.FATAL_ERROR)) {

				System.out.println("Validation Error:" + event.getMessage());

				ValidationEventLocator locator = event.getLocator();
				System.out.println("at line number:" + locator.getLineNumber());
				System.out.println("Unmarshalling Terminated");
				return false;
			}
			return true;
		}

	}
}
