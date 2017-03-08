package com.proptiger.oracle.model.master;

import java.io.Serializable;
import java.util.ArrayList;

public class ReasonStatusMappingContainer implements Serializable {

    /**
     * 
     */
    private static final long                   serialVersionUID = -935768545278064086L;

    private ArrayList<ReasonStatusMappingModel> modelList        = new ArrayList<ReasonStatusMappingModel>();

    public ArrayList<ReasonStatusMappingModel> getModelList() {
        return modelList;
    }

}
