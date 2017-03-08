package com.proptiger.oracle.model.lead;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.spark.sql.Row;

import com.proptiger.oracle.model.master.ReasonHelper;
import com.proptiger.oracle.model.master.ReasonStatusMappingModel;

public class LeadDataFactory {

    public static LeadData newInstance(Row row, Map<Integer, ReasonStatusMappingModel> rsmMap) {

        Integer leadId = nullSafeInteger(row.getAs("id"));
        Boolean clientType = row.getAs("client_type_id");
        Integer clientTypeId = clientType == null ? 0 : clientType.booleanValue() ? 1 : 0;

        Integer cityId = nullSafeInteger(row.getAs("city_id"));
        Integer saleTypeId = nullSafeInteger(row.getAs("sale_type_id"));
        Integer timeFrameId = nullSafeInteger(row.getAs("time_frame_id"));

        String starSourcesList = row.getAs("star");
        Boolean isStar = starSourcesList != null ? starSourcesList.contains("1") : false;

        Integer budget = nullSafeInteger(row.getAs("max_budget"));

        String eIdList = row.getAs("enquiryIds");
        Integer additionalEnquiryCount = eIdList != null ? eIdList.split(",").length - 1 : 0;

        Integer statusId = nullSafeInteger(row.getAs("status_id"));

        String rsmIds = row.getAs("rsmIds");
        String uploadTypes = row.getAs("upload_types");
        String bedrooms = row.getAs("bedrooms");

        Integer countryId = nullSafeInteger(row.getAs("country_id"));

        LeadData leadData = new LeadData();
        setUploadType(leadData, uploadTypes);
        leadData.setLeadId(leadId);
        leadData.setClientTypeId(clientTypeId);
        leadData.setCityId(cityId);
        leadData.setSaleTypeId(saleTypeId);
        leadData.setTimeFrameId(timeFrameId);
        leadData.setIsStar(isStar);
        leadData.setBudget(budget);
        leadData.setAdditionalEnquiryCount(additionalEnquiryCount);
        leadData.setStatusId(statusId);
        leadData.setClosed(statusId.equals(9) || statusId.equals(32) || statusId.equals(39));
        leadData.setCountryId(countryId);

        setProjectLevelEnquiryCount(leadData, row.getAs("enquiry_projects"));
        populateDataFromLeadHistory(leadData, rsmIds, rsmMap);
        setBedrooms(leadData, bedrooms);

        return leadData;
    }

    private static Integer nullSafeInteger(Integer integer) {
        return integer == null ? 0 : integer;
    }

    private static void setUploadType(LeadData leadData, String uploadTypes) {
        if (uploadTypes == null || uploadTypes.trim().isEmpty()) {
            return;
        }
        leadData.setUploadTypeId(0);
        String[] arr = uploadTypes.split(",");
        for (String uploadTypeString : arr) {
            Integer uploadType = Integer.valueOf(uploadTypeString);
            if (uploadType == 3) {
                leadData.setUploadTypeId(1);
                return;
            }
        }
    }

    private static void setProjectLevelEnquiryCount(LeadData leadData, String enquiryProjects) {
        if (enquiryProjects == null || enquiryProjects.trim().isEmpty()) {
            leadData.setEnquiryLevelProjectCount(0);
            return;
        }
        String[] arr = enquiryProjects.split(",");
        Set<Integer> projectSet = new HashSet<>();
        for (String enquiryPRString : arr) {
            Integer project = Integer.valueOf(enquiryPRString);
            projectSet.add(project);
        }
        leadData.setEnquiryLevelProjectCount(projectSet.size());
    }

    private static void setBedrooms(LeadData leadData, String bedrooms) {
        if (bedrooms == null || bedrooms.trim().isEmpty()) {
            leadData.setBedrooms(0);
            return;
        }
        String[] arr = bedrooms.split(",");
        Integer maxBedroom = 0;
        for (String bedroomPRString : arr) {
            Integer br = Integer.valueOf(bedroomPRString);
            maxBedroom = Math.max(br, maxBedroom);
        }
        leadData.setBedrooms(maxBedroom);
    }

    private static void populateDataFromLeadHistory(
            LeadData leadData,
            String rsmIds,
            Map<Integer, ReasonStatusMappingModel> rsmMap) {
        if (rsmIds == null || rsmIds.trim().isEmpty()) {
            return;
        }
        Set<Integer> commentIdSet = new HashSet<>();
        String[] rmsIdArray = rsmIds.split(",");
        int countPresalesComment = 0;

        for (String rsmString : rmsIdArray) {
            ReasonStatusMappingModel model = rsmMap.get(Integer.valueOf(rsmString));
            if (model == null) {
                System.out.println("No rsm found for id " + Integer.valueOf(rsmString));
                continue;
            }
            commentIdSet.add(model.getCommentId());
            if (model.getIsPresalesComment()) {
                countPresalesComment++;
            }
        }
        leadData.setIsPresalesVerified(ReasonHelper.isPresalesVerified(commentIdSet));
        leadData.setConfCallVerified(ReasonHelper.isConfCallVerified(commentIdSet));
        leadData.setMeetingDone(ReasonHelper.isMeetingDone(commentIdSet));
        leadData.setSvDone(ReasonHelper.isSiteVisitDone(commentIdSet));
        leadData.setNumCallAtPresales(countPresalesComment);

    }
}
