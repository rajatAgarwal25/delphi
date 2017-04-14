package com.proptiger.delphi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.proptiger.delphi.dto.LeadScoreDTO;
import com.proptiger.delphi.model.lead.LeadData;
import com.proptiger.delphi.service.ModelService;

public class LeadScoreController {

    private static Logger LOGGER = LoggerFactory.getLogger(LeadScoreController.class);

    @Autowired
    private ModelService  modelService;

    @RequestMapping(value = "leadScore", method = RequestMethod.GET)
    public LeadScoreDTO getLeadScore(LeadData leadData) {
        LOGGER.debug("Computing lead score for " + leadData);
        LeadScoreDTO leadScore = modelService.getLeadScore(leadData);
        LOGGER.debug("Lead score for " + leadData + " is " + leadScore);
        return leadScore;
    }
}
