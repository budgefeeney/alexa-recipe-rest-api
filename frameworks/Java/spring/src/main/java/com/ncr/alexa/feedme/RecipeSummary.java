package com.ncr.alexa.feedme;

import java.util.Objects;

public class RecipeSummary {
    private final String id;
    private final String name;
    private final String description;
    private final String cuisine;

    public RecipeSummary(String id, String name, String description, String cuisine) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cuisine = cuisine;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCuisine() {
        return cuisine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeSummary that = (RecipeSummary) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
