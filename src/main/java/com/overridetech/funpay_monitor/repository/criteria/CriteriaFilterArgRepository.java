package com.overridetech.funpay_monitor.repository.criteria;


import com.overridetech.funpay_monitor.dto.FilterArgDto;

public interface CriteriaFilterArgRepository {

    void updateNonNullField(FilterArgDto filterArgDto, String category);

}
