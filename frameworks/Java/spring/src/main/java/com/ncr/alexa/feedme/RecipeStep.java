package com.ncr.alexa.feedme;

import java.util.Objects;

public class RecipeStep implements Comparable<RecipeStep> {
    private final int stepIndex;
    private final int atInstanceSinceStartMinutes;
    private final String description;

    public RecipeStep(int stepIndex, int atInstanceSinceStartMinutes, String description) {
        this.stepIndex = stepIndex;
        this.atInstanceSinceStartMinutes = atInstanceSinceStartMinutes;
        this.description = description;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public int getAtInstanceSinceStartMinutes() {
        return atInstanceSinceStartMinutes;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeStep that = (RecipeStep) o;
        return stepIndex == that.stepIndex &&
                atInstanceSinceStartMinutes == that.atInstanceSinceStartMinutes &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(stepIndex, atInstanceSinceStartMinutes, description);
    }

    @Override
    public int compareTo(RecipeStep o) {
        return this.stepIndex - o.stepIndex;
    }
}
