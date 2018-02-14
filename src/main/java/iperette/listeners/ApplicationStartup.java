package iperette.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iperette.domain.Account;
import iperette.domain.Booking;
import iperette.domain.Role;
import iperette.repository.AccountRepository;
import iperette.repository.BookingRepository;

@Component
public class ApplicationStartup {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartup.class);

	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private BookingRepository bookingRepo;

	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;

	@EventListener(ApplicationReadyEvent.class)
	public void initDatabase() {
		// Accounts :
		Account alex = new Account("alex.beg@compte.com", bcryptPasswordEncoder.encode("password"));
		alex.setFirstname("alex");
		alex.setLastname("beg");
		alex.setRole(Role.ADMIN);
		accountRepo.save(alex);

		Account john = new Account("john.doe@compte.com", bcryptPasswordEncoder.encode("password"));
		john.setFirstname("John");
		john.setLastname("Doe");
		accountRepo.save(john);

		Account jack = new Account("jack.brown@compte.com", bcryptPasswordEncoder.encode("password"));
		jack.setFirstname("Jack");
		jack.setLastname("Brown");
		accountRepo.save(jack);

		// Bookings :
		ZonedDateTime firstDay = ZonedDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0);
		
		Booking booking = new Booking();
		booking.setBooker(john);
		booking.setStartDate(firstDay);
		booking.setEndDate(firstDay.plusDays(5));
		booking.setNbOfGuests(10);
		bookingRepo.save(booking);

		Booking booking2 = new Booking();
		booking2.setBooker(jack);
		booking2.setStartDate(firstDay.plusDays(6));
		booking2.setEndDate(firstDay.plusDays(10));
		booking2.setNbOfGuests(4);
		bookingRepo.save(booking2);

		LOGGER.debug("DataBase initialization done");
	}
}
