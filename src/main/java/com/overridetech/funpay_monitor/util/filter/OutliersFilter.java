package com.overridetech.funpay_monitor.util.filter;

import com.overridetech.funpay_monitor.dto.FunPayPoe2Offer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
public class OutliersFilter {

    public List<FunPayPoe2Offer> trimDataSet(List<FunPayPoe2Offer> dtoDataSet,
                                             BigDecimal lowPriceDelta,
                                             BigDecimal highPriceDelta,
                                             BigDecimal lowStockDelta,
                                             BigDecimal highStockDelta) {
        Stream<FunPayPoe2Offer> stream = dtoDataSet.stream();

        if (lowPriceDelta != null) {

            List<BigDecimal> priceDataSet = dtoDataSet.stream()
                    .map(FunPayPoe2Offer::getPrice)
                    .map(BigDecimal::new)
                    .toList();
            IQR priceIQR = getIQR(priceDataSet, lowPriceDelta, highPriceDelta);
            Predicate<BigDecimal> predicate = getFilter(priceIQR);

            stream = stream.filter(funPayPoe2Offer -> {
                BigDecimal price = new BigDecimal(funPayPoe2Offer.getPrice());
                return predicate.test(price);
            });
        }
        if (lowStockDelta != null) {

            List<BigDecimal> stockDataSet = dtoDataSet.stream()
                    .map(FunPayPoe2Offer::getStock)
                    .map(BigDecimal::new)
                    .toList();
            IQR stockIQR = getIQR(stockDataSet, lowStockDelta, highStockDelta);
            Predicate<BigDecimal> predicate = getFilter(stockIQR);

            stream = stream.filter(funPayPoe2Offer -> {
                BigDecimal stock = new BigDecimal(funPayPoe2Offer.getStock());
                return predicate.test(stock);
            });
        }

        return stream.toList();
    }


    record IQR(BigDecimal q1, BigDecimal q3, BigDecimal iqr, BigDecimal lowDeltaIQR, BigDecimal higDeltaIQR) {
    }

    private IQR getIQR(List<BigDecimal> unsortedDataSet, BigDecimal lowDeltaIQR, BigDecimal higDeltaIQR) {
        List<BigDecimal> sortedDataSet = unsortedDataSet.stream().sorted().toList();

        int indexQ1 = (sortedDataSet.size() / 4);
        int indexQ3 = (sortedDataSet.size() / 4 * 3);

        BigDecimal q1 = sortedDataSet.get(indexQ1);
        BigDecimal q3 = sortedDataSet.get(indexQ3);
        BigDecimal iqr = q3.subtract(q1);
        return new IQR(q1, q3, iqr, lowDeltaIQR, higDeltaIQR);
    }

    private Predicate<BigDecimal> getFilter(IQR iqr) {
        BigDecimal lowDelta = iqr.lowDeltaIQR();
        BigDecimal highDelta = iqr.higDeltaIQR();

        if (lowDelta != null && highDelta != null) {
            return (n) ->
                    n.compareTo(iqr.q1().subtract(lowDelta.multiply(iqr.iqr().multiply(BigDecimal.valueOf(0.1))))) >= 0
                            && n.compareTo(iqr.q3().add(highDelta.multiply(iqr.iqr()))) <= 0;
        }
        return n -> true;
    }


}
