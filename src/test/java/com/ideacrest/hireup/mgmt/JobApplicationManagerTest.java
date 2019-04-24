package com.ideacrest.hireup.mgmt;

import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ideacrest.app.validator.BeanValidatorUtility;
import com.ideacrest.hireup.bean.JobApplication;
import com.ideacrest.hireup.dao.JobApplicationDao;

public class JobApplicationManagerTest {

	@InjectMocks
	private JobApplicationManager jobApplicationManager;

	@Mock
	private JobApplicationDao jobApplicationDao;

	@Mock
	private BeanValidatorUtility beanValidatorUtility;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetJobApplicationsWithInvalidId() {
		Mockito.when(jobApplicationDao.getJobApplicationsById(Mockito.anyLong())).thenReturn(null);
		Response result = jobApplicationManager.getJobApplicationsById(12);
		int expectedStatusCode = Status.NOT_FOUND.getStatusCode();
		int actualStatusCode = result.getStatus();
		Assert.assertTrue(
				"Invalid status code.\nExpected: " + expectedStatusCode + "\n Actual code: " + actualStatusCode,
				expectedStatusCode == actualStatusCode);
		Assert.assertTrue("Response entity should not be null.", result.getEntity() != null);
	}

	@Test
	public void testGetJobApplicationByIdWithValidId() {
		Mockito.when(jobApplicationDao.getJobApplicationsById(Mockito.anyLong())).thenReturn(new JobApplication());
		Response result = jobApplicationManager.getJobApplicationsById(1);
		int expectedStatusCode = Status.OK.getStatusCode();
		int actualStatusCode = result.getStatus();
		Assert.assertTrue(
				"Invalid status code.\nExpected: " + expectedStatusCode + "\n Actual code: " + actualStatusCode,
				expectedStatusCode == actualStatusCode);
		Assert.assertTrue("Response entity should not be null.", result.getEntity() != null);
	}

	@Test
	public void testGetAllJobApplications() {
		Mockito.when(jobApplicationDao.getAllJobApplications()).thenReturn(Arrays.asList(new JobApplication()));
		Response result = jobApplicationManager.getAllJobApplications();
		int expectedStatusCode = Status.OK.getStatusCode();
		int actualStatusCode = result.getStatus();
		Assert.assertTrue(
				"Invalid status code.\nExpected: " + expectedStatusCode + "\n Actual code: " + actualStatusCode,
				expectedStatusCode == actualStatusCode);
		Assert.assertTrue("Response entity should not be null.", result.getEntity() != null);
	}

	@Test
	public void testAddJobApplicationWithInvalidPayload() {
		Mockito.when(beanValidatorUtility.validateBean(Mockito.any(JobApplication.class)))
				.thenReturn(Arrays.asList("Invalid name"));
		Response result = jobApplicationManager.addJobApplications(new JobApplication());
		int expectedStatusCode = Status.BAD_REQUEST.getStatusCode();
		int actualStatusCode = result.getStatus();
		Assert.assertTrue(
				"Invalid status code.\nExpected: " + expectedStatusCode + "\n Actual code: " + actualStatusCode,
				expectedStatusCode == actualStatusCode);
		Assert.assertTrue("Response entity should not be null.", result.getEntity() != null);
	}

	@Test
	public void testAddJobApplicationWithExistingId() {
		Mockito.when(beanValidatorUtility.validateBean(Mockito.any(JobApplication.class)))
				.thenReturn(new ArrayList<String>());
		Mockito.when(jobApplicationDao.getJobApplicationsById(Mockito.anyInt())).thenReturn(new JobApplication());
		Response result = jobApplicationManager.addJobApplications(new JobApplication());
		int expectedStatusCode = Status.BAD_REQUEST.getStatusCode();
		int actualStatusCode = result.getStatus();
		Assert.assertTrue(
				"Invalid status code.\nExpected: " + expectedStatusCode + "\n Actual code: " + actualStatusCode,
				expectedStatusCode == actualStatusCode);
		Assert.assertTrue("Response entity should not be null.", result.getEntity() != null);
	}

	@Test
	public void testAddJobApplicationWithValidId() {
		Mockito.when(beanValidatorUtility.validateBean(Mockito.any(JobApplication.class)))
				.thenReturn(new ArrayList<String>());
		Mockito.when(jobApplicationDao.getJobApplicationsById(Mockito.anyInt())).thenReturn(null);
		Mockito.doNothing().when(jobApplicationDao).insertOne(Mockito.any(JobApplication.class));
		Response result = jobApplicationManager.addJobApplications(new JobApplication());
		int expectedStatusCode = Status.OK.getStatusCode();
		int actualStatusCode = result.getStatus();
		Assert.assertTrue(
				"Invalid status code.\nExpected: " + expectedStatusCode + "\n Actual code: " + actualStatusCode,
				expectedStatusCode == actualStatusCode);
		Assert.assertTrue("Response entity should not be null.", result.getEntity() != null);
	}

	@Test
	public void testUpdateJobApplicationWithInvalidPayload() {
		Mockito.when(beanValidatorUtility.validateBean(Mockito.any(JobApplication.class)))
				.thenReturn(Arrays.asList("Invalid name"));
		Response result = jobApplicationManager.updateJobApplications(new JobApplication());
		int expectedStatusCode = Status.BAD_REQUEST.getStatusCode();
		int actualStatusCode = result.getStatus();
		Assert.assertTrue(
				"Invalid status code.\nExpected: " + expectedStatusCode + "\n Actual code: " + actualStatusCode,
				expectedStatusCode == actualStatusCode);
		Assert.assertTrue("Response entity should not be null.", result.getEntity() != null);
	}

	@Test
	public void testUpdateJobApplicationWithNonExistingId() {
		Mockito.when(beanValidatorUtility.validateBean(Mockito.any(JobApplication.class)))
				.thenReturn(new ArrayList<String>());
		Mockito.when(jobApplicationDao.getJobApplicationsById(Mockito.anyInt())).thenReturn(null);
		Response result = jobApplicationManager.updateJobApplications(new JobApplication());
		int expectedStatusCode = Status.NOT_FOUND.getStatusCode();
		int actualStatusCode = result.getStatus();
		Assert.assertTrue(
				"Invalid status code.\nExpected: " + expectedStatusCode + "\n Actual code: " + actualStatusCode,
				expectedStatusCode == actualStatusCode);
		Assert.assertTrue("Response entity should not be null.", result.getEntity() != null);
	}

	@Test
	public void testUpdateJobApplicationWithValidId() {
		Mockito.when(beanValidatorUtility.validateBean(Mockito.any(JobApplication.class)))
				.thenReturn(new ArrayList<String>());
		Mockito.when(jobApplicationDao.getJobApplicationsById(Mockito.anyInt())).thenReturn(new JobApplication());
		Mockito.doNothing().when(jobApplicationDao).updateOne(Mockito.any(JobApplication.class), Mockito.any());
		Response result = jobApplicationManager.updateJobApplications(new JobApplication());
		int expectedStatusCode = Status.OK.getStatusCode();
		int actualStatusCode = result.getStatus();
		Assert.assertTrue(
				"Invalid status code.\nExpected: " + expectedStatusCode + "\n Actual code: " + actualStatusCode,
				expectedStatusCode == actualStatusCode);
		Assert.assertTrue("Response entity should not be null.", result.getEntity() != null);
	}

}
