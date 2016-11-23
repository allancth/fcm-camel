# fcm-camel
Connectivity to Firebase Cloud Messaging (FCM) with a sample XMPP client that 
runs on Apache Camel, packaged as a WAR and is ready to be deployed to a 
servlet container.

## Pre-requisites
1. FCM project id
2. FCM token

Edit xmpp.properties, replace with your project id and token.

## Run
    mvn clean tomcat7:run