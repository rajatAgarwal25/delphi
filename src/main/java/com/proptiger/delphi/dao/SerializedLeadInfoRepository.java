package com.proptiger.delphi.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.proptiger.delphi.model.meta.SerializedLeadInfo;

public interface SerializedLeadInfoRepository extends MongoRepository<SerializedLeadInfo, String> {

}
