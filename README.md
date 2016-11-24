# fcm-camel
Connectivity to Firebase Cloud Messaging (FCM) with a sample XMPP client that 
runs on Apache Camel, packaged as a WAR and is ready to be deployed to a 
servlet container.

See also [fcm-android](https://github.com/allancth/fcm-android).

## Pre-requisites
1. FCM sender ID
2. FCM token
3. JDK 8 + Maven 3.3.9

Edit xmpp.properties, replace with your sender ID and token.

## Run
    mvn clean tomcat7:run
