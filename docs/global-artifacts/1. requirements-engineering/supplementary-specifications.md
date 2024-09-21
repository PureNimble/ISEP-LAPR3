# Supplementary Specification (FURPS+)

## Functionality

_Specifies functionalities that:_

- _are common across several US/UC;_
- _are not related to US/UC, namely: Audit, Reporting and Security._

### Auditing
- "Real Estate USA needs an application that enables buyers who visit its stores/agencies to access the
properties available for lease or sale, as well as the companyâ€™s employees to carry out a set of
operations related to the real estate business. Among these operations are the publication of rental
and sale advertisements, the registration of a business (lease or sale) and the scheduling and
registration of visits to the property.
"
### Licensing

### Localization
- "The application must support the English language."
### Email
- "The password should have eight characters in length and should be generated automatically. The password is sent to the employee by e-mail."
### Help 
- "The company's systems administrator will be responsible for (...) preparing the system in order to facilitate the insertion of advertisements and facilitate the use of the application."
### Printing 
- "(...) publishes the offer so that it is visible to all clients who visit the agency and use the application."
### Reporting
- "The main function of a store manager are to monitor and streamline the branch with the aim of getting to know better the business carried out and to analyze and evaluate the performance of employees."
-"The manager of the network intends to analyze the performance of each of the branches and the global behavior of the network on a daily basis."
### Security
- "All those who wish to use the application must be authenticated with a password of seven alphanumeric characters, including three capital letters and two digits."
- "All registered information, except the agency commission, can be accessed by the client (...)"
### System management
- "The company's systems administrator will be responsible for registering all employees (...) and branches of the network (...) as well as preparing the system in order to facilitate the insertion of advertisements and facilitate the use of the application."
### Workflow
- "If the customer accepts the order, it is automatically scheduled in the system."


## Usability 

- "The
   application graphical interface is to be developed in JavaFX 11."
- "(...) use Javadoc to generate useful documentation for Java code."
- "(...) adopt recognized coding conventions
  and standards (...)"
- "All the images/figures produced during the software development process should be recorded in
  SVG format."

## Reliability
- Refers to the integrity, compliance and interoperability of the software. The requirements to be considered are: frequency and severity of failure, possibility of recovery, possibility of prediction, accuracy, average time between failures.
- When the application fails the algoritm keep the date safe
- "The application should use object serialization to ensure date persistence between two runs of the application"

## Performance
_Evaluates the performance requirements of the software, namely: response time, start-up time, recovery time, memory consumption, CPU usage, load capacity and application availability._
- The program should be able to support many users
- _ _
-

## Supportability

- "The development team must implement unit tests for all methods, except for methods that
  implement Input/Output operations."_Specifies or constraints the system design process. Examples may include: programming languages, software process, mandatory standards/patterns, use of development tools, class library, etc._

- "The unit tests should be implemented using the JUnit 5 framework."
- "The JaCoCo plugin should be used to generate the coverage report."
- All images should be in SVG format



## +

### Design Constraints

- _ _
- _ _
- _ _


### Implementation Constraints


- "During the system development, the team must: (i) adopt best practices for identifying
  requirements, and for OO software analysis and design; (ii) adopt recognized coding conventions
  and standards (e.g., CamelCase); (iii) use Javadoc to generate useful documentation for Java code. "
- "The application must be developed in Java language using the IntelliJ IDE or NetBeans."
- "The unit tests should be implemented using the JUnit 5
  framework"



### Interface Constraints
- "The application graphical interface is to be developed in JavaFX 11."

### Physical Constraints

- "The application should use object serialization to ensure date persistence between two runs of the
application."