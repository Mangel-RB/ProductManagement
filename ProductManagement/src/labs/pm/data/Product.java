/*
 * Copyright (c) 2026.
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package labs.pm.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static java.math.RoundingMode.HALF_UP;

import static  labs.pm.data.Rating.NOT_RATED;

/**
 * @author manana
 **/

/**
 * {@code Product} class represents properties and behaviors of
 * product objects in the Product Management System.
 * <br>
 * Each product has an id, name, and price
 * <br>
 * Each product can have a discount, calculated based on a
 * {@link DISCOUNT_RATE discount rate}
 * @version 4.0
 * @author oracle
 */

public abstract sealed class Product implements Rateable<Product> permits Food, Drink { // Solo pueden heredar de esta clase las permitidas

    private int id;
    private String name;
    private BigDecimal price;
    private Rating rating;

    /**
     * A constant that defines a
     * {@link java.math.BigDecimal BigDecimal} value of the discount rate
     * <br>
     * Discount rate is 10%
     */

    public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1); // En Mayus por ser una constante.

    Product() { // Empty Constructor
        this(0, "NO_NAME", BigDecimal.ZERO);
    }

    Product(int id, String name, BigDecimal price, Rating rating) { // Full constructor
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    Product(int id, String name, BigDecimal price) { // No Rating constructor
        // this(id, name, price, Rating.NOT_RATED); o con el import que puse
        this(id, name, price, NOT_RATED);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Rating getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return id + ", " + name + ", " + price + ", " + getDiscount() + ", " + rating.getStars() + ", " + getBestBefore();
    }

    /**
     * Calculates discount based on a product price and
     * {@link DISCOUNT_RATE discount rate}
     * @return a {@link java.math.BigDecimal BigDecimal}
     * value of the discount
     */

    public BigDecimal getDiscount(){
        return price.multiply(DISCOUNT_RATE).setScale(2, HALF_UP);
    }

    //public abstract Product applyRating(Rating newRating); // Ya no hace falta al estar implementada la interfaz

    /**
     * Assumes that the best before date is today
     * @return The current date
     */
    public LocalDate getBestBefore(){
        return LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if(o instanceof Product product) return id == product.id && Objects.equals(name, product.name);

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}