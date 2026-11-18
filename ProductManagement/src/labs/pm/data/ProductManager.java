/*
 * Copyright (c) 2026.
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package labs.pm.data;

import com.sun.tools.javac.Main;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * @author manana
 **/

public class ProductManager {
    private Product product;
    private Review review;

    private Review[] reviews = new Review[5];
    private Map<Product, List<Review>> products = new HashMap<>();
    private static Map<String, ResourceFormatter> formatters =
            Map.of("en-GB", new ResourceFormatter(Locale.UK),
                    "en-US", new ResourceFormatter(Locale.US),
                    "es-ES", new ResourceFormatter(Locale.of("es", "ES")),
                    "fr-FR", new ResourceFormatter(Locale.FRANCE),
                    "zh-CN", new ResourceFormatter(Locale.CHINA));

    private ResourceFormatter formatter;

    public ProductManager(String languageTag){
        changeLocale(languageTag);
    }

    public ProductManager (Locale locale){
        this(locale.toLanguageTag());
    }

    public void changeLocale(String languageTag){
        formatter = formatters.getOrDefault(languageTag, formatters.get("es-ES"));
    }

    public static Set<String> getSupportedLocales(){
        return formatters.keySet();
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore){
        product = new Food(id, name, price, rating, bestBefore);

        //Crear listado de reseñas por producto
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating){
        product = new Drink(id, name, price, rating);

        //Crear listado de reseñas por producto
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product reviewProduct(Product product, Rating rating, String comments) {

        List<Review> reviews = products.get(product);
        products.remove(product, reviews);

        reviews.add(new Review(rating,comments));

        int sum= 0;
        for (Review review: reviews){
            sum += review.rating().ordinal();
        }

        this.product = product.applyRating(Rateable.convert(Math.round(sum/reviews.size())));

        products.put(product, reviews);

        return this.product;
    }

    public Product reviewProduct ( int id, Rating rating, String comments){
        return reviewProduct(findProduct(id), rating, comments);
    }

    public void printProductReport(Product product){

        List<Review> reviews = products.get(product);


        StringBuilder txt = new StringBuilder();


        txt.append(formatter.formatProduct(product));
        txt.append('\n');

        for (Review review : reviews) {
            if (review == null){
                break;
            }

            txt.append(formatter.formatReview(review));
            txt.append('\n');

            if (reviews.isEmpty()){
                txt.append(formatter.getText("no.reviews"));
                txt.append('\n');
            }

        }

        txt.append('\n');

        Collections.sort(reviews);

        System.out.println(txt);
    }

    public void printProductReport(int id){
        printProductReport(findProduct(id));
    }

    public Product findProduct (int id){
        Product result = null;

        for (Product product : products.keySet()) {
            if (product.getId() == id) {
                result = product;
                break;
            }
        }
        return result;
    }

    // Clase estática anidada

    private static  class ResourceFormatter{
        private Locale locale;
        private ResourceBundle resources;
        private DateTimeFormatter dateFomat;
        private NumberFormat moneyFormat;

        // Lógica previamente en el constructor de ProductManager
        private ResourceFormatter(Locale locale){
            this.locale = locale;
            resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
            dateFomat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
            moneyFormat = NumberFormat.getCurrencyInstance(locale);
        }

        private  String formatProduct(Product product){
            String type = switch (product){
                case Food food -> resources.getString("food");
                case Drink drink -> resources.getString("drink");
            };

            return MessageFormat.format(resources.getString("product"),
                    product.getName(),
                    moneyFormat.format(product.getPrice()),
                    product.getRating().getStars(),
                    dateFomat.format(product.getBestBefore()),
                    type);
        }

        private String formatReview(Review review){
            return MessageFormat.format(resources.getString("review"),
                    review.rating().getStars(),
                    review.comments());
        }

        private String getText(String key){
            return resources.getString(key);
        }

    }

}
//        review = new Review(rating, comments);



//        if(reviews[reviews.length - 1] != null){
//            reviews = Arrays.copyOf(reviews, reviews.length + 5);
//        }

//        int sum = 0, i = 0;
//        // Creamos una 'flag'
//        boolean reviewed = false;
//
//        while (i < reviews.length && !reviewed){
//            if(reviews[i] == null){
//                reviews[i] = new Review(rating, comments);
//                reviewed = true;
//            }
//            sum += reviews[i].rating().ordinal();
//            ++i;
//        }

//        this.product = product.applyRating(rating);
//        this.product = product.applyRating(Rateable.convert(Math.round((float)sum / i)));


//    public void printProductReport(){
//        StringBuilder txt = new StringBuilder();
//        txt.append(product); // Append llama al toString de Product
//        txt.append('\n');
//
//        if(review != null){
//            txt.append(review);
//        }else txt.append("Not reviewed.");
//
//        txt.append('\n');
//
//        System.out.println(txt);
//    }
