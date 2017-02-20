## This example is simple API which shows how you can create it using Spring boot and Hibernate.

### Include examples of: 
* Error handing ("REST and WEB services development using Spring", Balaji Varanasi and Sudha Delida, chapter 5);
* Paging and sorting ("REST and WEB services development using Spring", Balaji Varanasi and Sudha Delida, chapter 7);
* Data validation with help of hibernate validator;
* Using PagingAndSortingRepository (with custom requests);
* Using different type of relationships (@OneToMany, @ManyToOne, bidirectional @ManyToMany);
* Using custom JsonSerializer and JsonDeserializer;

### Prerequisites

- Java 8
- Maven > 3.0

### Configurations

Open file ...\SimpleApiApp\src\main\resources\application.properties and set your own configurations for database connection.
Create a new schema with name specified in "spring.datasource.url" property (default name = "users_schema").

### Build and run

Import projects as "Existing Maven Project". Run it as "Spring Boot App" (for Eclipse: Run configurations... -> Spring boot app, than chose current project "2-spring-boot-simple-api" in "Projects" field and "dsergeyev.example.SimpleApiApplication" in "Main type" field.
Use this configuration to run project.