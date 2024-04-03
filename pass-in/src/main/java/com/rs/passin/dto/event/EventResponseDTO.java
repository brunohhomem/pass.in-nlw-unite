package com.rs.passin.dto.event;

import com.rs.passin.domain.event.Event;
import lombok.Getter;

@Getter
public class EventResponseDTO {
    EventDetailDTO event;

    public EventResponseDTO(Event event, Integer numberOFAttendees){
        this.event = new EventDetailDTO(event.getId(),
                event.getTitle(),
                event.getSlug(),
                event.getDetails(),
                event.getMaximumAttendees(),
                numberOFAttendees);
    }

}
