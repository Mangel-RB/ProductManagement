/*
 * Copyright (c) 2026.
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package labs.pm.app;

import labs.pm.data.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * {@code Shop} class represents an application that manages products
 * @version 4.0
 * @author Oracle
 */

public class Shop {

    public static void main(String[] args) {

        ProductManager pm = new ProductManager("es_ES");

        Product p1 = pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED, LocalDate.now());

//        Product p2 = pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
//        Product p3 = pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
//        Product p4 = pm.createProduct(105, "Cookie", BigDecimal.valueOf(3.99), Rating.TWO_STAR, LocalDate.now());
//        Product p6 = pm.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
//        Product p7 = pm.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
//
//        Product p1 = new Drink(101, "Tea", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
//        Product p2 = new Drink(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
//        Product p3 = new Food(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
//        Product p4 = new Food(105, "Cookie", BigDecimal.valueOf(3.99), Rating.TWO_STAR, LocalDate.now());;
//        Product p6 = new Drink(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
//        Product p7 = new Food(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
//
//        Product p5 = p3.applyRating(Rating.THREE_STAR);
//        Product p8 = p4.applyRating(Rating.FIVE_STAR);
//        Product p9 = p1.applyRating(Rating.TWO_STAR);

//        List<Product> listOfProducts = List.of(p1, p2, p3, p4, p5, p6, p7, p8, p9);

//        for(Product p: listOfProducts){
//
//            System.out.println(p);

//            System.out.println(p.getId() + " - " + p.getName() + " - "
//                    + p.getPrice() + " - " + p.getDiscount() + " - "
//                    + p.getRating().getStars());
        //}
//
//        System.out.println(p6.equals(p7));
//
//        if(p3 instanceof Food f) { // Pattern Matching
//            System.out.println(f.getBestBefore());
//        }
//
//        System.out.println(p1.getBestBefore() + " " + p3.getBestBefore());

        /*if(p3 instanceof Food) {
            System.out.println(((Food) p3).getBestBefore());
        }*/

        pm.printProductReport(p1);

        p1 = pm.reviewProduct(p1, Rating.FOUR_STAR, "Muy weno.");
        p1 = pm.reviewProduct(p1, Rating.TWO_STAR, "Sabía a cartón.");
        p1 = pm.reviewProduct(p1, Rating.THREE_STAR, "Sin más.");
        p1 = pm.reviewProduct(p1, Rating.FOUR_STAR, "Qué delisia.");
        p1 = pm.reviewProduct(p1, Rating.FIVE_STAR, "El mejor café de mi vida.");
        p1 = pm.reviewProduct(p1, Rating.FIVE_STAR, "Weniiisimo.");

        Product p2 = pm.createProduct(102, "Coffee",
                BigDecimal.valueOf(1.99), Rating.NOT_RATED);
        p2 = pm.reviewProduct(p2, Rating.THREE_STAR, "Coffee was ok");
        p2 = pm.reviewProduct(p2, Rating.ONE_STAR, "Where is the milk?!");
        p2 = pm.reviewProduct(p2, Rating.FIVE_STAR,
                "It's perfect with ten spoons of sugar!");

        Product p3 = pm.createProduct(103, "Cake",
                BigDecimal.valueOf(3.99), Rating.NOT_RATED,
                LocalDate.now().plusDays(2));
        p3 = pm.reviewProduct(p3, Rating.FIVE_STAR, "Very nice cake");
        p3 = pm.reviewProduct(p3, Rating.FOUR_STAR,
                "Good, but I've expected more chocolate");
        p3 = pm.reviewProduct(p3, Rating.FIVE_STAR, "This cake is perfect!");

        Product p4 = pm.createProduct(104, "Cookie",
                BigDecimal.valueOf(2.99), Rating.NOT_RATED,
                LocalDate.now());
        p4 = pm.reviewProduct(p4, Rating.THREE_STAR, "Just another cookie");
        p4 = pm.reviewProduct(p4, Rating.THREE_STAR, "Ok");

        Product p5 = pm.createProduct(105, "Hot Chocolate",
                BigDecimal.valueOf(2.50), Rating.NOT_RATED);
        p5 = pm.reviewProduct(p5, Rating.FOUR_STAR, "Tasty!");
        p5 = pm.reviewProduct(p5, Rating.FOUR_STAR, "No bad at all");

        Product p6 = pm.createProduct(106, "Chocolate",
                BigDecimal.valueOf(2.50), Rating.NOT_RATED,
                LocalDate.now().plusDays(3));
        p6 = pm.reviewProduct(p6, Rating.TWO_STAR, "Too sweet");
        p6 = pm.reviewProduct(p6, Rating.THREE_STAR, "Better then cookie");
        p6 = pm.reviewProduct(p6, Rating.TWO_STAR, "Too bitter");
        p6 = pm.reviewProduct(p6, Rating.ONE_STAR, "I don't get it!");

        pm.printProductReport(p6);

//        for(Product p: pm.products.keySet()){
//            System.out.println("Reseñas de " + p);
//
//            for (Review r: pm.products.get(p)){
//                System.out.println("\t" + r);
//            }
//
//        }

        pm.printProductReport(p6);

    }
}
