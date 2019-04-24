package com.ideacrest.hireup.mgmt;

import java.util.List;

import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ideacrest.app.validator.BeanValidatorUtility;
import com.ideacrest.hireup.bean.JobApplication;
import com.ideacrest.hireup.dao.JobApplicationDao;

@Singleton
public class JobApplicationManager {

	private JobApplicationDao jobApplicationDao;

	private BeanValidatorUtility beanValidatorUtility;

	@Inject
	public JobApplicationManager(JobApplicationDao jobApplicationDao, BeanValidatorUtility beanValidatorUtility) {
		this.jobApplicationDao = jobApplicationDao;
		this.beanValidatorUtility = beanValidatorUtility;
	}

	public Response getJobApplicationsById(long id) {
		JobApplication jobApplicationBean = jobApplicationDao.getJobApplicationsById(id);
		if (jobApplicationBean == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Job application with id : " + id + " does not exist").build();
		}
		return Response.status(Response.Status.OK).entity(jobApplicationBean).build();
	}

	public Response getAllJobApplications() {
		List<JobApplication> jobApplications;
		jobApplications = jobApplicationDao.getAllJobApplications();
		return Response.status(Response.Status.OK).entity(jobApplications).build();
	}

	public Response addJobApplications(JobApplication jobApplicationBean) {

		List<String> validationMessage = beanValidatorUtility.validateBean(jobApplicationBean);
		if (validationMessage.size() > 0) {
			return validationErrorResponse(validationMessage);
		}
		return addJobApplicationsAndGetResponse(jobApplicationBean);

	}

	public Response updateJobApplications(JobApplication jobApplicationBean) {

		List<String> validationMessage = beanValidatorUtility.validateBean(jobApplicationBean);
		if (validationMessage.size() > 0) {
			return validationErrorResponse(validationMessage);
		}
		return updateJobApplicationsAndGetResponse(jobApplicationBean);

	}

	private Response addJobApplicationsAndGetResponse(JobApplication jobApplicationBean) {
		JobApplication jobApplicationInDB = jobApplicationDao.getJobApplicationsById(jobApplicationBean.get_id());
		if (jobApplicationInDB == null) {
			jobApplicationDao.insertOne(jobApplicationBean);
			return Response.status(Response.Status.OK).entity(jobApplicationBean.get_id()).build();
		}
		return Response.status(Response.Status.BAD_REQUEST)
				.entity("Jobs with id : " + jobApplicationBean.get_id() + " already exist").build();
	}

	private Response updateJobApplicationsAndGetResponse(JobApplication jobApplicationBean) {
		JobApplication jobApplicationInDB = jobApplicationDao.getJobApplicationsById(jobApplicationBean.get_id());
		if (jobApplicationInDB == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Jobs with id : " + jobApplicationBean.get_id() + " does not exist").build();
		}
		jobApplicationDao.updateOne(jobApplicationBean, jobApplicationBean.get_id());
		return Response.status(Response.Status.OK).entity(jobApplicationBean.get_id()).build();
	}

	private Response validationErrorResponse(List<String> validationMessage) {
		return Response.status(Response.Status.BAD_REQUEST).entity(validationMessage).build();
	}

}
