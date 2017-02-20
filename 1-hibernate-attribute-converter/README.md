## This example shows how you can use AttributeConverter for saving and downloading data from database using Hibernate (JPA 2.1). In particular how to save and download LocalDate, LocalDateTime and the rest of types of data from java.time (Java 8) in Hibernate 5.

### Prerequisites

- Java 8
- Maven > 3.0

### Configurations

Open file ...\SimpleApiApp\src\main\resources\application.properties and set your own configurations for database connection.
Create a new schema with name specified in "spring.datasource.url" property (default name = "users_schema").

### Build and run

Import projects as "Existing Maven Project". Run it as "Spring Boot App" (for Eclipse: Run configurations... -> Spring boot app, than chose current project "1-hibernate-attribute-converter" in "Projects" field and "dsergeyev.example.ExampleAppilation" in "Main type" field.
Use this configuration to run project.

### Usage

* Run the application;
* Pay attention on the User's field type and the appropriating types of field in created table "user" in database;
* Use the following request to create new user:
	
	POST localhost:8080/users
	
	Content-Type: application/json
	
	Body: 
	{
		"firstName": "Dmitry",
		"secondName": "Sergeyev"
	}
		
	The most of fields will be set automaticaly.
	
* Use the following request to get all users:

	GET localhost:8080/users
	
	Content-Type: application/json
	
* You can also added new user to database manually, for example, use this SQL request: INSERT INTO `users_schema`.`user` (`birth_date1`, `birth_date2`, `birth_date3`, `color`, `create_date`, `fisrt_name`, `second_name`, `update_date`) VALUES ('2016-01-01', '2016-02-02', '2016-03-03', '11 22 33 222', '2016-04-04 20:22:24', 'Ivan', 'Ivanov', '2016-05-05 21:23:25') and then try to get all users again.
