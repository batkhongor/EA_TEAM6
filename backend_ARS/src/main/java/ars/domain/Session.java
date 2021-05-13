package ars.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Session {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	@Future
	private LocalDate date;

	@Column(nullable = false)
	@NotNull
	private LocalTime startTime; // 0 - 24

	@Column(nullable = false)
	@NotNull
	@Max(value = 60)
	@Min(value = 1)
	private int duration; // in minutes

	@Column(length = 50, nullable = false)
	@NotNull
	private String location;

	@ManyToOne
	@JoinColumn(name = "provider_id", nullable = false)
	private Person provider;
	
	@JsonIgnore
	@OneToMany(mappedBy = "session")
	private List<Appointment> appointmentRequests;

	public Session(@Future LocalDate date, @NotNull LocalTime startTime, @NotNull @Max(60) @Min(1) int duration,
			@NotNull String location, Person provider) {
		super();
		this.date = date;
		this.startTime = startTime;
		this.duration = duration;
		this.location = location;
		this.provider = provider;
	}

}
