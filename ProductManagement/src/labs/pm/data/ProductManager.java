/*
 * Copyright (c) 2026.
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package labs.pm.data;

import com.sun.tools.javac.Main;
import labs.pm.data.exceptions.ProductManagerException;

import java.awt.color.ProfileDataException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author manana
 **/

public class ProductManager {
    private Product product;
    private Review review;

    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());

    private ResourceBundle config = ResourceBundle.getBundle("labs.pm.data.config");
    private MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
    private MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));

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

//        int sum= 0;
//        for (Review review: reviews){
//            sum += review.rating().ordinal();
//        }
//
//        this.product = product.applyRating(Rateable.convert(Math.round(sum/reviews.size())));

        product = product.applyRating(
                                      Rateable.convert(
                                                       (int) Math.round(reviews.stream()
                                                                         .mapToInt(r -> r.rating().ordinal())
                                                                         .average()
                                                                         .orElse(0)
                                                                 )
                                                       )
                                      );

        products.putIfAbsent(product, reviews);

        return product;
    }

    public Product reviewProduct ( int id, Rating rating, String comments){
        try {
            return reviewProduct(findProduct(id), rating, comments);
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
            return null;
        }
    }

    public void printProductReport(Product product){

        List<Review> reviews = products.get(product);


        StringBuilder txt = new StringBuilder();


        txt.append(formatter.formatProduct(product));
        txt.append('\n');

//        for (Review review : reviews) {
//            if (review == null){
//                break;
//            }
//
//            txt.append(formatter.formatReview(review));
//            txt.append('\n');
//
//            if (reviews.isEmpty()){
//                txt.append(formatter.getText("no.reviews"));
//                txt.append('\n');
//            }
//
//        }

        if(reviews.isEmpty()){
            txt.append(formatter.getText("no.reviews") + '\n');
        }else txt.append(reviews.stream()
                                .map(r -> formatter.formatReview(r) + '\n')
                                .collect(Collectors.joining())
                        );
        // Otra forma de hacer lo del else sería:
        // reviews.stream().forEach(r -> txt.append(formatter.formatReview(r) + '\n'));

//        txt.append('\n');

        Collections.sort(reviews);

        System.out.println(txt);
    }

    public void printProductReport(int id){
        try {
            printProductReport(findProduct(id));
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        }
    }

    public void printProducts(Predicate<Product> filter, Comparator<Product> sorter) {
//        List<Product> productList = new ArrayList<>(products.keySet());
//        productList.sort(sorter);

        StringBuilder txt = new StringBuilder();

        products.keySet()
                .stream()
                .sorted(sorter)
                .filter(filter)
                .forEach(p -> txt.append(formatter.formatProduct(product) + '\n'));

//        for(Product product: productList){
//            txt.append(formatter.formatProduct(product));
//            txt.append('\n');
//        }

        System.out.println(txt);

    }

    public void parseReview(String text) throws ProductManagerException {
        try {
            Object[] values = reviewFormat.parse(text);
            reviewProduct(Integer.parseInt((String) values[0]),
                    Rateable.convert(Integer.parseInt((String) values[1])),
                    (String) values[2]);
        } catch (ParseException | NumberFormatException e) {
            logger.log(Level.WARNING, "Error parsing review " + text, e.getMessage());
            throw new ProductManagerException("Unable to parse review");
        }
    }

    public void parseProduct(String text){
        try{
            Object[] values = productFormat.parse(text);

            int id = Integer.parseInt((String) values[1]);

            String name = (String) values[2];

            BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String) values[3]));

            Rating rating = Rateable.convert(Integer.parseInt((String) values[4]));

            switch ((String) values[0]){
                case "D":
                    createProduct(id, name, price, rating);
                    break;
                case "F":
                    LocalDate bestBefore  = LocalDate.parse ((String) values[5], formatter.dateFomat);
                    createProduct(id, name, price, rating, bestBefore);
                    break;
            }

        } catch (ParseException | NumberFormatException | DateTimeParseException e) {
//            throw new RuntimeException(e);
            logger.log(Level.WARNING, "Error parsing product " + text, e.getMessage());
        }
    }

    public Map <String, String> getDiscounts(){



        return products.keySet()
                       .stream()
                       .collect(Collectors.groupingBy((Product product) -> product.getRating().getStars(),
                               Collectors.collectingAndThen(
                                       Collectors.summingDouble(
                                               (Product product) -> product.getDiscount().doubleValue()
                                       ),
                                       (Double discount) -> formatter.moneyFormat.format(discount)
                               )
                       ));
    }

    public Product findProduct (int id) throws ProductManagerException {
//        Product result = null;
//
//        for (Product product : products.keySet()) {
//            if (product.getId() == id) {
//                result = product;
//                break;
//            }
//        }
//        return result;

        return products.keySet()
                       .stream()
                       .filter(p -> p.getId() == id)
                       .findFirst()
//                       .orElseGet(() -> null)
//                       .get()
                       .orElseThrow(() -> new ProductManagerException("Product with id: " + id + " not found."))
                       ;
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
