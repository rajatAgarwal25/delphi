package com.proptiger.delphi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proptiger.delphi.dto.APIResponse;
import com.proptiger.delphi.service.ModelService;

@Controller
@RequestMapping(value = "/")
@Api(value = "TrainedModelController")
public class TrainedModelController {

    private static Logger LOGGER = LoggerFactory.getLogger(TrainedModelController.class);

    @Autowired
    private ModelService  modelTrainService;

    @ApiOperation(
            value = "API endpoint to start training of ml models",
            notes = "API endpoint to start training of ml models")
    @RequestMapping(value = "train", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse trainModel() {
        LOGGER.debug("Starting training");
        modelTrainService.trainModel();
        return new APIResponse();
    }
}
