To setup the application and create the database structure:-
cd to the atm directory
run mvnw spring-boot:run
Wait for it to finish.
Kill the application with control c.
Fire up your Ide and import the project.
Go into the test directory.
The AtmControllerTest can be run from the src/test/java directory as Junit tests.
Firstly the unit tests run.
Then the file atm.txt that contains the input data is parsed and the account transactions processed.
Further Bank transactions can be added here at will.
