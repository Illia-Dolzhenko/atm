# README #

### ATM Service test assignemnt ###

	/src/main/resources/application.properties contains intial data
	
### Exposed endpoints:

	GET http://localhost:8080/balance?account=123&password=123
	POST http://localhost:8080/withdraw?account=123&password=123&amount=123
	
### To run: ###

	mvn spring-boot:run
