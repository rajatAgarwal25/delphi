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
// $example off$
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;

import scala.Tuple2;

public class JavaBinaryClassificationMetricsExample {

    private static Integer      count       = 0;
    private static final String MODEL_FNAME = "target/tmp/LogisticRegressionModel";

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Java Binary Classification Metrics Example").setMaster("local");
        SparkContext sc = new SparkContext(conf);
        // $example on$
        String path = "/Users/harshit/git/spark/data/mllib/sample_binary_classification_data.txt";
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sc, path).toJavaRDD();

        // Split initial RDD into two... [60% training data, 40% testing data].
        trainModel(sc, data, null);
        LogisticRegressionModel load = LogisticRegressionModel.load(sc, "target/tmp/LogisticRegressionModel");
        // $example off$

        sc.stop();
    }

    public static Map<Integer, Double> trainModel(
            SparkContext sc,
            JavaRDD<LabeledPoint> data,
            Map<LabeledPoint, Integer> idMap) {
        Map<Integer, Double> leadIdScoreMap = new HashMap<>();
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[] { 0.75, 0.25 }, 11L);
        JavaRDD<LabeledPoint> training = splits[0].cache();
        JavaRDD<LabeledPoint> test = splits[1];

        // Run training algorithm to build the model.
        final LogisticRegressionModel model = new LogisticRegressionWithLBFGS().setNumClasses(2).run(training.rdd());

        // Clear the prediction threshold so the model will return probabilities
        model.clearThreshold();

        // Compute raw scores on the test set.
        JavaRDD<Tuple2<Integer, Double>> leadIdScores = test.map(new Function<LabeledPoint, Tuple2<Integer, Double>>() {
            @Override
            public Tuple2<Integer, Double> call(LabeledPoint p) {
                Double prediction = model.predict(p.features()) * 100;
                System.out.println("Key " + idMap.get(p) + " value " + prediction);
                // System.out.println(" -- " + prediction);
                if (idMap != null && prediction > 1) {
                    System.out.println("Prob for id : " + idMap.get(p) + " is " + prediction);
                    incrementCount();
                }

                return new Tuple2<Integer, Double>(idMap.get(p), prediction);
            }
        });

        List<Tuple2<Integer, Double>> collectedList = leadIdScores.collect();
        collectedList.forEach(tuple -> {
            leadIdScoreMap.put(tuple._1, tuple._2);
        });

        System.out.println("Count of cases when prob > 1 is " + count);

        // Get evaluation metrics.
        // BinaryClassificationMetrics metrics = new
        // BinaryClassificationMetrics(predictionAndLabels.rdd());

        // Precision by threshold
        // JavaRDD<Tuple2<Object, Object>> precision =
        // metrics.precisionByThreshold().toJavaRDD();
        // System.out.println("Precision by threshold: " + precision.collect());
        //
        // // Recall by threshold
        // JavaRDD<Tuple2<Object, Object>> recall =
        // metrics.recallByThreshold().toJavaRDD();
        // System.out.println("Recall by threshold: " + recall.collect());
        //
        // // F Score by threshold
        // JavaRDD<Tuple2<Object, Object>> f1Score =
        // metrics.fMeasureByThreshold().toJavaRDD();
        // System.out.println("F1 Score by threshold: " + f1Score.collect());
        //
        // JavaRDD<Tuple2<Object, Object>> f2Score =
        // metrics.fMeasureByThreshold(2.0).toJavaRDD();
        // System.out.println("F2 Score by threshold: " + f2Score.collect());
        //
        // // Precision-recall curve
        // JavaRDD<Tuple2<Object, Object>> prc = metrics.pr().toJavaRDD();
        // System.out.println("Precision-recall curve: " + prc.collect());

        // Thresholds
        // JavaRDD<Double> thresholds = precision.map(new
        // Function<Tuple2<Object, Object>, Double>() {
        // @Override
        // public Double call(Tuple2<Object, Object> t) {
        // return new Double(t._1().toString());
        // }
        // });
        //
        // // ROC Curve
        // JavaRDD<Tuple2<Object, Object>> roc = metrics.roc().toJavaRDD();
        // System.out.println("ROC curve: " + roc.collect());
        //
        // // AUPRC
        // System.out.println("Area under precision-recall curve = " +
        // metrics.areaUnderPR());
        //
        // // AUROC
        // System.out.println("Area under ROC = " + metrics.areaUnderROC());

        // Save and load model
        File f = new File(MODEL_FNAME);
        if (f.exists()) {
            deleteDir(f);
        }
        model.save(sc, MODEL_FNAME);
        return leadIdScoreMap;
    }

    private static void incrementCount() {
        count++;
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
