package iperette.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import iperette.domain.Account;
import iperette.domain.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
	List<Booking> findByBooker(Account booker);

	@Query(value = "SELECT * FROM booking WHERE (?1 < start_date AND start_date < ?2) OR  (?1 < end_date AND end_date < ?2)", nativeQuery = true)
	List<Booking> findBookingsThatOverlaps(ZonedDateTime zonedDateTime, ZonedDateTime zonedDateTime2);

}
