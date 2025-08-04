package com.example;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * CancellationService is responsible for handling the cancellation of bookings.
 * 
 * It interacts with BookingRepository to find and delete bookings 
 * and updates the timetables of equipment and DJs accordingly.
 * 
 * It also processes refunds through the RefundService, 
 * according to the card details in PaymentInfo.
 */

public class CancellationService {

    private final BookingRepository bookingRepository;
    private final AssetTimetableService timetableService;
    private final RefundService refundService;

    public CancellationService(BookingRepository bookingRepository, AssetTimetableService timetableService, RefundService refundService) {
        this.bookingRepository = bookingRepository;
        this.timetableService = timetableService;
        this.refundService = refundService;
    }

    /**
     * Cancels the booking which corresponds to the given 'bookingID' identifier.
     * 
     * @param bookingId The identifier of the booking to cancel.
     * @return
     */
    public boolean cancelBooking(String bookingId, PaymentInfo paymentInfo) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            return false; // Exception: booking not found.
        }
        Booking booking = bookingOpt.get();

        // Removes booking from the repository.
        bookingRepository.getAllBookings().remove(booking);

        // Updates the timetables for equipment and DJs.
        Date eventDate = booking.getEvent().getDate();
        List<Equipment> equipmentList = booking.getEquipmentList();
        List<DJ> djList = booking.getDjList();

        removeAssetDates(equipmentList, eventDate, true);
        removeAssetDates(djList, eventDate, false);

        // Process refund
        refundService.processRefund(booking, paymentInfo);

        return true;
    }

    // Helper Method: Removes the event date from the equipment/DJ timetables.
    private void removeAssetDates(List<?> assets, Date eventDate, boolean isEquipment) {
        for (Object asset : assets) {
            String id = isEquipment ? ((Equipment) asset).getId() : ((DJ) asset).getId();
            if (isEquipment) {
                timetableService.equipmentTimetable
                    .computeIfPresent(id, (k, v) -> { v.remove(eventDate); return v; });
            } else {
                timetableService.djTimetable
                    .computeIfPresent(id, (k, v) -> { v.remove(eventDate); return v; });
            }
        }
    }
}