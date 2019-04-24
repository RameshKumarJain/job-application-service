package com.ideacrest.hireup.dao;

import static com.mongodb.client.model.Filters.eq;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.ideacrest.app.mongo.MongoService;
import com.ideacrest.hireup.bean.JobApplication;
import com.mongodb.client.MongoDatabase;

public class JobApplicationDao extends MongoService<JobApplication> {

	public static final String JOB_APPLICATION_COLLECTION_NAME = "job_applications";

	public static final String ID = "_id";

	@Inject
	public JobApplicationDao(MongoDatabase db, ObjectMapper objectMapper) {
		super(db, objectMapper, JOB_APPLICATION_COLLECTION_NAME, JobApplication.class);
	}

	public JobApplication getJobApplicationsById(long id) {
		return findOneByKey(eq(ID, id));
	}

	public List<JobApplication> getAllJobApplications() {
		return find();
	}

	public void addJobApplication(JobApplication jobApplicationBean) {
		insertOne(jobApplicationBean);
	}

	public void updateJobApplication(JobApplication jobApplicationBean) {
		updateOne(jobApplicationBean, jobApplicationBean.get_id());
	}
}