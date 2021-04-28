To run the application, import the maven project into you're favourite ide and choose run.

It can also be run from the command line using :-
mvn install
mvn spring-boot:run

It uses an in memory H2 database which persists to a file in the data directory.


To add car makes goto
http://localhost:8080/h2
username sa
password sa
insert into make (make) values(Porsche); etc

The tests are in the test directory and should give an idea of the things you can do and the parameters that can be passed in.
The following operations are supported.

Patch
To change the colour of a car to say maroon
http://localhost:8080/api/v1/car/1
application/json-patch+json
[{\"op\": \"replace\", \"path\": \"/colour\", \"value\": \"maroon\"}]

Put
To create a new car
http://localhost:8080/api/v1/car
{\"model\": \"XC90\",\"colour\": \"Yellow\",\"year\": 2010,\"make\": {\"id\": 1,\"make\": \"Volvo\"}}

Get
To view a car along with it's DataMuse words
http://localhost:8080/api/v1/car/1

Delete
To delete a car
http://localhost:8080/api/v1/car/S40