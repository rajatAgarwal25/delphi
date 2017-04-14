package com.proptiger.delphi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proptiger.delphi.service.ModelService;

@Controller
@RequestMapping(value = "/trainedModels/")
public class TrainedModelController {

    private static Logger     LOGGER = LoggerFactory.getLogger(TrainedModelController.class);

    @Autowired
    private ModelService modelTrainService;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    // TODO change to post
    @ResponseBody
    public String trainModel() {
        LOGGER.debug("Starting training");
        modelTrainService.trainModel();
        return "hello-world";
    }
}
