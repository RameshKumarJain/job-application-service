# job-application-service

Resources
-----

Two resources files 
	GraphQLResource - expose GraphQL service
	JobApplicationResource - expose CURD operation for Job applications
	AggregateGraphQLResource - another GraphQL service which gets User and Job opening associated with the Job application.

GraphQL
----
GraphQL setup
1 GraphQLProvider:
	Initialize the GraphQL object by reading the schema, type registry the schema and wiring the type Object with the data fetchers.


2 GraphQLDataFetchers:
	defines the data fetching logic either from DB or making Rest call or by consuming another GraphQL end p[oint.


**Note**
---
To make your GraphQL query execution parallel, define your data-fetcher async using an instance of CompletableFuture data-fetcher.
You can also use AsyncDataFetcher.async(DataFetcher<T> wrappedDataFetcher) to make data fetchers as async.

Database - Mongo
---
1 JobApplicationDao extends the MongoService class from commons_library, which defines basic methods required to communicate with mongodb.
