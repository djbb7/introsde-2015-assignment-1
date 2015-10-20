package xpath;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.HealthProfile;
import model.Person;
import dao.PeopleStore;

public class PersonWriter {  	
	public static PeopleStore people = new PeopleStore();

	public static void initializeDB() {
		Person p;
		HealthProfile hp;
		
		hp = new HealthProfile(82, 1.80);
		p = new Person(1L, "Francesco", "Totti", "1984-09-20", hp);
		people.getData().add(p);
		hp = new HealthProfile(105, 1.73);
		p = new Person(2L, "Giuseppe", "Rossi", "1984-02-10", hp);
		people.getData().add(p);
		hp = new HealthProfile(68, 1.77);
		p = new Person(3L, "Andrea", "Pirlo", "1980-12-05", hp);
		people.getData().add(p);
		hp = new HealthProfile(91, 1.73);
		p = new Person(4L, "Carlos", "Tévez", "1994-01-27", hp);
		people.getData().add(p);
		hp = new HealthProfile(82, 1.84);
		p = new Person(5L, "Gonzalo", "Higuaín", "1996-03-07", hp);
		people.getData().add(p);
		hp = new HealthProfile(60, 1.72);
		p = new Person(6L, "Robson", "de Souza", "1987-07-30", hp);
		people.getData().add(p);
		hp = new HealthProfile(74, 1.81);
		p = new Person(7L, "Adem", "Ljajić", "1964-11-21", hp);
		people.getData().add(p);
		hp = new HealthProfile(78, 1.88);
		p = new Person(8L, "Víctor", "Ibarbo", "1944-05-04", hp);
		people.getData().add(p);
		hp = new HealthProfile(86, 1.91);
		p = new Person(9L, "Angelo", "Ogbonna", "1976-06-15", hp);
		people.getData().add(p);
		hp = new HealthProfile(75, 1.84);
		p = new Person(10L, "M'Baye", "Niang", "1988-08-28", hp);
		people.getData().add(p);
		hp = new HealthProfile(77, 1.84);
		p = new Person(11L, "Maicon", "Douglas Sisenando", "1975-04-20", hp);
		people.getData().add(p);
		hp = new HealthProfile(59, 1.63);
		p = new Person(12L, "Lorenzo", "Insigne", "1991-08-03", hp);
		people.getData().add(p);
		hp = new HealthProfile(88, 1.89);
		p = new Person(13L, "Mario", "Gomez", "1980-12-20", hp);
		people.getData().add(p);
		hp = new HealthProfile(79, 1.83);
		p = new Person(14L, "Marek", "Hamšik", "1985-08-24", hp);
		people.getData().add(p);
		hp = new HealthProfile(68, 1.80);
		p = new Person(15L, "Miralem", "Pjanić", "1993-02-15", hp);
		people.getData().add(p);
		hp = new HealthProfile(66, 1.76);
		p = new Person(16L, "Juan", "Cuadrado", "1987-04-16", hp);
		people.getData().add(p);
		hp = new HealthProfile(90, 1.95);
		p = new Person(17L, "Fernando", "Llorente Torres", "1983-05-09", hp);
		people.getData().add(p);
		hp = new HealthProfile(65, 1.80);
		p = new Person(18L, "Luiz", "Frello Filho Jorge", "1983-07-21", hp);
		people.getData().add(p);
		hp = new HealthProfile(73, 1.80);
		p = new Person(19L, "Ignazio", "Abate", "1987-04-21", hp);
		people.getData().add(p);
		hp = new HealthProfile(74, 1.88);
		p = new Person(20L, "Mattia", "Perin", "1936-02-26", hp);
		people.getData().add(p);
	}	

	public static void main(String[] args)  {
		
		if(args.length<1){
			System.err.println("Please specify the filename with which to save the resulting XML.");
			return;
		}
		
		initializeDB();
		
		try {
			JAXBContext jc = JAXBContext.newInstance(PeopleStore.class);
	        Marshaller m = jc.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        
	        m.marshal(people,new File(args[0])); // marshalling into a file
	        System.out.println("Generating XML file to: "+args[0]);
		} catch (JAXBException e){
			System.out.println(e);
		}
    }
}
