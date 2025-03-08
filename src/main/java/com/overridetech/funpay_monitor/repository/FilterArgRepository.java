package com.overridetech.funpay_monitor.repository;


import com.overridetech.funpay_monitor.entity.BaseFilterArgTable;
import com.overridetech.funpay_monitor.repository.criteria.CriteriaFilterArgRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilterArgRepository extends JpaRepository<BaseFilterArgTable, Long>, CriteriaFilterArgRepository {

    @Query("select b from BaseFilterArgTable b where b.category = ?1")
    Optional<BaseFilterArgTable> findByCategoty(String categoty);


    @Query("select filter.category from BaseFilterArgTable filter")
    List<String> getCategoryList();
}
