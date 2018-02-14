package iperette.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iperette.domain.Booking;
import iperette.exceptions.DatesAlreadyBookedExceptions;
import iperette.exceptions.NoGuestNumberGivenException;
import iperette.repository.BookingRepository;
import iperette.services.AccountService;

@RestController
@RequestMapping("/bookings")
public class BookingController {

	@Autowired
	private BookingRepository bookingRepo;

	@Autowired
	private AccountService accountSvc;

	@GetMapping
	public List<Booking> getAllBookings() {
		return bookingRepo.findAll();
	}

	@GetMapping(path = "/me")
	public List<Booking> getMyBookings() {
		return accountSvc.getUserBookings();
	}

	@PostMapping(path = "/creer")
	public boolean createBooking(@RequestBody Booking booking) throws DatesAlreadyBookedExceptions,NoGuestNumberGivenException {
		return accountSvc.addBooking(booking);
	}
	
	@DeleteMapping(path = "/{id}")
	public List<Booking> deleteBooking(@PathVariable("id") Integer bookingId) {
		bookingRepo.delete(bookingId);
		return getAllBookings();
	}
}
