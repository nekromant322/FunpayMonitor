package com.overridetech.funpay_monitor.controller;


import com.overridetech.funpay_monitor.dto.FilterArgDto;
import com.overridetech.funpay_monitor.service.FilterManageService;
import com.overridetech.funpay_monitor.service.FunPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPageController {

    private final FilterManageService filterManageService;

    @GetMapping
    public String adminPage(
            @RequestParam(name = "category", required = false) String category,
            Model model) {

        FilterArgDto filterArgDto = new FilterArgDto();
        if (category != null) {
            filterArgDto = filterManageService.getfilterArg(category);
        }
        model.addAttribute("filterArgDto", filterArgDto);
        List<String> categories = filterManageService.getAvaliableCategories();
        model.addAttribute("categories", categories);
        return "admin-page";
    }


    @PatchMapping("/apply-filters")
    public String updateFilter(@RequestParam("category") String category,
                               @ModelAttribute FilterArgDto  filterArgs,
                               Model model) {
        FilterArgDto filterArgDto = filterManageService.updateFilter(filterArgs, category);
        model.addAttribute(filterArgDto);
        return "admin-page";
    }
}
