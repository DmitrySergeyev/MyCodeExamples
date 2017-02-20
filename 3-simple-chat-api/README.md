## This project is example of simple chat API

Examples of using API are located in folder "request examples"

### API allow you: 
* Create new user and verified him via email;
* Reset user password (via user's email);
* Change user password;
* Search user(-s);
* Create chats;
* Send messages in chat;

Using basic authentification to access to the resources; 

### Prerequisites

- Java 8
- Maven > 3.0

### Configurations

* Open file ...\SimpleApiApp\src\main\resources\application.properties and set your own configurations for database connection ("DATA SOURCE" section);
* Create a new schema in database with name specified in "spring.datasource.url" property (default name = "chat_schema");
* Setup you own configuration of email address which will be used to sending emails ("EMAIL SENDING" section);
* In my case email sending works only after allowing less secure apps to access your account (https://support.google.com/accounts/answer/6010255?hl=en) and disabling Avast antivirus;

### Build and run

Import projects as "Existing Maven Project". Run it as "Spring Boot App" (for Eclipse: Run configurations... -> Spring boot app, than chose current project "3-simple-chat-api" in "Projects" field and "dsergeyev.example.ChatApplication" in "Main type" field.
Use this configuration to run project.