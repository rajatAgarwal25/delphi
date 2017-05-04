package com.proptiger.delphi.service.ml.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proptiger.delphi.model.lead.LeadScore;

public class ModelValidator {

    private static Logger LOGGER = LoggerFactory.getLogger(ModelValidator.class);

    public static void printStats(List<LeadScore> scores) {
        Collections.sort(scores);
        for (DistributionConfig distributionConfig : DistributionConfig.KNOWN_CONFIGS) {
            printStats(scores, distributionConfig);
        }
    }

    /**
     * Assuming sorted scores
     * 
     * @param scores
     * @param distributionConfig
     */
    private static void printStats(List<LeadScore> scores, DistributionConfig distributionConfig) {
        System.out.println("Count of scores being validated is " + scores.size());
        int totalClosedWon = getClosedWonLeads(scores).size();
        int totalClosedLost = getClosedLostLeads(scores).size();
        int init = 0;
        int index = 0;
        System.out.println("Getting data for config " + distributionConfig);
        System.out.println("Total closed won " + totalClosedWon);
        for (Integer partition : distributionConfig.getConfigs()) {
            int itemsCount = (partition * scores.size()) / 100;
            int partitionClosedWon = getClosedWonLeads(scores.subList(init, init + itemsCount)).size();
            init += itemsCount;
            System.out.println(index + " Closed won from partition is "
                    + partitionClosedWon
                    + " , while partition size is "
                    + itemsCount);
            if (totalClosedWon != 0) {
                System.out.println("For " + index

                + " partition, closed won ratio is " + (partitionClosedWon * 100) / totalClosedWon);
            }
            // System.out.println(index + " Closed lost from partition is "
            // + partitionClosedLost
            // + " , while partition size is "
            // + itemsCount);
            // if (totalClosedWon != 0) {
            // System.out.println("For " + index
            //
            // + " partition, closed lost ratio is " + (partitionClosedLost *
            // 100) / totalClosedLost);
            // }
            index++;
        }

    }

    private static List<LeadScore> getClosedWonLeads(List<LeadScore> scores) {
        System.out.println("Score ranges from " + scores.get(0).getScore()
                + " to "
                + scores.get(scores.size() - 1).getScore());
        List<LeadScore> closedWonScores = scores.stream().filter(l -> l.getLeadData().getClosed())
                .collect(Collectors.toList());
        // for (LeadScore cs : closedWonScores) {
        // try {
        // System.out.println("Score is " + cs.getScore());
        // System.out.println(new
        // ObjectMapper().writeValueAsString(cs.getLeadData()));
        // }
        // catch (JsonProcessingException e) {
        // System.out.println("Exception occured while converting to json.");
        // }
        // }

        return closedWonScores;
    }

    private static List<LeadScore> getClosedLostLeads(List<LeadScore> scores) {
        return scores.stream().filter(l -> l.getLeadData().getMeetingDone()).collect(Collectors.toList());
    }

}
