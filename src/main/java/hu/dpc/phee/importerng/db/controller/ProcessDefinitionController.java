package hu.dpc.phee.importerng.db.controller;

import hu.dpc.phee.importerng.db.service.ProcessDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProcessDefinitionController {

    @Autowired
    private ProcessDefinitionService processDefinitionService;

}
