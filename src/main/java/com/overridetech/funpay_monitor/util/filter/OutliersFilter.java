package com.overridetech.funpay_monitor.util.filter;

import com.overridetech.funpay_monitor.dto.FunPayPoe2Offer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
public class OutliersFilter {

    public List<FunPayPoe2Offer> trimDataSet(List<FunPayPoe2Offer> dtoDataSet, BigDecimal priceDelta, BigDecimal stockDelta) {
        Stream<FunPayPoe2Offer> stream = dtoDataSet.stream();

        if (priceDelta != null) {

            List<BigDecimal> priceDataSet = dtoDataSet.stream()
                    .map(FunPayPoe2Offer::getPrice)
                    .map(BigDecimal::new)
                    .toList();
            IQR priceIQR = getIQR(priceDataSet, priceDelta);
            Predicate<BigDecimal> predicate = getFilter(priceIQR);

            stream = stream.filter(funPayPoe2Offer -> {
                BigDecimal price = new BigDecimal(funPayPoe2Offer.getPrice());
                return predicate.test(price);
            });
        }
        if (stockDelta != null) {

            List<BigDecimal> stockDataSet = dtoDataSet.stream()
                    .map(FunPayPoe2Offer::getStock)
                    .map(BigDecimal::new)
                    .toList();
            IQR priceIQR = getIQR(stockDataSet, priceDelta);
            Predicate<BigDecimal> predicate = getFilter(priceIQR);

            stream = stream.filter(funPayPoe2Offer -> {
                BigDecimal stock = new BigDecimal(funPayPoe2Offer.getStock());
                return predicate.test(stock);
            });
        }

        return stream.toList();
    }


    record IQR(BigDecimal q1, BigDecimal q3, BigDecimal iqr, BigDecimal deltaIQR) {
    }

    private IQR getIQR(List<BigDecimal> unsortedDataSet, BigDecimal deltaIQR) {
        List<BigDecimal> sortedDataSet = unsortedDataSet.stream().sorted().toList();

        int indexQ1 = (sortedDataSet.size() / 4);
        int indexQ3 = (sortedDataSet.size() / 4 * 3);

        BigDecimal q1 = sortedDataSet.get(indexQ1);
        BigDecimal q3 = sortedDataSet.get(indexQ3);
        BigDecimal iqr = q3.subtract(q1);
        return new IQR(q1, q3, iqr, deltaIQR);
    }

    private Predicate<BigDecimal> getFilter(IQR iqr) {
        BigDecimal delta = iqr.deltaIQR();

        if (delta != null) {
            return (n) ->
                    n.compareTo(iqr.q1().subtract(delta.multiply(iqr.iqr().multiply(BigDecimal.valueOf(0.1))))) >= 0
                            && n.compareTo(iqr.q3().add(delta.multiply(iqr.iqr()))) <= 0;
        }
        return n -> true;
    }


}
