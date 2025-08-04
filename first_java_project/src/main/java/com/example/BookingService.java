package com.example;

import java.util.Date;
import java.util.List;

/**
 * BookingService is responsible for managing bookings.
 * 
 * This includes creating new bookings in the BookingRepository, checking for conflicts,
 * updating timetables for equipment/DJs with AssetTimetableService, 
 * and calling paymentService and emailService to process transactions and send confirmation emails.
 */

public class BookingService {

    private BookingRepository bookingRepository;
    private AssetTimetableService timetableService;
    private UserRepository userRepository;
    private PaymentService paymentService;
    private EmailService emailService;

    public BookingService(BookingRepository bookingRepository,
                          AssetTimetableService timetableService,
                          UserRepository userRepository,
                          PaymentService paymentService,
                          EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.timetableService = timetableService;
        this.userRepository = userRepository;
        this.paymentService = paymentService;
        this.emailService = emailService;
    }

    public Booking registerBooking(EventDetails event,
                                   List<Equipment> equipmentList,
                                   List<DJ> djList,
                                   List<Date> monitorDates,
                                   String userId,
                                   PaymentInfo paymentInfo,
                                   List<User> stakeholders) throws BookingException {

        // Checks for an existing booking.
        if (bookingRepository.existsByEventDetails(event)) {
            throw new BookingException("Booking already exists with identical event details.");
        }

        // Checks for date conflicts.
        for (Equipment equipment : equipmentList) {
            if (timetableService.hasDateConflict(equipment, event.getDate(), monitorDates)) {
                throw new BookingException("Equipment is unavailable due to date conflict.");
            }
        }

        for (DJ dj : djList) {
            if (timetableService.hasDateConflict(dj, event.getDate(), monitorDates)) {
                throw new BookingException("DJ is unavailable due to date conflict.");
            }
        }

        // Retrieves user by their unique identifier, userID.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BookingException("User not found."));

        // Creates new booking in the repository.
        String bookingId = IdGenerator.generateUniqueId("BOOK");
        Booking booking = new Booking(bookingId, event, equipmentList, djList, user);
        bookingRepository.save(booking);

        // Updates equipment/DJ timetables with AssetTimetableService.
        timetableService.updateAssetTimetable(booking, List.copyOf(equipmentList), event.getDate());
        timetableService.updateAssetTimetable(booking, List.copyOf(djList), event.getDate());

        // Calls paymentService to process payment.
        boolean paymentSuccess = paymentService.processPayment(booking, paymentInfo);
        if (!paymentSuccess) {
            throw new BookingException("Payment failed. Booking aborted.");
        }

        // Calls emailService to send confirmation email.
        emailService.sendConfirmationEmail(booking, user, stakeholders);

        return booking;
    }
}
