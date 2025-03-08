package com.overridetech.funpay_monitor.repository.criteria;

import com.overridetech.funpay_monitor.dto.FilterArgDto;
import com.overridetech.funpay_monitor.entity.BaseFilterArgTable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import org.openqa.selenium.InvalidArgumentException;
import org.springframework.stereotype.Repository;


@Repository
public class CriteriaFilterArgRepositoryImpl implements CriteriaFilterArgRepository {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void updateNonNullField(FilterArgDto filterArgDto, String category) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<BaseFilterArgTable> updateQuery = criteriaBuilder.createCriteriaUpdate(BaseFilterArgTable.class);
        Root<BaseFilterArgTable> root = updateQuery.from(BaseFilterArgTable.class);

        if (category == null) {
            throw new InvalidArgumentException("category is null");
        }

        if (filterArgDto.getMinStock() != null) {
            updateQuery.set(root.get("minStock"), filterArgDto.getMinStock());
        }
        if (filterArgDto.getMaxStock() != null) {
            updateQuery.set(root.get("maxStock"), filterArgDto.getMaxStock());
        }
        if (filterArgDto.getMinPrice() != null) {
            updateQuery.set(root.get("minPrice"), filterArgDto.getMinPrice());
        }
        if (filterArgDto.getMaxPrice() != null) {
            updateQuery.set(root.get("maxPrice"), filterArgDto.getMaxPrice());
        }
        if (filterArgDto.getCheckForOnline() != null) {
            updateQuery.set(root.get("checkForOnline"), filterArgDto.getCheckForOnline());
        }

        updateQuery.where(criteriaBuilder.like(root.get("category").as(String.class), category));

        entityManager.createQuery(updateQuery).executeUpdate();
    }


}
