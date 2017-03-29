/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.proptiger.delphi.service.ml.impl;

// $example on$
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.util.MLUtils;
// $example off$


import scala.Tuple2;

import com.proptiger.delphi.model.lead.LabeledPointFactory;

class JavaDecisionTreeRegressionExample {

    private static final String MODEL_FNAME = "target/tmp/myDecisionTreeRegressionModel";

    public static void main(String[] args) {

        // $example on$
        SparkConf sparkConf = new SparkConf().setAppName("JavaDecisionTreeRegressionExample");
        SparkContext jsc = new SparkContext(sparkConf);

        // Load and parse the data file.
        String datapath = "data/mllib/sample_libsvm_data.txt";
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(jsc, datapath).toJavaRDD();
        // Split the data into training and test sets (30% held out for testing)
        trainModel(jsc, data, null);
        // $example off$
    }

    static Map<Integer, Double> trainModel(
            SparkContext jsc,
            JavaRDD<LabeledPoint> data,
            Map<LabeledPoint, Integer> idMap) {
        Map<Integer, Double> leadIdScoreMap = new HashMap<>();
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[] { 0.7, 0.3 });
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];
        System.out.println("Test Data size is " + testData.count());

        // Set parameters.
        // Empty categoricalFeaturesInfo indicates all features are continuous.
        Map<Integer, Integer> categoricalFeaturesInfo = LabeledPointFactory.getCategorialData();
        String impurity = "variance";
        Integer maxDepth = 7;
        Integer maxBins = 32;

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

        // Double testMSE = predictionAndLabel.map(new Function<Tuple2<Double,
        // Double>, Double>() {
        // @Override
        // public Double call(Tuple2<Double, Double> pl) {
        // Double diff = pl._1() - pl._2();
        // return diff * diff;
        // }
        // }).reduce(new Function2<Double, Double, Double>() {
        // @Override
        // public Double call(Double a, Double b) {
        // return a + b;
        // }
        // }) / data.count();
        // System.out.println("Test Mean Squared Error: " + testMSE);
        // System.out.println("Learned regression tree model:\n" +
        // model.toDebugString());

        // Save and load model
        // File f = new File(MODEL_FNAME);
        // if (f.exists()) {
        // deleteDir(f);
        // }
        // model.save(jsc, MODEL_FNAME);

        return leadIdScoreMap;
    }

    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }
}
