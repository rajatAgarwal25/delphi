package com.proptiger.delphi.model.master;

import org.apache.spark.sql.Row;

public class ReasonStatusMappingModelFactory {

    public static ReasonStatusMappingModel newInstance(Row row) {
        ReasonStatusMappingModel model = new ReasonStatusMappingModel();

        model.setId(row.getAs("id"));
        model.setIsPresalesComment(row.getAs("is_presales_status"));
        model.setIsSalesComment(row.getAs("is_sales_status"));
        model.setIsHomeloanComment(row.getAs("is_homeloan_status"));
        model.setCommentId(row.getAs("commentId"));
        model.setComment(row.getAs("reason"));

        return model;
    }

}
