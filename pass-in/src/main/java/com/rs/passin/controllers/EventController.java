package com.rs.passin.controllers;

import com.rs.passin.dto.event.EventIdDto;
import com.rs.passin.dto.event.EventRequestDTO;
import com.rs.passin.dto.event.EventResponseDTO;
import com.rs.passin.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id){
        EventResponseDTO event = service.getEventDetail(id);

        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventIdDto> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDto eventIdDto = service.createEvent(body);

        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDto.id()).toUri();

        return ResponseEntity.created(uri).body(eventIdDto);
    }
}
