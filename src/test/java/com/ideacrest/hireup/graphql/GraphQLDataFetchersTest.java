package com.ideacrest.hireup.graphql;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.http.client.HttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideacrest.hireup.bean.JobApplication;
import com.ideacrest.hireup.dao.JobApplicationDao;

import graphql.Scalars;
import graphql.execution.ExecutionPath;
import graphql.execution.ExecutionStepInfo;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class GraphQLDataFetchersTest {

	@InjectMocks
	GraphQLDataFetchers graphQLDataFetchers;

	@Mock
	private JobApplicationDao jobApplicationDao;

	@Mock
	private HttpClient httpClient;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private DataFetchingEnvironment environment;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(environment.getExecutionStepInfo()).thenReturn(ExecutionStepInfo.newExecutionStepInfo()
				.type(Scalars.GraphQLString).path(ExecutionPath.rootPath()).build());
	}

	@Test
	public void testGetJobsByIdDataFetcher() throws Exception {
		DataFetcher jobDataFetcher = graphQLDataFetchers.getJobApplicationsByIdDataFetcher();
		Mockito.when(environment.getArgument("_id")).thenReturn("1");
		Mockito.when(jobApplicationDao.getJobApplicationsById(1)).thenReturn(new JobApplication());
		CompletableFuture<JobApplication> jobs = (CompletableFuture<JobApplication>) jobDataFetcher.get(environment);
		Assert.assertTrue("Job opening should not be null.", jobs.get() != null);
	}

	@Test
	public void testGetAllJobs() throws Exception {
		DataFetcher jobDataFetcher = graphQLDataFetchers.getAllJobApplications();
		Mockito.when(jobApplicationDao.getAllJobApplications()).thenReturn(Arrays.asList(new JobApplication()));
		CompletableFuture<List<JobApplication>> jobsList = (CompletableFuture<List<JobApplication>>) jobDataFetcher
				.get(environment);
		Assert.assertTrue("Job openings  should not be null!!", jobsList != null && !jobsList.get().isEmpty());
	}

}
