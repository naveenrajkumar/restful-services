# Account Rest API Demo with Spring Boot

The Sample Account Rest services as following capabilities (Refer postman collection), Persistence is done through JPA, h2 temporary database is used in this process. 
 * Get  - /accounts - Returns all account
 * Get  - /account/{id}/transactions - Returns all transactions for an account
 * Post - /account - Creates an account
 * Post - /account/{id}/transaction - Posts a transaction for an account
 
[Swagger Specification](http://13.210.72.123:8080/swagger-ui.html)
The instance is hosted in aws ec2 for a temporary period, Postman Collections in the repository has the sample requests using this repository.

The [Sample Angular UI App](http://sinfinity.io/accounts) is deployed on AWS for a quick look of the functionality, the code for the app is in [git hub repo](https://github.com/naveenrajkumar/angularapp-accountservice)