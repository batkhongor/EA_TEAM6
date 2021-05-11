package ars.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.*;

import ars.domain.Appointment;
import ars.domain.Person;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@NoArgsConstructor @Getter @Setter
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
	@DecimalMin(value="0",inclusive=true)
	@DecimalMax(value="24",inclusive=false)
	private Integer startTime; // 0 - 24



	@Column(nullable = false)
	@NotNull
	@Max(value=60)
	@Min(value=1)
	private int duration; // in minutes


	@Column(length = 50, nullable = false)
	@NotNull
	private String location;

	@ManyToOne
	@JoinColumn(name="provider_id", nullable = false)
	private Person provider;

	@OneToMany(mappedBy="session")
	private List<Appointment> appointmentRequests;

}
