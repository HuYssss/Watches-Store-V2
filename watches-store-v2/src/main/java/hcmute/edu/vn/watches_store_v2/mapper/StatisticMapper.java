package hcmute.edu.vn.watches_store_v2.mapper;

import hcmute.edu.vn.watches_store_v2.dto.order.response.Statistic;
import hcmute.edu.vn.watches_store_v2.dto.order.response.StatisticAdmin;

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

    public static StatisticAdmin mapStatisticAmin() {
        return new StatisticAdmin(
                new StatisticAdmin.Status(),
                mapListPriceAdmin(),
                new ArrayList<>(),
                mapListGenderAdmin()
        );
    }

    public static List<StatisticAdmin.Price> mapListPriceAdmin() {

        List<StatisticAdmin.Price> priceList = new ArrayList<>();

        for (int i = 1; i < 13; i++) {
            StatisticAdmin.Price price = new StatisticAdmin.Price();
            price.setMonth(i);
            priceList.add(price);
        }

        return priceList;
    }

    public static List<StatisticAdmin.Gender> mapListGenderAdmin() {
        List<StatisticAdmin.Gender> genderList = new ArrayList<>();

        for (int i = 1; i < 13; i++) {
            StatisticAdmin.Gender male = new StatisticAdmin.Gender();
            male.setGender("Nam");
            male.setMonth(i);
            male.setQuantity(0);

            genderList.add(male);

            StatisticAdmin.Gender female = new StatisticAdmin.Gender();
            female.setGender("Nữ");
            female.setMonth(i);
            female.setQuantity(0);

            genderList.add(female);
        }

        return genderList;
    }
}
