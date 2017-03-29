package com.proptiger.delphi.service.ml.impl;

import java.util.Map;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.regression.LabeledPoint;

interface Regression {

    Map<Integer, Double> trainModel(SparkContext jsc, JavaRDD<LabeledPoint> data, Map<LabeledPoint, Integer> idMap);

}