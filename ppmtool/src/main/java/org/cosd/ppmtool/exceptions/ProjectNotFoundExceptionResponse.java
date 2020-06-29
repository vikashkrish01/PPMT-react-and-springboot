package org.cosd.ppmtool.exceptions;

public class ProjectNotFoundExceptionResponse {

	private String projectNotFound;

	public String getProjectNotFound() {
		return projectNotFound;
	}

	public void setProjectNotFound(String projectNotFound) {
		this.projectNotFound = projectNotFound;
	}

	public ProjectNotFoundExceptionResponse(String projectNotFound) {
		super();
		this.projectNotFound = projectNotFound;
	}
	
	
}
