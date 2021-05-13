package ars.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor @Getter @Setter 
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Appointment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private LocalDate createdDate;
	private LocalTime createdTime;
	private LocalDate confirmedDate;
	
	@Enumerated(EnumType.STRING)
	private Status status = Status.PENDING;
	
	@ManyToOne
	@JoinColumn(name="session_id")
	private Session session;
	
	@ManyToOne
	@JoinColumn(name="client_id")
	private Person client;
	
	public Appointment(LocalDate createdDate, Person client, Session session) {
		this.createdDate = createdDate;
		this.client = client;
		this.session = session;
	}
}
