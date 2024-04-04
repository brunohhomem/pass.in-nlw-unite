package com.rs.passin.services;

import com.rs.passin.domain.attendee.Attendee;
import com.rs.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.rs.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.rs.passin.domain.checkin.CheckIn;
import com.rs.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.rs.passin.dto.attendee.AttendeeDetails;
import com.rs.passin.dto.attendee.AttendeesListResponseDTO;
import com.rs.passin.dto.attendee.AttendeeBadgeDTO;
import com.rs.passin.repositories.AttendeeRepository;
import com.rs.passin.repositories.CheckinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = checkInService.getCheckIn(attendee.getId());

            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);

            return new AttendeeDetails(attendee.getId(),
                    attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();

    return new AttendeesListResponseDTO(attendeeDetailsList);
    }

    public void verifyAttendeeSubscription(String eventId, String email){
        Optional<Attendee> isAttendeeRegistered = attendeeRepository.findByEventIdAndEmail(eventId, email);

        if (isAttendeeRegistered.isPresent())
            throw new AttendeeAlreadyExistException("Attendee is already registered.");
    }

    public Attendee registerAttendee(Attendee newAttendee){
        return attendeeRepository.save(newAttendee);
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        Attendee attendee = getAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in")
                .buildAndExpand(attendeeId).toUri().toString();

        AttendeeBadgeDTO newAttendeeBadgeDTO = new AttendeeBadgeDTO(
                attendee.getName(),
                attendee.getEmail(),
                uri,
                attendee.getEvent().getId()
        );
        return new AttendeeBadgeResponseDTO(newAttendeeBadgeDTO);
    }

    public void checkInAttendee(String attendeeId){
        Attendee attendee = getAttendee(attendeeId);
        checkInService.registerCheckIn(attendee);
    }

    public Attendee getAttendee(String attendeeId){
        return attendeeRepository.findById(attendeeId)
                .orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with Id: " + attendeeId));
    }
}
