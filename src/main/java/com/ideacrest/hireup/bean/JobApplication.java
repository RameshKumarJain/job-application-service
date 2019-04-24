package com.ideacrest.hireup.bean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class JobApplication {

	@NotNull
	@Min(1)
	private long _id;

	@NotNull
	@Min(1)
	private long submittedBy;

	@NotNull
	@Min(1)
	private long jobOpeningId;

	@NotBlank
	private String coverNote;

	@NotBlank
	private String resumeURL;

	@NotBlank
	private String quotedPrice;

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public long getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(long submittedBy) {
		this.submittedBy = submittedBy;
	}

	public long getJobOpeningId() {
		return jobOpeningId;
	}

	public void setJobOpeningId(long jobOpeningId) {
		this.jobOpeningId = jobOpeningId;
	}

	public String getCoverNote() {
		return coverNote;
	}

	public void setCoverNote(String coverNote) {
		this.coverNote = coverNote;
	}

	public String getResumeURL() {
		return resumeURL;
	}

	public void setResumeURL(String resumeURL) {
		this.resumeURL = resumeURL;
	}

	public String getQuotedPrice() {
		return quotedPrice;
	}

	public void setQuotedPrice(String quotedPrice) {
		this.quotedPrice = quotedPrice;
	}
}
