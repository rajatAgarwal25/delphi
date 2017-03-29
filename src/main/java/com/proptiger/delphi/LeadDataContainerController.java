package com.proptiger.delphi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proptiger.delphi.service.LeadService;

@Controller
@RequestMapping(value = "/leadDataContainers/")
public class LeadDataContainerController {

    private static Logger                logger = LoggerFactory.getLogger(LeadDataContainerController.class);

    @Autowired
    private LeadService                  leadService;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    // TODO change to post
    @ResponseBody
    public String persistLeadDataController() {
        leadService.fetchLeadsAndSerialize(11967177);
        return "hello-world";
    }
}
