package com.proptiger.delphi.service;

import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.springframework.data.mongodb.core.query.Query;

import com.proptiger.delphi.model.lead.LeadDataContainer;

public interface SerializationService {

    String serialize(LeadDataContainer leadDataContainer);

    LeadDataContainer getLeadDataContainer(Query query);

    String serialize(DecisionTreeModel decisionTreeModel);

    DecisionTreeModel getModel(Query query);

}
