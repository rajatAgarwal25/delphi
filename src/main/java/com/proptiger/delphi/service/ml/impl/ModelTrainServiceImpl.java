package com.proptiger.delphi.service.ml.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proptiger.delphi.dto.LeadScoreDTO;
import com.proptiger.delphi.model.lead.LabeledPointFactory;
import com.proptiger.delphi.model.lead.LeadData;
import com.proptiger.delphi.model.lead.LeadScore;
import com.proptiger.delphi.service.LeadService;
import com.proptiger.delphi.service.ModelService;
import com.proptiger.delphi.service.SerializationService;
import com.proptiger.delphi.service.impl.Pair;

@Service
public class ModelTrainServiceImpl implements ModelService {

    private static Logger          LOGGER = LoggerFactory.getLogger(ModelTrainServiceImpl.class);

    @Autowired
    private SparkSession           sparkSession;

    @Autowired
    private JavaSparkContext       jsc;

    @Autowired
    private LeadService            leadService;

    @Autowired
    private DecisionTreeRegression decisionTreeRegression;

    @Autowired
    private SerializationService   serializationService;

    @Override
    public void trainModel() {
        try {
            LOGGER.debug("Loading data..");
            Pair<Map<LabeledPoint, Integer>, List<LeadData>> pair = leadService.getLeads();
            Map<LabeledPoint, Integer> leadsMap = pair.getFirst();

            JavaRDD<LabeledPoint> data = jsc.parallelize(new ArrayList<>(leadsMap.keySet()));

            LOGGER.debug("Training data..");
            Map<Integer, Double> leadIdScoreMap = decisionTreeRegression.trainModel(
                    sparkSession.sparkContext(),
                    data,
                    leadsMap);
            LOGGER.debug("Testing models");
            ModelValidator.printStats(createLeadScores(pair.getSecond(), leadIdScoreMap));
        }
        finally {
        }
    }

    private static List<LeadScore> createLeadScores(List<LeadData> leadData, Map<Integer, Double> leadIdScoreMap) {
        // System.out.println(leadData);
        List<LeadScore> leadScores = new ArrayList<>();
        leadData.forEach(ld -> {
            if (leadIdScoreMap.get(ld.getLeadId()) != null) {
                LeadScore ls = new LeadScore();
                ls.setLeadData(ld);
                ls.setScore(leadIdScoreMap.get(ld.getLeadId()));
                leadScores.add(ls);
            }

        });

        // System.out.println(leadScores);
        return leadScores;
    }

    @Override
    public LeadScoreDTO getLeadScore(LeadData leadData) {
        DecisionTreeModel model = serializationService.getModel(null);
        double predict = model.predict(LabeledPointFactory.newInstance(leadData).features());
        LeadScoreDTO leadScoreDTO = new LeadScoreDTO();
        leadScoreDTO.setScore(predict);
        return leadScoreDTO;
    }

}
