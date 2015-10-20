# introsde-2015-assignment-1

This project serves as an introduction to different technologies for working with XML, XSD, and JSON from a Java program. It is an assignment for the Introduction to Service Design and Engineering lecture at UNITN, Winter Semester 2015-16.

The examples are based on a `people.xml` file defined by `people.xsd`. Together they define a database of people with some attributes such as their first name, last name, birthdate, health profile (weight, height, bmi, and update date).

##Package Structure

The project is divided into 3 packages.

`xpath`: Inside this package is the class for reading from people.xml and extracting user information using xpath, namely `PersonReaderXPath.java`. There is also a class file (PersonWriter.java) which was used for generating the initial people.xml file.

`marshalling`: This package contains the classes necessary for Marshalling from an object representation to XML/JSON using JAXB/Jackson (`JAXBMarshaller.java`). It also contains the classes for Unmarshalling from an XML representation directly into java objects. Note that for either of these classes to work, first the according classes have to be generated from the people.xsd file. See the targets section for instructions.

`model`: Representations of the Person, People and HealthProfile objects which are used for the generation of the initial XML file.

##Assignment information

As this project is part of an assignment for the lecture. This section describes how each individual task was solved.

*Based on Lab 3
1.Use xpath to implement methods like getWeight and getHeight:
	`xpath/PersonReaderXPath.java` implements these methods. For each case two methods were developed. They take either the id of the person, or the <firstname, lastname> and use XPATH to get the required person.

2.Make a function that prints all people in the list with detail:
	`xpath/PersonReaderXPath.java` contains the method `printAllPeople()` which gets the people node using XPATH and then traverses the tree using methods like `getChildNodes()`.

3.Make a function that accepts id as parameter and prints the HealthProfile of the person with that id:
	`xpath/PersonReaderXPath.java` contains the methods `findHealthProfileByPersonId()` and `printHealthProfile()` which fetch (using XPATH) and print the required health profile.
4.A function which accepts a weight and an operator (=, > , <) as parameters and prints people that fulfill that condition (i.e., >80Kg, =75Kg, etc.):
	`xpath/PersonReaderXPath.java` contains the methods `getPersonsbyWeight()` and `printPeopleByWeight()` which fetch (using XPATH) the people that match the condition and print it to the screen.

*Based on Lab 4
1.Create the XML schema XSD file for the example XML document of people:
	The file is `xml/people.xsd` and follows the 'Salami Slice' approach to XSD definitions, that is, types are defined as root elements.
2.Write a java application that does the marshalling and un-marshalling using classes generated with JAXB XJC:
	`marshalling/JAXBMarshaller.java` does the marshalling to XML. It prints the output on screen, and saves it to a file specified as a parameter.
	`marshalling/JAXBUnMarshaller.java` does the unmarshalling from XML. The location of the XML file is specified as a parameter.
3.Make your java application to convert also JSON:
	`marshalling/JAXBMarshaller.java` does the marshalling into JSON. It prints the output on screen, and saves it to a file specified as a parameter.


##Targets

This project contains a build.xml file which can be run by ant. It will download all the required dependencies using ivy. It will also download ivy if it is not installed.

To download dependencies, generate classes from the XSD file, and to execute everything you can simply run:
```
ant execute.evaluation
```

There are several targets that allow you to execute the tasks in a seperate manner.

`execute.generateInitialXML` created the initial people.xml file with 20 persons.
`execute.printAllPeople` prints the table with all the people.
`execute.printHealthProfile` prints the health profile for person with id=5.
`execute.printByWeight` prints the information for all the people with weight>90.
`execute.marshallXML` marshalls an object with 3 persons into XML. Saves it to file marshalled.people.xml and prints it on screen.
`execute.marshallJSON` marshalls an object with 3 persons into JSON. Saves it to file marshalled.people.json and prints it on screen.
`execute.unmarshallXML` unmarshalls the contents of people.xml and prints them on screen.