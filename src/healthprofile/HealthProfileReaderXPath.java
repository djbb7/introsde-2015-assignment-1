package healthprofile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Reads people from and XML file and performs operations on the dataset 
 * by traversing the XML tree with XPATH.
 * @author Daniel Bruzual (https://github.com/djbb7
 *
 */
public class HealthProfileReaderXPath {  	

	Document doc;
	XPath xpath;
	public static String XML_LOCATION;

	public HealthProfileReaderXPath(){
		//For proper parsing of decimal numbers
		Locale.setDefault(Locale.US);
	}

	/**
	 * Parse XML document containing people database
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void loadXML() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		doc = builder.parse(XML_LOCATION);

		//creating xpath object
		getXPathObj();
	}

	/**
	 * @return XPath object to traverse the XML file
	 */
	public XPath getXPathObj() {
		XPathFactory factory = XPathFactory.newInstance();
		xpath = factory.newXPath();
		return xpath;
	}

	/**
	 * Find a person by full name.
	 * @param firstname
	 * @param lastname
	 * @return The XML node corresponding to the person, or null if not found.
	 * @throws XPathExpressionException
	 */
	public Node getPersonByFullname(String firstname, String lastname) throws XPathExpressionException {
		XPathExpression expr = xpath.compile("/people/person[firstname='" + firstname + "' and lastname='"+lastname+"']");
		Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
		return node;
	}

	/**
	 * Find a person's Health Profile.
	 * @param id
	 * @return The XML node corresponding to the Health Profile, or null if not found.
	 * @throws XPathExpressionException
	 */
	public Node findHealthProfileByPersonId(int id) throws XPathExpressionException{
		XPathExpression expr = xpath.compile("/people/person[@id=" + id+"]/healthprofile");
		Node hProfile = (Node) expr.evaluate(doc, XPathConstants.NODE);
		return hProfile;
	}

	/**
	 * Find a person's full name
	 * @param id
	 * @return A String containing the person's full name in <lastname, firstname> format, or null if not found.
	 * @throws XPathExpressionException
	 */
	public String getFullnameById (int id) throws XPathExpressionException{
		XPathExpression expr = xpath.compile("//person[@id="+id+"]/firstname | //person[@id="+id+"]/lastname");
		NodeList names = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		String firstname=null, lastname=null;
		for(int j=0; j<names.getLength(); j++){
			Node name = names.item(j);
			if(name.getNodeName().equals("firstname")){
				firstname = name.getTextContent(); 
			} else if (name.getNodeName().equals("lastname")){
				lastname = name.getTextContent();
			}
		}
		if(firstname==null && lastname==null)
			return null;
		
		return lastname+", "+firstname;
	}

	/**
	 * Get the weight of a person by id.
	 * @param id
	 * @return The weight, or null if not found.
	 * @throws XPathExpressionException
	 */
	public Double getWeight(int id) throws XPathExpressionException{
		XPathExpression expr = xpath.compile("//person[@id="+id+"]/weight");
		String res = (String) expr.evaluate(doc, XPathConstants.STRING);
		if(res == null) 
			return null;
		return Double.valueOf(res);
	}

	/**
	 * Get the weight of a person by full name.
	 * @param firstname
	 * @param lastname
	 * @return The height, or null if not found.
	 * @throws XPathExpressionException
	 */
	public Double getWeight(String firstname, String lastname) throws XPathExpressionException{
		XPathExpression expr = xpath.compile("//person[firstname='"+firstname+"' and lastname='"+lastname+"']/weight");
		String res = (String) expr.evaluate(doc, XPathConstants.STRING);
		return Double.valueOf(res);
	}

	/**
	 * Get the height of a person by id.
	 * @param id
	 * @return The weight, or null if not found.
	 * @throws XPathExpressionException
	 */
	public Double getHeight(int id) throws XPathExpressionException{
		XPathExpression expr = xpath.compile("//person[@id="+id+"]/weight");
		String res = (String) expr.evaluate(doc, XPathConstants.STRING);
		return Double.valueOf(res);
	}

	/**
	 * Get the height of a person by full name.
	 * @param firstname
	 * @param lastname
	 * @return The height, or null if not found.
	 * @throws XPathExpressionException
	 */
	public Double getHeight(String firstname, String lastname) throws XPathExpressionException{
		XPathExpression expr = xpath.compile("//person[firstname='"+firstname+"' and lastname='"+lastname+"']/weight");
		String res = (String) expr.evaluate(doc, XPathConstants.STRING);
		return Double.valueOf(res);
	}


	/**
	 * Find all the people who's weight satisfies the condition.
	 * @param op Comparison operator: '<', '>', '='
	 * @param weight Value to compare
	 * @return All the matching people, or null if no match.
	 * @throws XPathExpressionException
	 */
	public NodeList getPersonsByWeight(String op, Double weight) throws XPathExpressionException {
		//do the actual searching now
		XPathExpression expr = xpath.compile(String.format("/people/person[./healthprofile/weight %s %.2f]",op,weight.doubleValue()));
		NodeList people = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		return people;

	}

	/**
	 * Find all people in the database
	 * @return
	 * @throws XPathExpressionException
	 */
	public NodeList getAllPeople() throws XPathExpressionException {
		XPathExpression expr = xpath.compile("//person");
		return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	}


	/**
	 * Pretty print, in table format, all the people in the database.
	 */
	public void printAllPeople(){
		printTableHeader();
		try {
			NodeList persons = getAllPeople();
			printPeople(persons);
		} catch (XPathExpressionException e){
			System.err.println("Error printing all persons");
		}
	}

	/**
	 * Print table headers for pretty print
	 */
	private void printTableHeader(){
		System.out.println ("--------------------------------------------------------------------------------------------------------------------");
		System.out.format("%5s%20s%20s%15s%15s%15s%10s%10s", "ID", "First Name", "Last Name", "Birthdate", "Last Update", "Weight", "Height", "BMI");
		System.out.println ("\n--------------------------------------------------------------------------------------------------------------------");
	}
	
	/**
	 * Print contents of table
	 */
	private void printPeople(NodeList people){
		for(int k=0; k<people.getLength(); k++){
			Node person = people.item(k);
			printPerson(person);
		}
	}

	/**
	 * Print a person as a row with his/her details.
	 * @param person
	 */
	private void printPerson(Node person){
		String id="", first="", last="", dob="", update="", bmi="", weight="", height="";
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

		NamedNodeMap attr = person.getAttributes();
		if(attr != null){
			id = attr.getNamedItem("id").getTextContent();
		}
		NodeList personDetails = person.getChildNodes();
		for(int j=0; j<personDetails.getLength(); j++){
			Node detail = personDetails.item(j);
			if (detail.getNodeName().equals("firstname")){
				first = detail.getTextContent();
			} else if (detail.getNodeName().equals("lastname")){
				last = detail.getTextContent();
			} else if (detail.getNodeName().equals("birthdate")){
				try {
					dob = fmt.format(fmt.parse(detail.getTextContent()));
				} catch (ParseException e) {
					
				}
			}else if (detail.getNodeName().equals("healthprofile")){
				NodeList hpDetails = detail.getChildNodes();
				for(int k=0; k<hpDetails.getLength(); k++){
					Node hpDetail = hpDetails.item(k);
					if(hpDetail.getNodeName().equals("lastupdate")){
						try {
							update = fmt.format(fmt.parse(hpDetail.getTextContent()));
						} catch (ParseException e) {
							
						}
					} else if(hpDetail.getNodeName().equals("weight")){
						weight = hpDetail.getTextContent();
					} else if(hpDetail.getNodeName().equals("height")){
						height = hpDetail.getTextContent();
					} else if(hpDetail.getNodeName().equals("bmi")){
						bmi = hpDetail.getTextContent();
					}
				}
			}
		}
		System.out.println(String.format("%5s%20s%20s%15s%15s%15s%10s%10s", id, first, last, dob, update, weight, height, bmi.substring(0, bmi.indexOf(".")+3)));
	}

	/**
	 * Print all the people who match the condition
	 * @param query Represents a condition for comparing weight (e.g. '<90').
	 */
	public void printPeopleByWeight(String query){
		String op = query.substring(0, 1);
		if(!op.equals(">") && !op.equals("<") && !op.equals("=")) {
			System.out.println(String.format("Invalid operator. Must be one of <, >, =. Found '%s'.",op));
			return;
		} 

		//get the comparisson value
		String qWeight = query.substring(1).trim();
		Double weight = Double.valueOf(qWeight);

		if(weight == null){
			System.out.println("Invalid parameter. Was expecting a number, encountered: '"+qWeight+"'");
			return;
		}

		try {
			System.out.println(String.format("People with weight%s%.2fkg",op,weight));

			printTableHeader();
			
			NodeList people = getPersonsByWeight(op, weight);
			printPeople(people);
		} catch (XPathExpressionException e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Print the health profile for a given person.
	 * @param idPerson
	 */
	public void printHealthProfile(int idPerson){
		String update="", weight="", height="", bmi="";
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

		try {
			Node healthProfile = findHealthProfileByPersonId(idPerson);
			if(healthProfile == null){
				System.out.println(String.format("Person with id %d not found.",idPerson));
			} else {
				System.out.println(String.format("Health Profile for <<%s>> (ID=%d)",getFullnameById(idPerson),idPerson));
				System.out.println ("\n-------------------------------------------------------");
				System.out.format("%15s%15s%10s%10s", "Last Update", "Weight", "Height", "BMI");
				System.out.println ("\n-------------------------------------------------------");

				NodeList hpDetails = healthProfile.getChildNodes();
				for(int k=0; k<hpDetails.getLength(); k++){
					Node hpDetail = hpDetails.item(k);
					if(hpDetail.getNodeName().equals("lastupdate")){
						try {
							update = fmt.format(fmt.parse(hpDetail.getTextContent()));
						} catch (ParseException e) {
							
						}
					} else if(hpDetail.getNodeName().equals("weight")){
						weight = hpDetail.getTextContent();
					} else if(hpDetail.getNodeName().equals("height")){
						height = hpDetail.getTextContent();
					} else if(hpDetail.getNodeName().equals("bmi")){
						bmi = hpDetail.getTextContent();
					}
				}
				System.out.println(String.format("%15s%15s%10s%10s", update, weight, height,bmi.substring(0, bmi.indexOf(".")+3)));

			}
		} catch (XPathExpressionException e) {
			System.out.println(String.format("Person with id %d not found.",idPerson));
		}
	}


	/**
	 * The health profile reader gets information from the command line about
	 * weight and height and calculates the BMI of the person based on this
	 * parameters
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws ParserConfigurationException, SAXException,
	IOException, XPathExpressionException {
		if(args.length <2){
			System.err.println("Please specify xml location and command to be executed.");
			System.err.println("Example: java HealthProfileReaderXPath xml/people.xml printAll");
			System.err.println("\nValid commands: printAll, printHealthProfile <id>, printByWeight '<condition>'");
			return;
		}
		HealthProfileReaderXPath.XML_LOCATION = args[0];
		String command = args[1];


		HealthProfileReaderXPath reader = new HealthProfileReaderXPath();
		reader.loadXML();

		if(command.equals("printAll")){
			reader.printAllPeople();
		} else if (command.equals("printHealthProfile")){
			if(args.length<3){
				System.err.println("Please specify the id of the person to look for.");
				return;
			}
			int ID = Integer.valueOf(args[2]);
			reader.printHealthProfile(ID);
		} else if (command.equals("printByWeight")){
			if(args.length<3){
				System.err.println("Please specify the search condition. E.g. '<90'.");
				return;
			}
			reader.printPeopleByWeight(args[2]);
		}

	}
}
