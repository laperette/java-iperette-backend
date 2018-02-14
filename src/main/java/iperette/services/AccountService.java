package iperette.services;

import static iperette.security.SecurityConstants.EXPIRATION_TIME;
import static iperette.security.SecurityConstants.SECRET;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import iperette.domain.Account;
import iperette.domain.Booking;
import iperette.exceptions.DatesAlreadyBookedExceptions;
import iperette.exceptions.NoGuestNumberGivenException;
import iperette.repository.AccountRepository;
import iperette.repository.BookingRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private BookingRepository bookingRepo;

	public List<Booking> getUserBookings() {
		return bookingRepo.findByBooker(getAuthenticatedUser());
	}

	public Account getAuthenticatedUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return accountRepo.findByEmail(email);
	}

	public boolean addBooking(Booking booking) throws DatesAlreadyBookedExceptions, NoGuestNumberGivenException {
		List<Booking> otherBookingsAtTheseDates = bookingRepo.findBookingsThatOverlaps(booking.getStartDate(),
				booking.getEndDate());
		if (otherBookingsAtTheseDates.isEmpty()) {
			booking.setBooker(getAuthenticatedUser());
			if (null == booking.getNbOfGuests() || booking.getNbOfGuests().equals(0)) {
				throw new NoGuestNumberGivenException("");
			}
			bookingRepo.save(booking);
			return true;
		}
		throw new DatesAlreadyBookedExceptions("");
	}

	public String makeToken(String email, String role) {
		String token = Jwts.builder().setSubject(email).claim("role", role)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();
		return token;
	}

}
