package com.ncr.alexa.feedme;

import java.util.List;

public class Recipe extends RecipeSummary {
    private final List<Ingredient> ingredientList;
    private final List<RecipeStep> steps;

    public Recipe(RecipeSummary summary, List<Ingredient> ingredientList, List<RecipeStep> steps) {
        super(summary.getId(), summary.getName(), summary.getDescription(), summary.getCuisine());
        this.ingredientList = ingredientList;
        this.steps = steps;
    }

    public Recipe(String id, String name, String description, String cuisine, List<Ingredient> ingredientList, List<RecipeStep> steps) {
        super(id, name, description, cuisine);
        this.ingredientList = ingredientList;
        this.steps = steps;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }
}
