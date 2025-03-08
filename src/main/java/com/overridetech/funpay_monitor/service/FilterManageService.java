package com.overridetech.funpay_monitor.service;

import com.overridetech.funpay_monitor.dto.FilterArgDto;
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

    public FilterArgDto getfilterArg(String category) {
        return FilterArgMapper.mapEntityToDto(filterArgRepository.findByCategoty(category).orElseThrow());


    }

    public List<String> getAvaliableCategories() {
        return filterArgRepository.getCategoryList();
    }


    @Transactional
    public FilterArgDto updateFilter(FilterArgDto filterArgDto, String category) {
        filterArgRepository.updateNonNullField(filterArgDto, category);
        return FilterArgMapper.mapEntityToDto(filterArgRepository.findByCategoty(category).orElseThrow());
    }
}
