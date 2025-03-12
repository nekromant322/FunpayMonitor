package com.overridetech.funpay_monitor.repository;

import com.overridetech.funpay_monitor.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    List<Category> findByNameIn(List<String> names);
}
