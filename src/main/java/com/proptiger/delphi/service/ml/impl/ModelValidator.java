package com.proptiger.delphi.service.ml.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.proptiger.delphi.model.lead.LeadScore;

public class ModelValidator {

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
            int partitionClosedLost = getClosedLostLeads(scores.subList(init, init + itemsCount)).size();
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
        return scores.stream().filter(l -> l.getLeadData().getClosed()).collect(Collectors.toList());
    }

    private static List<LeadScore> getClosedLostLeads(List<LeadScore> scores) {
        return scores.stream().filter(l -> l.getLeadData().getMeetingDone()).collect(Collectors.toList());
    }

}
