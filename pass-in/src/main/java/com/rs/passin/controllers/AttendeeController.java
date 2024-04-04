package com.rs.passin.controllers;

import com.rs.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.rs.passin.services.AttendeeService;
import com.rs.passin.services.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class AttendeeController {

    private final AttendeeService attendeeService;


    @GetMapping("/{attendeeId}/badge")
    public ResponseEntity<AttendeeBadgeResponseDTO> getAttendeeBadge(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        AttendeeBadgeResponseDTO responseDTO = attendeeService.getAttendeeBadge(attendeeId, uriComponentsBuilder);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("{attendeeId}/check-in")
    public ResponseEntity registerCheckIn(@PathVariable String attendeeId,
                                          UriComponentsBuilder uriComponentsBuilder){
        attendeeService.checkInAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendee/{attendeeId}/badge")
                .buildAndExpand(attendeeId).toUri();

        return ResponseEntity.created(uri).build();
    }
}
