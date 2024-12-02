package hcmute.edu.vn.watches_store_v2.mapper;

import hcmute.edu.vn.watches_store_v2.dto.order.response.Statistic;

import java.util.ArrayList;
import java.util.List;

public class StatisticMapper {

    public static Statistic mapStatistic() {
        return new Statistic(
                new Statistic.Status(),
                mapListPrice()
        );
    }

    public static List<Statistic.Price> mapListPrice() {

        List<Statistic.Price> priceList = new ArrayList<>();

        for (int i = 1; i < 13; i++) {
            Statistic.Price price = new Statistic.Price();
            price.setMonth(i);
            priceList.add(price);
        }

        return priceList;
    }
}
