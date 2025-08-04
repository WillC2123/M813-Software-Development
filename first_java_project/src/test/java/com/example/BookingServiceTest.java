package com.example;

// This test class covers the BookingService functionality, including successful booking registration,
// handling of duplicate events, and asset conflict scenarios. It uses JUnit 5 for testing
// and Mockito for dummy variables.

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    private BookingRepository bookingRepository;
    private AssetTimetableService timetableService;
    private UserRepository userRepository;
    private PaymentService paymentService;
    private EmailService emailService;
    private BookingService bookingService;
    private User testUser;

    @BeforeEach
    void setUp() {
        bookingRepository = new BookingRepository();
        timetableService = new AssetTimetableService();
        userRepository = new UserRepository();
        paymentService = mock(PaymentService.class);
        emailService = mock(EmailService.class);

        bookingService = new BookingService(
            bookingRepository,
            timetableService,
            userRepository,
            paymentService,
            emailService
        );

        // Add a sample user to the repository
        testUser = new User("u1", "Test User");
        userRepository.addUser(testUser);
    }

    @Test
    void testRegisterBookingSuccess() throws BookingException {
        EventDetails event = new EventDetails("Party Night", new Date(), "Club Venue");
        List<Equipment> equipment = List.of(new Equipment("eq1", "Speakers"));
        List<DJ> djs = List.of(new DJ("dj1", "DJ Test"));
        List<Date> monitorDates = new ArrayList<>();
        PaymentInfo paymentInfo = mock(PaymentInfo.class);
        List<User> stakeholders = List.of(testUser);

        Booking booking = bookingService.registerBooking(event, equipment, djs, monitorDates, "u1", paymentInfo, stakeholders);

        assertNotNull(booking);
        assertEquals(event.getName(), booking.getEvent().getName());
        assertEquals("u1", booking.getUser().getUserId());
    }

    @Test
    void testRegisterBookingDuplicateEventFails() throws BookingException {
        Date date = new Date();
        EventDetails event = new EventDetails("Same Event", date, "Location A");
        List<Equipment> equipment = List.of(new Equipment("eq1", "Mic"));
        List<DJ> djs = List.of(new DJ("dj1", "DJ Repeat"));
        List<Date> monitorDates = new ArrayList<>();
        PaymentInfo paymentInfo = mock(PaymentInfo.class);
        List<User> stakeholders = List.of(testUser);

        // First booking
        bookingService.registerBooking(event, equipment, djs, monitorDates, "u1", paymentInfo, stakeholders);

        // Attempt duplicate
        BookingException ex = assertThrows(BookingException.class, () ->
                bookingService.registerBooking(event, equipment, djs, monitorDates, "u1", paymentInfo, stakeholders));

        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    void testRegisterBookingAssetConflictFails() throws BookingException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        Date bookingDate = calendar.getTime();

        Equipment equipment = new Equipment("eq1", "Lights");
        DJ dj = new DJ("dj1", "DJ Blocked");

        // Pre-block the asset with the same event date
        timetableService.updateAssetTimetable(null, List.of(equipment, dj), bookingDate);

        EventDetails event = new EventDetails("Blocked Event", bookingDate, "Club");
        List<Date> monitorDates = List.of(bookingDate);
        PaymentInfo paymentInfo = mock(PaymentInfo.class);
        List<User> stakeholders = List.of(testUser);

        BookingException ex = assertThrows(BookingException.class, () ->
                bookingService.registerBooking(event, List.of(equipment), List.of(dj), monitorDates, "u1", paymentInfo, stakeholders));

        assertTrue(ex.getMessage().contains("conflict"));
    }
}
