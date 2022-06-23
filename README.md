# Beauty Store
Java Microservices with Spring Boot, Spring Cloud, Spring Cloud Gateway, Spring Security and JHipster

Microservice base code were generated with JHipster, but I change a lot for matching my architecture design.
Extract frontend code from gateway project. Keep gateway independence and stable whatever new features.

##My final design as below:
- gateway - Authorize for management user and route to microservices.
- gatewaymk - Authorize for market customer and route to microservices.
- auth-service - Authentication Service which support two type of users and manage users and authority assignment.
- customer-service - Manage customer's address and credit cards.
- product-service - Provide the list of products and ability to search products and get individual products.
- order-service - Manage orders for management users, Create/Cancel order for customers.
- payment-service - Charges the customer's credit card or integrate with third part payment.
- shipping-service - Gives shipping cost estimates based on goods and the received address. Manage the delivery status.
- notification-service - Notify user by email, sms or third part interface.

- beauty-frontend-web - Frontend web which link to gateway for management user.
- beauty-frontend-market-web - Frontend web which link to gatewaymk for customer.

##Features
- Support authorization base on RBAC and ABAC. 
- Using JWT, no matter how many permission data, the length of the JWT will not increase significantly to ensure 
  the performance of transmission and authentication in microservices.
- Support Domain Object Security (ACLs).
- Using kafka to communication between microservices
- Support search text with Elasticsearch.
- Using Liquibase for DB script management.
- Support to deploy to Kubernetes.

##Architecture
![architecture](https://github.com/SharpQin/beauty/raw/main/doc/architecture-diagram.png)

##Technology
- gateway: gateway | reactive | Redis
- gatewaymk: gateway | reactive
- auth: microservice | reactive | postgresql | Hazecast | Redis | Kafka | (Hibernate|R2DBC)
- customer: microservice | imperative | postgresql | Hazecast | Kafka | (Hibernate|JDBC)
- product: microservice | imperative | postgresql | Hazecast | Kafka | Elasticsearch | (Hibernate|JDBC)
- order: microservice | imperative | postgresql | Hazecast | Kafka | (Hibernate|JDBC) | openfeign
- payment: microservice | imperative | postgresql | Hazecast | Kafka | (Hibernate|JDBC)
- shipping: microservice | imperative | postgresql | Hazecast | Kafka | (Hibernate|JDBC)
- notificaiton: microservice | imperative | postgresql | Hazecast | Kafka | (Hibernate|JDBC)
