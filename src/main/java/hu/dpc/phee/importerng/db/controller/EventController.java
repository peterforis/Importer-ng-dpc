package hu.dpc.phee.importerng.db.controller;

import hu.dpc.phee.importerng.db.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class EventController {

    @Autowired
    private EventService eventService;

}
