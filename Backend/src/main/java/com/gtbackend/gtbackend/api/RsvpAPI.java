package com.gtbackend.gtbackend.api;

import com.gtbackend.gtbackend.dao.EventRepository;
import com.gtbackend.gtbackend.dao.RsvpRepository;
import com.gtbackend.gtbackend.model.Event;
import com.gtbackend.gtbackend.model.Rsvp;
import com.gtbackend.gtbackend.model.RsvpStatus;
import com.gtbackend.gtbackend.service.RsvpService;
import org.aspectj.weaver.ast.Literal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")

public class RsvpAPI {
    private RsvpRepository rsvpRepository;
    private EventRepository eventRepository;
    private RsvpService rsvpService;

    @Autowired
    public RsvpAPI(RsvpRepository rsvpRepository, EventRepository eventRepository, RsvpService rsvpService){
        this.rsvpRepository = rsvpRepository;
        this.eventRepository = eventRepository;
        this.rsvpService = rsvpService;
    }

    @PatchMapping("/updateRsvp")
    public void updateRsvp(Principal principal, @RequestBody Map<String, String> body) throws NoSuchElementException, IllegalArgumentException, IllegalAccessException{
        RsvpStatus status = RsvpStatus.valueOf(body.get("status").toUpperCase());
        long event_id = Long.valueOf(body.get("event_id"));
        Optional<Event> tmp_event = eventRepository.findById(event_id);
        List<Rsvp> tmp_rsvp = rsvpRepository.getRsvpEmail(event_id, principal.getName());
        if(status.equals(RsvpStatus.INVITED)){
            throw new IllegalAccessException();
        }
        if(tmp_event.isEmpty()){
            throw new NoSuchElementException();
        }
        if(!tmp_rsvp.isEmpty() && tmp_rsvp.get(0).getStatus().equals(status)){
            return;
        }
        Event event = tmp_event.get();
        if(event.isInviteOnly() && status.equals(RsvpStatus.DELETE)){
            rsvpRepository.updateStatus(event_id, principal.getName(), RsvpStatus.INVITED);
            return;
        }
        if(status.equals(RsvpStatus.DELETE)){
            rsvpRepository.deleteRsvp(event_id, principal.getName());
            return;
        }
        if(status.equals(RsvpStatus.WILLATTEND)){
            rsvpService.updateWillAttend(event_id, principal.getName(), event);
            return;
        }
        if(tmp_rsvp.isEmpty()){
            if(!event.isInviteOnly()){
                addRsvp(event_id, principal.getName(), status);
            }
            return;
        }
        rsvpRepository.updateStatus(event_id, principal.getName(), status);
    }

    public void addRsvp(long event_id, String email, RsvpStatus status){
        Rsvp rsvp = new Rsvp(event_id, status, email);
        rsvpRepository.save(rsvp);
    }

    @GetMapping("/getRsvp")
    @ResponseBody
    public List<Rsvp> getRsvp(@RequestBody Map<String, String> body) throws NoSuchElementException, IllegalArgumentException{
        long event_id = Long.valueOf(body.get("event_id"));
        if(eventRepository.findById(event_id).isEmpty()){
            throw new NoSuchElementException();
        }
        if(body.get("status").toUpperCase().equals("ALL")){
            return rsvpRepository.getRsvp(event_id);
        }
        RsvpStatus status = RsvpStatus.valueOf(body.get("status").toUpperCase());
        return rsvpRepository.getRsvpByStatus(event_id, status);
    }

    @GetMapping("/getRsvpStatus")
    public String getRsvpStatus(Principal principal, @RequestParam String id) throws NoSuchElementException, IllegalArgumentException{
        long event_id = Long.valueOf(id);
        List<Rsvp> tmp_rsvp = rsvpRepository.getRsvpEmail(event_id, principal.getName());
        if(tmp_rsvp.isEmpty()){
            return "NORSVP";
        }
        return tmp_rsvp.get(0).getStatus().toString();
    }

}
