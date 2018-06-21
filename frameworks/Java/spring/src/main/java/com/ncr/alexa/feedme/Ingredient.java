package com.ncr.alexa.feedme;

import java.util.Objects;

public class Ingredient {
    enum Unit {
        unit, gram, tspn, tblspn, packet, ml, liter ;

        public String toString(boolean isOne) {
            if (unit.equals(this)) {
                return "";
            }

            final String result;
            switch (this){
                case ml: result = "milliliters"; break;
                case gram: result = "grams"; break;
                case liter: result = "liters"; break;
                case packet: result = "packets"; break;
                case tblspn: result = "tablespoons"; break;
                case tspn: result = "teaspoons"; break;
                default:
                    throw new IllegalStateException("No code defined to convert " + this + " to a string");
            }

            return isOne
                ? result.substring(0, result.length() - 1) + " of"
                : result + " of";
        }
    }

    private final String id;
    private final String description;
    private final double quantity;
    private final Unit quantityUnit;
    private final String quantityText;

    public Ingredient(String id, String description, double quantity, Unit quantityUnit) {
        this.id = id;
        this.description = description;
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;

        if (quantity > 0.999 && quantity < 1.001) {
            this.quantityText = "one " + quantityUnit.toString(true);
        } else {
            this.quantityText = quantity + " " + quantityUnit.toString(false);
        }
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getQuantity() {
        return quantity;
    }

    public Unit getQuantityUnit() {
        return quantityUnit;
    }

    public String getQuantityText() {
        return quantityText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Double.compare(that.quantity, quantity) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(quantityUnit, that.quantityUnit);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, quantity, quantityUnit);
    }
}
