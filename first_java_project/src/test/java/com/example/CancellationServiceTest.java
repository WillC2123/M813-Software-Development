package com.example;

/**
 * CancellationServiceTest tests the booking cencellation functionality.
 * It includes tests for successful cancellation, the handling of non-existent bookings
 * and ensures that asset dates are removed from DJ/Equipment asset timetables.
 * It uses JUnit 5 for testing and Mockito for dummy variables.
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CancellationServiceTest {

    private CancellationService cancellationService;
    private BookingRepository bookingRepository;
    private AssetTimetableService timetableService;
    private RefundService refundService;
    private UserRepository userRepository;
    private User testUser;
    private PaymentInfo paymentInfo;

    @BeforeEach
    void setUp() {
        bookingRepository = new BookingRepository();
        timetableService = new AssetTimetableService();
        refundService = mock(RefundService.class);
        userRepository = new UserRepository();
        cancellationService = new CancellationService(bookingRepository, timetableService, refundService);

        testUser = new User("u1", "Test User");
        userRepository.addUser(testUser);
        paymentInfo = new PaymentInfo("1234", "Test User", "12/25", "123");
    }

    @Test
    void testCancelBookingSuccess() {

        Date eventDate = new Date();
        Equipment equipment = new Equipment("eq1", "Speakers");
        DJ dj = new DJ("dj1", "DJ Test");
        EventDetails event = new EventDetails("Party", eventDate, "Venue");
        Booking booking = new Booking("b1", event, List.of(equipment), List.of(dj), testUser);
        bookingRepository.save(booking);

        // Add event date to timetables
        timetableService.updateAssetTimetable(booking, List.of(equipment), eventDate);
        timetableService.updateAssetTimetable(booking, List.of(dj), eventDate);

        when(refundService.processRefund(any(), any())).thenReturn(true);

        boolean result = cancellationService.cancelBooking("b1", paymentInfo);

        assertTrue(result);
        assertFalse(bookingRepository.findById("b1").isPresent());
        assertFalse(timetableService.equipmentTimetable.get("eq1").contains(eventDate));
        assertFalse(timetableService.djTimetable.get("dj1").contains(eventDate));
        verify(refundService, times(1)).processRefund(eq(booking), eq(paymentInfo));
    }

    @Test
    void testCancelBookingNotFound() {
        boolean result = cancellationService.cancelBooking("nonexistent", paymentInfo);
        assertFalse(result);
        verify(refundService, never()).processRefund(any(), any());
    }

    @Test
    void testCancelBookingRemovesOnlyRelevantDates() {

        Calendar cal = Calendar.getInstance();
        Date eventDate1 = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date eventDate2 = cal.getTime();

        Equipment equipment = new Equipment("eq1", "Speakers");
        DJ dj = new DJ("dj1", "DJ Test");
        EventDetails event = new EventDetails("Party", eventDate1, "Venue");
        Booking booking = new Booking("b2", event, List.of(equipment), List.of(dj), testUser);
        bookingRepository.save(booking);

        // Add dates to timetables
        timetableService.updateAssetTimetable(booking, List.of(equipment), eventDate1);
        timetableService.updateAssetTimetable(booking, List.of(equipment), eventDate2);
        timetableService.updateAssetTimetable(booking, List.of(dj), eventDate1);
        timetableService.updateAssetTimetable(booking, List.of(dj), eventDate2);

        when(refundService.processRefund(any(), any())).thenReturn(true);

        boolean result = cancellationService.cancelBooking("b2", paymentInfo);

        assertTrue(result);
        assertFalse(timetableService.equipmentTimetable.get("eq1").contains(eventDate1));
        assertTrue(timetableService.equipmentTimetable.get("eq1").contains(eventDate2));
        assertFalse(timetableService.djTimetable.get("dj1").contains(eventDate1));
        assertTrue(timetableService.djTimetable.get("dj1").contains(eventDate2));
    }
}
