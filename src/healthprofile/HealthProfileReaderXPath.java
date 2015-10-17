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


public class HealthProfileReaderXPath {  	

	Document doc;
	XPath xpath;
	public static String XML_LOCATION;

	public HealthProfileReaderXPath(){
		Locale.setDefault(Locale.US);
	}

	public void loadXML() throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		doc = builder.parse(XML_LOCATION);

		//creating xpath object
		getXPathObj();
	}

	public XPath getXPathObj() {

		XPathFactory factory = XPathFactory.newInstance();
		xpath = factory.newXPath();
		return xpath;
	}

	public Node getPersonByFullname(String firstname, String lastname) throws XPathExpressionException {
		XPathExpression expr = xpath.compile("/people/person[firstname='" + firstname + "' and lastname='"+lastname+"']");
		Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
		return node;
	}

	public Node findHealthProfileByPersonId(int id) throws XPathExpressionException{
		XPathExpression expr = xpath.compile("/people/person[@id=" + id+"]/healthprofile");
		Node hProfile = (Node) expr.evaluate(doc, XPathConstants.NODE);
		return hProfile;
	}

	public String getFullnameById (int id) throws XPathExpressionException{
		XPathExpression expr = xpath.compile("//person[@id="+id+"]/firstname | //person[@id="+id+"]/lastname");
		NodeList names = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		String firstname = "", lastname = "";
		for(int j=0; j<names.getLength(); j++){
			Node name = names.item(j);
			if(name.getNodeName().equals("firstname")){
				firstname = name.getTextContent(); 
			} else if (name.getNodeName().equals("lastname")){
				lastname = name.getTextContent();
			}
		}
		return lastname+", "+firstname;
	}

	public Double getWeight(int id) throws XPathExpressionException{
		XPathExpression expr = xpath.compile("//person[@id="+id+"]/weight");
		String res = (String) expr.evaluate(doc, XPathConstants.STRING);
		return Double.valueOf(res);
	}

	public Double getWeight(String firstname, String lastname) throws XPathExpressionException{
		XPathExpression expr = xpath.compile("//person[firstname='"+firstname+"' and lastname='"+lastname+"']/weight");
		String res = (String) expr.evaluate(doc, XPathConstants.STRING);
		return Double.valueOf(res);
	}

	public Double getHeight(int id) throws XPathExpressionException{
		XPathExpression expr = xpath.compile("//person[@id="+id+"]/weight");
		String res = (String) expr.evaluate(doc, XPathConstants.STRING);
		return Double.valueOf(res);
	}

	public Double getHeight(String firstname, String lastname) throws XPathExpressionException{
		XPathExpression expr = xpath.compile("//person[firstname='"+firstname+"' and lastname='"+lastname+"']/weight");
		String res = (String) expr.evaluate(doc, XPathConstants.STRING);
		return Double.valueOf(res);
	}


	public NodeList getPersonsByWeight(String op, Double weight) throws XPathExpressionException {
		//do the actual searching now
		XPathExpression expr = xpath.compile(String.format("/people/person[./healthprofile/weight %s %.2f]",op,weight.doubleValue()));
		NodeList people = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		return people;

	}

	public NodeList getAllPeople() throws XPathExpressionException {
		XPathExpression expr = xpath.compile("//person");
		return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	}


	public void printAllPeople(){
		System.out.println ("--------------------------------------------------------------------------------------------------------------------");
		System.out.format("%5s%20s%20s%15s%15s%15s%10s%10s", "ID", "First Name", "Last Name", "Birthdate", "Last Update", "Weight", "Height", "BMI");
		System.out.println ("\n--------------------------------------------------------------------------------------------------------------------");

		try {
			NodeList persons = getAllPeople();
			printPeople(persons);
		} catch (XPathExpressionException e){
			System.err.println("Error printing all persons");
		}
	}

	private void printPeople(NodeList people){
		for(int k=0; k<people.getLength(); k++){
			Node person = people.item(k);
			printPerson(person);
		}
	}

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
		System.out.println(String.format("%5s%20s%20s%15s%15s%15s%10s%10s", id, first, last, dob, update, weight, height, bmi));
	}

	public void printPeopleByWeight(String query){
		String op = query.substring(0, 1);
		if(!op.equals(">") && !op.equals("<") && !op.equals("=")) {
			System.out.println(String.format("Invalid operator. Must be one of <, >, =. Found '%s'.",op));
			return;
		} 

		String qWeight = query.substring(1).trim();
		Double weight = Double.valueOf(qWeight);

		if(weight == null){
			System.out.println("Invalid parameter. Was expecting a number, encountered: '"+qWeight+"'");
			return;
		}

		try {
			System.out.println(String.format("People with weight%s%.2fkg",op,weight));

			System.out.println ("\n--------------------------------------------------------------------------------------------------------------------");
			System.out.format("%5s%20s%20s%15s%15s%15s%10s%10s", "ID", "First Name", "Last Name", "Birthdate", "Last Update", "Weight", "Height", "BMI");
			System.out.println ("\n--------------------------------------------------------------------------------------------------------------------");

			NodeList people = getPersonsByWeight(op, weight);
			printPeople(people);
		} catch (XPathExpressionException e){
			System.out.println(e.getMessage());
		}
	}

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
				System.out.println(String.format("%15s%15s%10s%10s", update, weight, height, bmi));

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
		HealthProfileReaderXPath.XML_LOCATION = args[0];
		String command = args[1];
		if(HealthProfileReaderXPath.XML_LOCATION == null){
			System.err.println("No XML file specified");
			return;
		}
		if(command==null){
			System.err.println("No command specified.");
			return;
		}

		HealthProfileReaderXPath reader = new HealthProfileReaderXPath();
		reader.loadXML();

		if(command.equals("printAll")){
			reader.printAllPeople();
		} else if (command.equals("printHealthProfile")){
			int ID = Integer.valueOf(args[2]);
			reader.printHealthProfile(ID);
		} else if (command.equals("printByWeight")){
			reader.printPeopleByWeight(args[2]);
		}

	}
}
