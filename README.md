
# This project is a sandbox project normally all the interns get while doing internship

### Prerequisites Before you begin, ensure you have the following installed:
1. Java Development Kit (JDK): Download and Install JDK
2. Gradle: Download and Install Maven

### Sandbox application is created with the same technologies what were used in production code — Java, Gradle, Grizzly, Jersey

### It’s a tiny integration hub wrapping existing public APIs.

### Backend and Run setup
1. ./gradlew build
2. Run the class in which main method exist which is Application.java

### The whole project is divide into the following task and each tasks were done in different branch, reviewed and merged.
#### TASK 1 :

Health check endpoint. Responds HTTP-200 to GET /health endpoint. Logs every request with SLF4J.

#### TASK 2 :

Create a endpoint which gives you a solar date - Earth date to Curiosity sol conversion endpoint.

Receives and optional date in ISO 8601 format. If not specified, takes current date. Converts date to a sol using this rough formula:

The logic to calculate the solar date is as follows
Ceil(Diff * 86400 / 88775.245)
Where Diff is difference between provided date and 06.08.2012 (Curiosity landing date) in days, Ceil is rounding up to the closest integer. For 6/15/2022 should give 3504.

Includes the tests, logging and error handling.

#### TASK 3:

Martian weather on a Curiosity mission sol

step-by step guide on the NASA’s API endpoint:

Make a test request (curl or Postman):

"https://mars.nasa.gov/rss/api/?feed=weather&feedtype=json&ver=1.0&category=msl"

Get the part of the response related to the weather

{
"id": "3338",
"terrestrial_date": "2022-06-27",
"sol": "3516",
"ls": "254",
"season": "Month 9",
"min_temp": "-66",
"max_temp": "-8",
"pressure": "875",
"pressure_string": "Higher",
"abs_humidity": "--",
"wind_speed": "--",
"wind_direction": "--",
"atmo_opacity": "Sunny",
"sunrise": "05:48",
"sunset": "18:05",
"local_uv_irradiance_index": "High",
"min_gts_temp": "-79",
"max_gts_temp": "8"
}

Create model class for response body mapping

Get a hold of Java HTTP client

Build and send a request via the client

Map the response to the POJO

Build the chain endpoint → request via HTTP client → mapping response → returning response to the endpoint call

Compare results with the official website of Mars Weather: https://mars.nasa.gov/msl/mission/weather/


Related URLs:

https://mars.nasa.gov/msl/mission/weather/

Curiosity Mars Weather at Gale Crater

The Curiosity rover sends back weather reports from Gale Crater, near the equator of Mars. (273 kB)

Mars Weather
Includes the tests (with NASA API client mock), logging and error handling.


#### TASK 4:

Martian weather on an Earth date endpoint

Provides Martian weather for a given Earth date using NASA’s APIs.

#### TASK 5:

Java versions endpoint

Fetch full list of releases for a request (curl or Postman): OpenAPI UI (Powered by Quarkus 2.9.2.Final

Input parameters to the url :

platform/architecture: Look in the website for values.

page : Iterate page number from 0 to response returns null.

And set the rest of the URL parameters to default values.

Each page returns list of releases. Need to iterate till the page that doesn’t have any releases. Collect the response and return a complete list.

Includes the tests (with AdoptOpenJDK client mock), logging and error handling.

#### TASK 6:
Dockerise martian app and run it in minikube.
