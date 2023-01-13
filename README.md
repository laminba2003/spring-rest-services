## SpringBoot Rest Service

### Introduction 

This is a simple SpringBoot Rest Service built around these capabilities :

- Persistence with JPA and Spring Data
- Data Migration with Liquibase
- Caching with Redis
- Exception Handling
- Logging with log4j2
- Oauth2 Security with OIDC
- Stubs generation with Spring Cloud Contract
- Distributed Tracing with Sleuth
- Documentation with Spring REST Docs and OpenAPI 
- CI/CD with Github actions

### REST endpoints

| HTTP verb | Resource  | Description
|----|---|---|
|  GET  | /persons  | retrieve list and information of persons  
|  GET |  /persons/{id} | retrieve information of a person specified by {id}
|  POST | /persons  | create a new person with payload  
|  PUT   |  /persons/{id} | update a person with payload   
|  DELETE   | /persons/{id}  |  delete a person specified by {id} 
|  GET  | /countries  | retrieve list and information of countries  
|  GET |  /countries/{name} | retrieve information of a country specified by {name} 
|  POST | /countries  | create a new country with payload  
|  PUT   |  /countries/{name} | update a country with payload   
|  DELETE   | /countries/{name}  |  delete a country specified by {name} 


### Developer Guide

You can read the developer guide [here](https://spring-documentation.netlify.app/) 

### Helm Chart 

Follow this [link](https://artifacthub.io/packages/helm/spring-rest/spring-rest) to know how 
to install this service on your Kubernetes cluster with Helm.