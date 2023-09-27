# Repository of Levin Hertrich #
**Mail:** st166200@stud.uni-stuttgart.de
# Prerequisites #
* Make sure at least Java JDK 17 is installed and configured. \
* The application uses a Database, therefore it is necessary that MariaDB is installed with the following settings: 
    * The port is set to 3306
    * An account with username and password "root" is configured 
* You also need to install maven manually or use the maven wrapper instead. To use the maven wrapper navigate to the folder `/api` and follow the instructions in the README.md there.

# Run the application #
## The mail server ##
Because the application contains a mail system to inform assignees about new assignments, it is necessary to have an account at the smtp mail server of the university of stuttgart and to set some values in the `application.properties` file before starting the application. The file can be found at api/src/main/ressources/application.properties. There you have to set the following values:
* `mail.username`: Set your username 
* `mail.password`: Set your password, ensure that the value is never in a public repository and that nobody can see the value
* `mail.senderAddress`: Set your mail address, the mails will be send from this address

## The api ##
The application contains an api for the backend. To start the backend, navigate to the folder `/api` and enter the command `./mvnw spring-boot:run` To get more options and details also navigate to the folder `/api` and follow the instructions in the `README.md` there. The api can be accessed under the url `http://localhost:8080/api/v1/{entity}` (possible entities: assignees and todos) via the browser or a tool like `postman`.

## The frontend ##
The application contains a frontend as well. To start it navigate to the folder `frontend` an enter the command `npm install`. After that enter `npm run serve` to start the hot reload developoment server which can then be accessed under the url `http://localhost:8000`. To get more options and details navigate to the folder `frontend` and follow the instructions in the `README.md` there.
