## Running (Play's docs - https://www.playframework.com/documentation/2.7.x/Deploying)

Run this using [sbt](http://www.scala-sbt.org/).
Install sbt 1.2.8.
In project folder run command "sbt dist", this produces a ZIP file containing all JAR files needed to run application in the target/universal folder.
To run the application, unzip the file on the target server, and then run the script in the bin directory.

Url: http://localhost:9000

There is no any frontend.

Available routes:
POST /customPortfolio 
```Expected request body: {"stocks":[{"symbol":"AAPL","volume":50},{"symbol":"MDSO","volume":50}]}```
```Expected response: {"value": 200.00, "allocations":[{"sector":"Health Technology", "assetValue":100.00, "proportion":0.500},{"sector":"Technology Services", "assetValue":100.00, "proportion":0.500}]}```

Test request:
```
curl --header "Content-Type: application/json" --request POST --data '{"stocks":[{"symbol":"AAPL","volume":50},{"symbol":"HOG","volume":10},{"symbol":"MDSO","volume":1},{"symbol":"IDRA","volume":1},{"symbol":"MRSN","volume":1}]}' localhost:9000/message
```