package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlRootElement(name="healthprofile")
@XmlType(propOrder = { "lastUpdate", "weight", "height", "BMI" })
@XmlAccessorType(XmlAccessType.FIELD)
public class HealthProfile {
	private double weight; // in kg
	private double height; // in m
	@XmlElement(name="lastupdate")
	private XMLGregorianCalendar lastUpdate;

	public HealthProfile(double weight, double height) {
		this.weight = weight;
		this.height = height;
		this.setLastUpdate(getRandomDate());
	}

	public HealthProfile() {
		this.weight = 85.5;
		this.height = 1.72;
		this.setLastUpdate(getRandomDate());
	}
	
	public XMLGregorianCalendar getLastUpdate(){
		return this.lastUpdate;
	}
	
	public void setLastUpdate(String lastUpdate){
		try {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.clear();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar parsedCalendar = Calendar.getInstance();
			parsedCalendar.setTime(sdf.parse(lastUpdate));
			calendar.set( parsedCalendar.get( Calendar.YEAR ), 
	                parsedCalendar.get( Calendar.MONTH ),
	                parsedCalendar.get( Calendar.DATE ) );
			this.lastUpdate = DatatypeFactory.newInstance().newXMLGregorianCalendar( calendar );
			} catch (ParseException e){
				
			} catch(DatatypeConfigurationException e){
				
			}
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	public String toString() {
		return "Height="+height+", Weight="+weight;
	}

	@XmlElement(name="bmi")
	public double getBMI() {
		return this.weight/(Math.pow(this.height, 2));
	}
	
	/* 
	 * Solution to Exercise #01-1e
	 * creating a random date between now and 1950
	 */
	private String getRandomDate() {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR); 		// 1. get the current year
		int year = (int) Math.round(Math.random()*(currentYear-1950)+1950); // 2. generate a random year 
																			//    between 1950 and currentYear
		int month = (int) Math.round(Math.floor(Math.random()*11)+1);		// 3. select a random month of the year
		// 4. select a random day in the selected month
		// 4.1 prepare a months array to store how many days in each month
		int[] months = new int[]{31,28,30,30,31,30,31,31,30,31,30,31};
		// 4.2 if it is a leap year, feb (months[1]) should be 29
		if ((currentYear % 4 == 0) && ((currentYear % 100 != 0) || (currentYear % 400 == 0))) {
			months[1] = 29;
		}
		long day = Math.round(Math.floor(Math.random()*(months[month-1]-1)+1));
		return ""+year+"-"+month+"-"+day;
	}
}
