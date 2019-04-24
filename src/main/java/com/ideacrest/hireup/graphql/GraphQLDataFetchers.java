package com.ideacrest.hireup.graphql;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ideacrest.app.bean.GraphQLResponse;
import com.ideacrest.app.eureka.EurekaServerHelper;
import com.ideacrest.app.hystrix.ClientServiceHandler;
import com.ideacrest.hireup.dao.JobApplicationDao;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

import graphql.language.Field;
import graphql.language.SelectionSet;
import graphql.schema.AsyncDataFetcher;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

@Singleton
public class GraphQLDataFetchers {

	private JobApplicationDao jobApplicationDao;

	private HttpClient httpClient;

	private ObjectMapper objectMapper;

	private Logger LOGGER = LoggerFactory.getLogger(GraphQLDataFetchers.class);

	DynamicStringProperty JOB_OPENING_NAME_PROPERTY = DynamicPropertyFactory.getInstance()
			.getStringProperty("JOB_OPENING_NAME", "ID_SERVER_HIRE_UP_JOB_OPENING_SERVICE");

	DynamicStringProperty USER_SERVICE_NAME_PROPERTY = DynamicPropertyFactory.getInstance()
			.getStringProperty("USER_SERVICE_NAME", "ID_SERVER_HIRE_UP_USER_SERVICE");

	private EurekaServerHelper eurekaServerHelper;

	@Inject
	public GraphQLDataFetchers(JobApplicationDao jobApplicationDao, EurekaServerHelper eurekaServerHelper,
			HttpClient httpClient, ObjectMapper objectMapper) {
		this.jobApplicationDao = jobApplicationDao;
		this.eurekaServerHelper = eurekaServerHelper;
		this.httpClient = httpClient;
		this.objectMapper = objectMapper;
	}

	public DataFetcher getJobApplicationsByIdDataFetcher() {
		return AsyncDataFetcher.async(dataFetchingEnvironment -> {
			LOGGER.info(getExecutionPathFromDataFetchingEnvironment(dataFetchingEnvironment)
					+ "getJobApplicationsByIdDataFetcher --> fetcher is called - " + System.currentTimeMillis());
			String jobApplicationId = dataFetchingEnvironment.getArgument("_id");
			return jobApplicationDao.getJobApplicationsById(Long.valueOf(jobApplicationId));
		});
	}

	public DataFetcher getAllJobApplications() {
		return AsyncDataFetcher.async(dataFetchingEnvironment -> {
			LOGGER.info(getExecutionPathFromDataFetchingEnvironment(dataFetchingEnvironment)
					+ "getAllJobApplications --> fetcher is called - " + System.currentTimeMillis());
			return jobApplicationDao.getAllJobApplications();
		});
	}

	public DataFetcher getJobOpeningsByIdDataFetcher() {
		return AsyncDataFetcher.async(dataFetchingEnvironment -> {
			LOGGER.info(getExecutionPathFromDataFetchingEnvironment(dataFetchingEnvironment)
					+ getExecutionPathFromDataFetchingEnvironment(dataFetchingEnvironment)
					+ " --> getJobOpeningsByIdDataFetcher --> fetcher is called - " + System.currentTimeMillis());
			Map<String, Object> jobApplicationBean = objectMapper.convertValue(dataFetchingEnvironment.getSource(),
					new TypeReference<Map<String, Object>>() {
					});
			long id = Long.valueOf(jobApplicationBean.get("submittedBy").toString());
			String query = getGraphQLQuery("jobOpeningsById(_id: " + id + ")",
					dataFetchingEnvironment.getField().getSelectionSet());
			GraphQLResponse result = getJobOpeningsById(query);
			Map<String, Object> map = new HashMap<>();
			map = objectMapper.convertValue(result.getData(), new TypeReference<Map<String, Object>>() {
			});
			return (Map<String, Object>) map.get("jobOpeningsById");
		});
	}

