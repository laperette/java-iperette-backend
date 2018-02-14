package iperette.domain;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "BOOKER_ID")
	private Account booker;

	@Column(nullable = false)
	private ZonedDateTime startDate;

	@Column(nullable = false)
	private ZonedDateTime endDate;

	@Column(nullable = false)
	private Integer nbOfGuests;
	
	@Enumerated(EnumType.STRING)
	private BookingStatus status;

	@Column(name = "created_at")
	public LocalDateTime createdAt;

	@Column(name = "updated_at")
	public LocalDateTime updatedAt;

	public Booking() {
		this.status = BookingStatus.PENDING;
	}
	
	@PrePersist
	void createdAt() {
		this.createdAt = this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	void updatedAt() {
		this.updatedAt = LocalDateTime.now();
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Account getBooker() {
		return booker;
	}

	public void setBooker(Account booker) {
		this.booker = booker;
	}

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}

	public ZonedDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(ZonedDateTime endDate) {
		this.endDate = endDate;
	}

	public Integer getNbOfGuests() {
		return nbOfGuests;
	}

	public void setNbOfGuests(Integer nbOfGuests) {
		this.nbOfGuests = nbOfGuests;
	}

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}
}
