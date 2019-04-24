package com.ideacrest.hireup.graphql;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import java.io.IOException;
import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import graphql.GraphQL;
import graphql.GraphQL.Builder;
import graphql.execution.AsyncSerialExecutionStrategy;
import graphql.execution.batched.BatchedExecutionStrategy;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Singleton
public class AggregateGraphQLProvider {
	private GraphQL graphQL;

	private GraphQLDataFetchers graphQLDataFetchers;

	@Inject
	public AggregateGraphQLProvider(GraphQLDataFetchers graphQLDataFetchers) throws IOException {
		this.graphQLDataFetchers = graphQLDataFetchers;
		URL url = Resources.getResource("aggregate_schema.graphqls");
		String sdl = Resources.toString(url, Charsets.UTF_8);
		GraphQLSchema graphQLSchema = buildSchema(sdl);
//		For sequential flow
//		Builder grapghQLBuilder = new Builder(graphQLSchema);
//		grapghQLBuilder.queryExecutionStrategy(new AsyncSerialExecutionStrategy());
//		this.graphQL = grapghQLBuilder.build();

		this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
	}

	public GraphQL graphQL() {
		return graphQL;
	}

	private GraphQLSchema buildSchema(String sdl) {
		TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
		RuntimeWiring runtimeWiring = buildWiring();
		SchemaGenerator schemaGenerator = new SchemaGenerator();
		return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
	}

	private RuntimeWiring buildWiring() {
		return RuntimeWiring.newRuntimeWiring()
				.type(newTypeWiring("Query")
						.dataFetcher("jobApplicationsById", graphQLDataFetchers.getJobApplicationsByIdDataFetcher())
						.dataFetcher("jobApplications", graphQLDataFetchers.getAllJobApplications()))
				.type(newTypeWiring("JobApplications")
						.dataFetcher("jobOpenings", graphQLDataFetchers.getJobOpeningsByIdDataFetcher())
						.dataFetcher("submittedBy", graphQLDataFetchers.getUserByIdDataFetcher()))
				.type(newTypeWiring("JobOpenings").dataFetcher("createdBy",
						graphQLDataFetchers.getUserByIdForJobOpeningDataFetcher()))
				.build();
	}

}
