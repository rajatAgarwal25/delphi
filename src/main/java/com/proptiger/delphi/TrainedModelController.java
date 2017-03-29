package com.proptiger.delphi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proptiger.delphi.service.ModelTrainService;

@Controller
@RequestMapping(value = "/trainedModels/")
public class TrainedModelController {

    private static Logger     logger = LoggerFactory.getLogger(TrainedModelController.class);

    @Autowired
    private ModelTrainService modelTrainService;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    // TODO change to post
    @ResponseBody
    public String trainModel() {
        modelTrainService.trainModel();
        return "hello-world";
    }
}
