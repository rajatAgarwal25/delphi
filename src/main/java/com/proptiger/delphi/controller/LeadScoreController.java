package com.proptiger.delphi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.proptiger.delphi.dto.APIResponse;
import com.proptiger.delphi.dto.LeadScoreDTO;
import com.proptiger.delphi.model.lead.LeadData;
import com.proptiger.delphi.service.ModelService;

@Controller
@RequestMapping(value = "/")
@Api(value = "LeadScoreController")
public class LeadScoreController {

    private static Logger LOGGER = LoggerFactory.getLogger(LeadScoreController.class);

    @Autowired
    private ModelService  modelService;

    @ApiOperation(
            value = "Computes lead score for given lead information",
            notes = "Computes lead score for given lead information",
            response = LeadScoreDTO.class,
            produces = "application/json")
    @RequestMapping(value = "leadScore", method = RequestMethod.GET)
    public APIResponse getLeadScore(@ApiParam(
            value = "Details of lead for which score has to be calculated",
            required = true) @RequestParam LeadData leadData) {
        LOGGER.debug("Computing lead score for " + leadData);
        LeadScoreDTO leadScore = modelService.getLeadScore(leadData);
        LOGGER.debug("Lead score for " + leadData + " is " + leadScore);
        return new APIResponse(leadScore);
    }
}
