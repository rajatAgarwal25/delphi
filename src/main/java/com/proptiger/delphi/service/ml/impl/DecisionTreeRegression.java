package com.proptiger.delphi.service.ml.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.springframework.stereotype.Component;

import scala.Tuple2;

import com.proptiger.delphi.model.lead.LabeledPointFactory;

@Component
public class DecisionTreeRegression implements Regression {

    private static final String  impurity = "variance";
    private static final Integer maxDepth = 7;
    private static final Integer maxBins  = 32;

    /**
     * Returns
     */
    public Map<Integer, Double> trainModel(
            SparkContext jsc,
            JavaRDD<LabeledPoint> data,
            Map<LabeledPoint, Integer> idMap) {
        Map<Integer, Double> leadIdScoreMap = new HashMap<>();
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[] { 0.7, 0.3 });
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];
        System.out.println("Test Data size is " + testData.count());

        // Empty categoricalFeaturesInfo indicates all features are continuous.
        Map<Integer, Integer> categoricalFeaturesInfo = LabeledPointFactory.getCategorialData();

        // Train a DecisionTree model.
        final DecisionTreeModel model = DecisionTree.trainRegressor(
                trainingData,
                categoricalFeaturesInfo,
                impurity,
                maxDepth,
                maxBins);

        // Evaluate model on test instances and compute test error
        JavaPairRDD<Integer, Double> leadIdScores = testData
                .mapToPair(new PairFunction<LabeledPoint, Integer, Double>() {
                    @Override
                    public Tuple2<Integer, Double> call(LabeledPoint p) {
                        return new Tuple2<>(idMap.get(p), model.predict(p.features()));
                    }
                });

        List<Tuple2<Integer, Double>> collectedList = leadIdScores.collect();
        collectedList.forEach(tuple -> {
            leadIdScoreMap.put(tuple._1, tuple._2);
        });
        System.out.println("Size of lead score map on test data is " + leadIdScoreMap.size());

        // Save and load model
        // File f = new File(MODEL_FNAME);
        // if (f.exists()) {
        // deleteDir(f);
        // }
        // model.save(jsc, MODEL_FNAME);

        return leadIdScoreMap;
    }
}
