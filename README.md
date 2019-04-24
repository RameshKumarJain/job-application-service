# job-application-service

How to start the job-application-service application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/sample-job-application-service-1.0.0.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

Rest Service
----

1 Get Job applications by id - GET
2 Add Job applications - POST
3 Update Job applications - PUT
4 Get all Job applications - GET

GraphQL service
-----

Single rest points where you query two different types/methods
	
	jobApplicationsById(_id: ID): JobApplications
	jobApplications: [JobApplications]

 Another GraphQL end point where you query the user and job openings data
 	
	jobApplicationsById(_id: ID): JobApplications
	jobApplications: [JobApplications]

In above type query you can ask for the user details and job opening data associated with the Job Application.