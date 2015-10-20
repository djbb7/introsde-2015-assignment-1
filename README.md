# introsde-2015-assignment-1

This project serves as an introduction to different technologies for working with XML, XSD, and JSON from a Java program. The examples are based on a people.xml file defined by people.xsd. In it is defined a database of people with some attributes such as their names, birthdate, health profile (weight, height, bmi).

The first part, found in '' performs operations on the people.xml file by using XPATH to transform de XML tree.

The second part, found in '' is in charge of unmarshalling from XML('') and marshalling to XML/JSON (''). For (un)marshalling from/to XML, the JAXB library is used. For marshalling to JSON, the Jackson library is used. In both cases, automatically generated classes are used. These classes are generated with the xjc compiler, and can be found in '' after the according build target has been executed

