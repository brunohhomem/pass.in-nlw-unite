package com.rs.passin.controllers;

import com.rs.passin.dto.attendee.AttendeeIdDTO;
import com.rs.passin.dto.attendee.AttendeeRequestDTO;
import com.rs.passin.dto.attendee.AttendeesListResponseDTO;
import com.rs.passin.dto.event.EventIdDto;
import com.rs.passin.dto.event.EventRequestDTO;
import com.rs.passin.dto.event.EventResponseDTO;
import com.rs.passin.services.AttendeeService;
import com.rs.passin.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id){
        EventResponseDTO event = eventService.getEventDetail(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/attendees/{id}")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String id){
        AttendeesListResponseDTO attendeesListResponseDTO = attendeeService.getEventsAttendee(id);
        return ResponseEntity.ok(attendeesListResponseDTO);
    }

    @PostMapping
    public ResponseEntity<EventIdDto> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDto eventIdDto = eventService.createEvent(body);
        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDto.id()).toUri();
        return ResponseEntity.created(uri).body(eventIdDto);
    }

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerAttendee(@PathVariable String eventId, @RequestBody AttendeeRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO attendeeIdDTO = eventService.registerAttendeeOnEvent(eventId, body);
        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge")
                .buildAndExpand(attendeeIdDTO.id()).toUri();
        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }

}
