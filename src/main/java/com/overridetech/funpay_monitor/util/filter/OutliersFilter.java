package com.overridetech.funpay_monitor.util.filter;

import com.overridetech.funpay_monitor.dto.Offer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
public class OutliersFilter<T extends Offer> {

    /**
     * Rejects offers with an abnormally high or low price
     * using Interquartile range algorithm, with the difference
     * that multipliers are added separately to determine the upper and lower limits
     * and truncate by stock's lower limit
     */
    public List<T> trimDataSet(List<T> dtoDataSet,
                               Double lowPriceDelta,
                               Double highPriceDelta,
                               Double minStock) {
        Stream<T> stream = dtoDataSet.stream();

        if (lowPriceDelta != null) {

            List<Double> priceDataSet = dtoDataSet.stream()
                    .map(Offer::getPrice)
                    .map(Double::parseDouble)
                    .toList();
            IQR priceIQR = getIQR(priceDataSet, lowPriceDelta, highPriceDelta);
            Predicate<Double> predicate = getFilter(priceIQR);

            stream = stream.filter(offer -> {
                Double price = Double.parseDouble(offer.getPrice());
                return predicate.test(price);
            });
        }
        if (minStock != null) {
            stream = stream.filter(offer -> {
                Double stock = Double.valueOf(offer.getStock());
                return stock >= minStock;
            });
        }

        return stream.toList();
    }


    record IQR(Double q1, Double q3, Double iqr, Double lowDeltaIQR, Double higDeltaIQR) {
    }

    private IQR getIQR(List<Double> unsortedDataSet, Double lowDeltaIQR, Double higDeltaIQR) {
        List<Double> sortedDataSet = unsortedDataSet.stream().sorted().toList();

        int indexQ1 = (sortedDataSet.size() / 4);
        int indexQ3 = (sortedDataSet.size() / 4 * 3);

        Double q1 = sortedDataSet.get(indexQ1);
        Double q3 = sortedDataSet.get(indexQ3);
        Double iqr = q3 - q1;
        return new IQR(q1, q3, iqr, lowDeltaIQR, higDeltaIQR);
    }

    private Predicate<Double> getFilter(IQR iqr) {
        Double lowDelta = iqr.lowDeltaIQR();
        Double highDelta = iqr.higDeltaIQR();

        if (lowDelta != null && highDelta != null) {
            return (n) ->
                    n >= (iqr.q1() - lowDelta * iqr.iqr() * 0.1)
                            && n <= (iqr.q3() + highDelta * iqr.iqr());
        }
        return n -> true;
    }


}
