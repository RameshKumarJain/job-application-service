package com.ideacrest.hireup.resource;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.ideacrest.hireup.graphql.AggregateGraphQLProvider;

import graphql.ExecutionInput;
import graphql.ExecutionResult;

@Path("/aggregate-graphql")
@Produces(MediaType.APPLICATION_JSON)
public class AggregateGraphQLResource {

	private Logger LOGGER = LoggerFactory.getLogger(AggregateGraphQLResource.class);

	private AggregateGraphQLProvider aggregateGraphQLProvider;

	@Inject
	public AggregateGraphQLResource(AggregateGraphQLProvider graphQLProvider) {
		this.aggregateGraphQLProvider = graphQLProvider;
	}

	@POST
	public ExecutionResult getGraphQLResult(Map<String, String> payload)
			throws InterruptedException, ExecutionException {
		String queryData = fetchQueryFromThePayload(payload);
		ExecutionInput executionInput = ExecutionInput.newExecutionInput().query(queryData).build();
		CompletableFuture<ExecutionResult> promise = aggregateGraphQLProvider.graphQL().executeAsync(executionInput);
		return promise.get();

//		return aggregateGraphQLProvider.graphQL().execute(queryData);
	}

	private String fetchQueryFromThePayload(Map<String, String> payload) {
		String queryData = "";
		try {
			if (payload.get("query") != null) {
				queryData = payload.get("query");
			}
		} catch (Exception e) {
			LOGGER.debug("Exception occured while fetching query from the request.");
		}
		return queryData;
	}

}
