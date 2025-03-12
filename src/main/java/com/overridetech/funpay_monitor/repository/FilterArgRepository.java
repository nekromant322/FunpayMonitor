package com.overridetech.funpay_monitor.repository;


import com.overridetech.funpay_monitor.entity.BaseFilterArgTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilterArgRepository extends JpaRepository<BaseFilterArgTable, Long> {

    Optional<BaseFilterArgTable> findByCategory(String category);


    @Query("select filter.category from BaseFilterArgTable filter")
    List<String> getCategoryList();
}
