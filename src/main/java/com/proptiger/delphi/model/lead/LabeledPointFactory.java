package com.proptiger.delphi.model.lead;

import java.util.HashMap;

import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

public class LabeledPointFactory {

    public static LabeledPoint newInstance(LeadData leadData) {
        LabeledPoint pos = new LabeledPoint(getY(leadData), Vectors.dense(
                leadData.getClientTypeId(),
                leadData.getCityId(),
                leadData.getSaleTypeId(),
                leadData.getTimeFrameId(),
                leadData.getIsStar() ? 1 : 0,
                leadData.getUploadTypeId(),
                leadData.getCountryId() == 1 ? 0 : 1,
                Boolean.TRUE.equals(leadData.getConfCallVerified()) ? 0 : 1,
                Math.sqrt(leadData.getBudget()),
                leadData.getAdditionalEnquiryCount(),
                Math.sqrt(leadData.getBedrooms()),
                leadData.getEnquiryLevelProjectCount(),
                leadData.getNumCallAtPresales(),
                Math.log(leadData.getLeadId())));

        return pos;

    }

    public static HashMap<Integer, Integer> getCategorialData() {

        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, 3); // client Types
        // map.put(1, 10);// Cities
        map.put(2, 3);// sale types
        map.put(3, 6); // time frame ids
        map.put(4, 2); // star
        map.put(5, 2); // upload types
        map.put(6, 2); // nri
        map.put(7, 2); // conf call verified

        return map;

    }

    private static double getY(LeadData leadData) {
        if (leadData.getClosed()) {
            return 1;
        }
        else if (leadData.getStatusId() == 8) {
            return -0.05;
        }
        // else if (leadData.getSvDone() && leadData.getMeetingDone()) {
        // return 0.2;
        // }
        else if (leadData.getSvDone()) {
            return 0.05;
        }
        // else if (leadData.getMeetingDone()) {
        // return 0.1;
        // }
        return 0;

    }

}
