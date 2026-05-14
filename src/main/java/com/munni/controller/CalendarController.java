package com.munni.controller;

import com.munni.model.CalendarEvent;
import com.munni.repository.CalendarEventRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/calendar")
public class CalendarController {

    private final CalendarEventRepository repo;

    public CalendarController(CalendarEventRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String calendar(Model model, Authentication auth) {
        model.addAttribute("currentUser", auth.getName());
        return "admin/calendar";
    }

    @GetMapping("/events")
    @ResponseBody
    public List<CalendarEvent> getEvents(@RequestParam(defaultValue = "all") String filter) {
        if ("all".equals(filter)) {
            return repo.findAllByOrderByEventDateAsc();
        }
        return repo.findByCreatedByOrderByEventDateAsc(filter);
    }

    @PostMapping("/events")
    @ResponseBody
    public CalendarEvent addEvent(@RequestBody Map<String, String> body, Authentication auth) {
        CalendarEvent event = new CalendarEvent();
        event.setTitle(body.get("title"));
        event.setDescription(body.get("description"));
        event.setEventDate(LocalDate.parse(body.get("eventDate")));
        event.setCreatedBy(auth.getName());
        return repo.save(event);
    }

    @DeleteMapping("/events/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id, Authentication auth) {
        repo.findById(id).ifPresent(e -> {
            if (e.getCreatedBy().equals(auth.getName())) {
                repo.delete(e);
            }
        });
        return ResponseEntity.ok().build();
    }
}
