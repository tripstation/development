Navigate to the root of the project via a command line and execute the command
mvn spring-boot:run

Open another command prompt and issue the following commands

To test the GET endpoint which returns a list of all existing drivers.
curl  http://localhost:8080/drivers

To Test the endpoint to allow new drivers to be created and stored.
curl -d '{"firstName": "Jannis","lastName": "Green","dateOfBirth": "1980-05-01"}' -H 'Content-Type: application/json' http://localhost:8080/driver/create

To test the  GET endpoint which returns a list of all drivers created after the specified date
curl  http://localhost:8080/drivers/byDate?date=1984-01-01

