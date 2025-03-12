package com.overridetech.funpay_monitor.service;

import com.overridetech.funpay_monitor.dto.FilterArgDto;
import com.overridetech.funpay_monitor.entity.BaseFilterArgTable;
import com.overridetech.funpay_monitor.mapper.FilterArgMapper;
import com.overridetech.funpay_monitor.repository.FilterArgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilterManageService {

    private final FilterArgRepository filterArgRepository;
    private final FilterArgMapper filterArgMapper;

    public FilterArgDto getfilterArg(String category) {
        return filterArgMapper.mapEntityToDto(filterArgRepository.findByCategory(category).orElseThrow());


    }

    public List<String> getAvaliableCategories() {
        return filterArgRepository.getCategoryList();
    }


    @Transactional
    public FilterArgDto updateFilter(FilterArgDto filterArgDto, String category) {
        BaseFilterArgTable table = filterArgRepository.findByCategory(category).orElseThrow();
        filterArgMapper.updateEntity(table, filterArgDto);
        filterArgRepository.save(table);
        return filterArgMapper.mapEntityToDto(filterArgRepository.findByCategory(category).orElseThrow());
    }
}
