package model;

import java.time.LocalDate;
import java.util.List;

public class Lesson {
	private Integer totalHours;
	private LocalDate day;
	private Double pricePerHour;
	private ClassState state;
	private String description;
	private List<Payment> payment;
	private Commission commission;
	private Subject subject;
}
