package com.ideacrest.hireup.resource;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.ideacrest.hireup.graphql.GraphQLProvider;

import graphql.ExecutionResult;

@Path("/graphql")
@Produces(MediaType.APPLICATION_JSON)
public class GraphQLResource {

	private GraphQLProvider graphQLProvider;

	@Inject
	public GraphQLResource(GraphQLProvider graphQLProvider) {
		this.graphQLProvider = graphQLProvider;
	}

	@POST
	public ExecutionResult getGraphQLResult(Map<String, String> payload) {
		String queryData = "";
		try {
			if (payload.get("query") != null) {
				queryData = payload.get("query");
			}
		} catch (Exception e) {

		}
		return graphQLProvider.graphQL().execute(queryData);
	}
}
