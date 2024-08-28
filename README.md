#To build the project:
mvn clean package

#To run the jar in background:
`nohup java -jar ssl-server-<version>.jar --server.ssl.key-store=file:<filepath> &`

#Send curl request for testing:
`curl -vsk -H "Content-Type:application/json" -X POST "https://<host-ip>:443/elisRequest" -d '<payload>'`<br/>
Help:<br/>
`-v : verbose output`<br/>
`-s : silent mode`<br/>
`-k : Allow insecure server connections when using SSL`<br/>
`-H : header`<br/>
`-X : Specify request command to use`<br/>
`-d : HTTP POST data`<br/>

#Documentation:
https://confluence-oss.seli.wh.rnd.internal.ericsson.com/display/ZORA/1.+Deploy+the+ELIS+simulator

#References:
1. https://howtodoinjava.com/spring-boot/spring-boot-ssl-https-example
2. https://www.baeldung.com/spring-boot-https-self-signed-certificate
3. https://www.novixys.com/blog/how-to-generate-rsa-keys-java/