	public DataFetcher getUserByIdDataFetcher() {
		return AsyncDataFetcher.async(dataFetchingEnvironment -> {
			LOGGER.info(getExecutionPathFromDataFetchingEnvironment(dataFetchingEnvironment)
					+ getExecutionPathFromDataFetchingEnvironment(dataFetchingEnvironment)
					+ " getUserByIdDataFetcher --> fetcher is called - " + System.currentTimeMillis());
			Map<String, Object> jobApplicationBean = objectMapper.convertValue(dataFetchingEnvironment.getSource(),
					new TypeReference<Map<String, Object>>() {
					});
			long id = Long.valueOf(jobApplicationBean.get("submittedBy").toString());
			String query = getGraphQLQuery("userById(_id: " + id + ")",
					dataFetchingEnvironment.getField().getSelectionSet());
			GraphQLResponse result = getUserById(query);
			Map<String, Object> resultDataValue = new HashMap<>();
			resultDataValue = objectMapper.convertValue(result.getData(), new TypeReference<Map<String, Object>>() {
			});
			return (Map<String, Object>) resultDataValue.get("userById");
		});
	}

	public String getGraphQLQuery(String typeName, SelectionSet selectionSet) {
		StringBuilder queryBuilder = new StringBuilder("{\"query\" : \"{ " + typeName + " { ");
		selectionSet.getSelections().stream().forEach(eachSelection -> {
			queryBuilder.append("" + ((Field) eachSelection).getName() + " ");
		});
		return queryBuilder.append("}}\"}").toString();
	}

	public DataFetcher getUserByIdForJobOpeningDataFetcher() {
		return AsyncDataFetcher.async(dataFetchingEnvironment -> {
			LOGGER.info(getExecutionPathFromDataFetchingEnvironment(dataFetchingEnvironment)
					+ getExecutionPathFromDataFetchingEnvironment(dataFetchingEnvironment)
					+ " getUserByIdForJobOpeningDataFetcher --> fetcher is called - " + System.currentTimeMillis());
			Map<String, Object> jobOpeningData = dataFetchingEnvironment.getSource();
			long id = Long.valueOf(jobOpeningData.get("createdBy").toString());
			String query = getGraphQLQuery("userById(_id: " + id + ")",
					dataFetchingEnvironment.getField().getSelectionSet());
			GraphQLResponse result = getUserById(query);
			Map<String, Object> resultDataValue = new HashMap<>();
			resultDataValue = objectMapper.convertValue(result.getData(), new TypeReference<Map<String, Object>>() {
			});
			return (Map<String, Object>) resultDataValue.get("userById");
		});
	}

	private GraphQLResponse getJobOpeningsById(String query) {
		try {
			String clientURL;
			try {
				clientURL = eurekaServerHelper.getEurekaClientInstanceUsingAppName(JOB_OPENING_NAME_PROPERTY.get());
			} catch (Exception e) {
				clientURL = JOB_OPENING_NAME_PROPERTY.get();
			}

			HttpPost httpPost = new HttpPost(clientURL + "/graphql");
			httpPost.setHeader("content-type", "application/json");
			httpPost.setEntity(new StringEntity(query));

			ClientServiceHandler jobOpeningService = new ClientServiceHandler(httpPost, httpClient);
			jobOpeningService.setCacheKey("graphql_job_opening");
			Response result = jobOpeningService.execute();
			return objectMapper.readValue(result.getEntity().toString(), GraphQLResponse.class);
		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
			return null;
		}
	}

	private GraphQLResponse getUserById(String query) {
		try {
			String clientURL;
			try {
				clientURL = eurekaServerHelper.getEurekaClientInstanceUsingAppName(USER_SERVICE_NAME_PROPERTY.get());
			} catch (Exception e) {
				clientURL = USER_SERVICE_NAME_PROPERTY.get();
			}

			HttpPost httpPost = new HttpPost(clientURL + "/graphql");
			httpPost.setHeader("content-type", "application/json");
			httpPost.setEntity(new StringEntity(query));

			ClientServiceHandler userClientService = new ClientServiceHandler(httpPost, httpClient);
			userClientService.setCacheKey("graphql_user");
			Response result = userClientService.execute();
			return objectMapper.readValue(result.getEntity().toString(), GraphQLResponse.class);
		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
			return null;
		}
	}

	private String getExecutionPathFromDataFetchingEnvironment(DataFetchingEnvironment dataFetchingEnvironment) {
		return dataFetchingEnvironment.getExecutionStepInfo().getPath().toString();
	}

}
