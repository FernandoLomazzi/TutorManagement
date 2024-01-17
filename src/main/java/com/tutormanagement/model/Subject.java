package com.tutormanagement.model;

import java.util.List;

public class Subject {
	private String name;
	private Institution institution;

	public Subject(String name, Institution institution) {
		this.name = name;
		this.institution = institution;
	}

	public String getName() {
		return name;
	}

	public Institution getInstitution() {
		return institution;
	}

	public String getInstitutionString() {
		return institution.toString();
	}

	@Override
	public String toString() {
		return name + " (" + institution.toString() + ")";
	}
}
