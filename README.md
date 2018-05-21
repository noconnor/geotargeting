# Geotargeting POC

Application processes a json file of user coordinates and identifies users within a predefined distance 
of a specified "HOTSPOT" 

See `src/main/resources/customers.txt` for an example input file

<br />

## Building Jar

To generate an executable jar, from the project root directory execute:

`./gradlew jar`

Jar will be created in `${PROJECT_ROOT}/build/libs/geotargeting-1.0-SNAPSHOT.jar`

To run unittests, execute the following command:

`./gradlew clean test`

<br />

## Running jar

To get a full list of available options, execute the following command:

`java -jar geotargeting-1.0-SNAPSHOT.jar -h`

Available options are as follows:

| Option        | Definition                                                                                     |
| ------------- |------------------------------------------------------------------------------------------------|
| -h            | Print help information                                                                         |
| -i            | Specify input file i.e. customers.txt **(Required)**                                           |
| -o            | Output directory (Optional)                                                                    |
| -a            | Distance calculation algorithm, options are haversine or vincenty (Default is vincenty)        |
| -f            | Output format, options are console (to log to console) or gmaps (to generate interactive html) |


**NOTE:** if `-f gmaps` is specified and no `-o` option, the interactive map will be written to `${PROJECT_ROOT}/build/map.html`

**Full Example:**

```
./gradlew jar
cd build/libs
java -jar geotargeting-1.0-SNAPSHOT.jar -i ../../src/main/resources/customers.txt -o .
open map.html
``` 
