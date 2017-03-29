package com.proptiger.delphi.service.ml.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.spark.sql.SparkSession;

import com.proptiger.delphi.model.lead.LeadData;
import com.proptiger.delphi.model.lead.LeadScore;

@SuppressWarnings("serial")
public class LeadTrainer implements Serializable {

    public static final SparkSession sparkSession = SparkSession.builder().master("local").appName("Spark2Train")
                                                          .getOrCreate();

    private static final boolean     loadLeadData = false;

    public static void main(String[] args) {
        // if (loadLeadData) {
        // LeadDataSerializer.main(null);
        // }
        //
        // JavaSparkContext jsc = new
        // JavaSparkContext(sparkSession.sparkContext());
        // try {
        //
        // System.out.println("Loading data..");
        // Pair<Map<LabeledPoint, Integer>, List<LeadData>> pair =
        // LeadDataService.getInstance().getLeads();
        // Map<LabeledPoint, Integer> leadsMap = pair.getFirst();
        //
        // JavaRDD<LabeledPoint> data = jsc.parallelize(new
        // ArrayList<>(leadsMap.keySet()));
        //
        // System.out.println("Training data..");
        // Map<Integer, Double> leadIdScoreMap =
        // JavaDecisionTreeRegressionExample.trainModel(
        // JavaSparkContext.toSparkContext(jsc),
        // data,
        // leadsMap);
        // System.out.println("Testing models");
        // ModelValidator.printStats(createLeadScores(pair.getSecond(),
        // leadIdScoreMap));
        // }
        // finally {
        // jsc.close();
        // }
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

}
