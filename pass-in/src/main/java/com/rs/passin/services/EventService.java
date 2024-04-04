package com.rs.passin.services;

import com.rs.passin.domain.attendee.Attendee;
import com.rs.passin.domain.event.Event;
import com.rs.passin.domain.event.exceptions.EventFullException;
import com.rs.passin.domain.event.exceptions.EventNotFoundException;
import com.rs.passin.dto.attendee.AttendeeIdDTO;
import com.rs.passin.dto.attendee.AttendeeRequestDTO;
import com.rs.passin.dto.event.EventIdDto;
import com.rs.passin.dto.event.EventRequestDTO;
import com.rs.passin.dto.event.EventResponseDTO;
import com.rs.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetail(String eventId){
        Event event = getEventById(eventId);

        List<Attendee> attendeeList = attendeeService.getAllAttendeesFromEvent(eventId);

        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventIdDto createEvent(EventRequestDTO dto){
        Event newEvent = new Event();

        newEvent.setTitle(dto.title());
        newEvent.setDetails(dto.details());
        newEvent.setMaximumAttendees(dto.maximumAttendees());
        newEvent.setSlug(createSlug(dto.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDto(newEvent.getId());
    }


    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO){
        attendeeService.verifyAttendeeSubscription(eventId, attendeeRequestDTO.email());

        Event event = getEventById(eventId);

        List<Attendee> attendeeList = attendeeService.getAllAttendeesFromEvent(eventId);

        if (event.getMaximumAttendees() <= attendeeList.size()) throw new EventFullException("Event is full");

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());

        attendeeService.registerAttendee(newAttendee);

        return new AttendeeIdDTO(newAttendee.getId());
    }

    private Event getEventById (String eventId){
        return this.eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
    }

    private String createSlug(String text){
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }
}
