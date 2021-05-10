package ars.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor @Getter @Setter 
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Session {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private LocalDate date;
	private int startTime; // 0 - 24
	private int duration; // in minutes?
	private String location;

	@ManyToOne
	@JoinColumn(name="provider_id")
	private Person provider;

	@OneToMany(mappedBy="session")
	private List<Appointment> appointmentRequests;

}
