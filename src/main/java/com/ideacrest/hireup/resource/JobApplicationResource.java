package com.ideacrest.hireup.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.ideacrest.hireup.bean.JobApplication;
import com.ideacrest.hireup.mgmt.JobApplicationManager;

@Path("/job-applications")
@Produces(MediaType.APPLICATION_JSON)
public class JobApplicationResource {

	private JobApplicationManager jobApplicationManager;

	@Inject
	public JobApplicationResource(JobApplicationManager jobApplicationManager) {
		this.jobApplicationManager = jobApplicationManager;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addJobApplications(JobApplication jobApplicationBean) {
		return jobApplicationManager.addJobApplications(jobApplicationBean);

	}

	@GET
	public Response getJobApplicationsById(@QueryParam("id") long id) {
		return jobApplicationManager.getJobApplicationsById(id);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateJobApplications(JobApplication jobApplicationBean) {
		return jobApplicationManager.updateJobApplications(jobApplicationBean);
	}

	@GET
	@Path("all")
	public Response getAllJobApplications() {
		return jobApplicationManager.getAllJobApplications();
	}

}
