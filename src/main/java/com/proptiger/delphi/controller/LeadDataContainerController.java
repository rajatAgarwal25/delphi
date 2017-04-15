package com.proptiger.delphi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proptiger.delphi.dto.APIResponse;
import com.proptiger.delphi.service.LeadService;

@Controller
@RequestMapping(value = "/")
@Api(value = "LeadDataContainerController")
public class LeadDataContainerController {

    private static Logger LOGGER = LoggerFactory.getLogger(LeadDataContainerController.class);

    @Autowired
    private LeadService   leadService;

    @ApiOperation(
            value = "Fetches leads data from mysql and persists for local computation.",
            notes = "Fetches leads data from mysql and persists for local computation.")
    @RequestMapping(value = "leadDataContainers", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse persistLeadDataController(@ApiParam(
            value = "Max lead ID from which fetching and persisting has to start",
            required = true) @PathVariable Integer leadId) {
        LOGGER.debug("Starting serialization");
        leadService.fetchLeadsAndSerialize(leadId);
        return new APIResponse();
    }
}
