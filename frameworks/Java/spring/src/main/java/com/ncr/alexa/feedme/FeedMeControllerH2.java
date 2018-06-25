package com.ncr.alexa.feedme;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Embedded H2 Controller
 */
@Controller
@EnableAutoConfiguration
public final class FeedMeControllerH2 {

    public static final RowMapper<RecipeSummary> RECIPE_SUMMARY_ROW_MAPPER =
        (rs, rn) -> new RecipeSummary(
            rs.getString("recipe_name"),
            rs.getString("recipe_long_name"),
            rs.getString("recipe_description"),
            rs.getString("recipe_cuisine"));

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/plaintext")
    @ResponseBody
    String plaintext() {
        return "Hello, World!";
    }

    @RequestMapping("/user/{username}/recipeByIngredient")
    @ResponseBody
    List<RecipeSummary> recipeByIngredient(
            @PathVariable("username") String username,
            @RequestParam String ingredient1,
            @Nullable @RequestParam String cuisine,
            @Nullable @RequestParam Integer page) {

        var noCuisine = StringUtils.isBlank(cuisine);
        var params = noCuisine
                ? new Object[] { ingredient1.toUpperCase() }
                : new Object[] { ingredient1.toUpperCase(), cuisine.toUpperCase() };

        return
                jdbcTemplate.query(
                        "select \n" +
                                "  r.recipe_name,\n" +
                                "  r.recipe_long_name, \n" +
                                "  r.recipe_description,\n" +
                                "  r.recipe_cuisine\n" +
                                "from\n" +
                                "   feedme.recipes r\n" +
                                "inner join\n" +
                                "   feedme.recipe_ingredients i\n" +
                                "on\n" +
                                "   r.recipe_name = i.recipe_name\n" +
                                "where\n" +
                                "  UPPER(i.ingredient_name)=?\n" +
                                (noCuisine ? "" : "AND UPPER(r.recipe_cuisine)=?"),
                        RECIPE_SUMMARY_ROW_MAPPER,
                        params);
    }

    @RequestMapping("/user/{username}/recipeByTwoIngredients")
    @ResponseBody
    List<RecipeSummary> recipeByTwoIngredients(
            @PathVariable("username") String username,
            @RequestParam String ingredient1,
            @RequestParam String ingredient2,
            @Nullable @RequestParam String cuisine,
            @Nullable @RequestParam Integer page) {


        var noCuisine = StringUtils.isBlank(cuisine);
        var params = noCuisine
                ? new Object[] { ingredient1.toUpperCase(), ingredient2.toUpperCase() }
                : new Object[] { ingredient1.toUpperCase(), ingredient2.toUpperCase(), cuisine.toUpperCase() };

        return
                jdbcTemplate.query(
                        "select \n" +
                                "  r.recipe_name,\n" +
                                "  r.recipe_long_name, \n" +
                                "  r.recipe_description,\n" +
                                "  r.recipe_cuisine\n" +
                                "from\n" +
                                "  feedme.recipes r\n" +
                                "inner join\n" +
                                "   feedme.recipe_ingredients i1\n" +
                                "inner join\n" +
                                "  feedme.recipe_ingredients i2\n" +
                                "where\n" +
                                "  UPPER(i1.ingredient_name) = ?\n" +
                                "  and UPPER(i2.ingredient_name)=?\n" +
                                "  and i1.recipe_name=i2.recipe_name\n" +
                                "  and r.recipe_name=i1.recipe_name\n" +
                                (noCuisine ? "" : "AND UPPER(r.recipe_cuisine)=?"),
                        RECIPE_SUMMARY_ROW_MAPPER,
                        params);
    }

    @RequestMapping("/user/{username}/recipeByThreeIngredients")
    @ResponseBody
    List<RecipeSummary> recipeByThreeIngredients(
            @PathVariable("username") String username,
            @RequestParam String ingredient1,
            @RequestParam String ingredient2,
            @RequestParam String ingredient3,
            @Nullable @RequestParam String cuisine,
            @Nullable @RequestParam Integer page) {

        var noCuisine = StringUtils.isBlank(cuisine);
        var params = noCuisine
                ? new Object[] { ingredient1.toUpperCase(), ingredient2.toUpperCase(), ingredient3.toUpperCase() }
                : new Object[] { ingredient1.toUpperCase(), ingredient2.toUpperCase(), ingredient3.toUpperCase(),
                                 cuisine.toUpperCase() };

        return
                jdbcTemplate.query(
                        "select \n" +
                                "  r.recipe_name,\n" +
                                "  r.recipe_long_name, \n" +
                                "  r.recipe_description,\n" +
                                "  r.recipe_cuisine\n" +
                                "from\n" +
                                "  feedme.recipes r\n" +
                                "inner join\n" +
                                "   feedme.recipe_ingredients i1\n" +
                                "inner join\n" +
                                "  feedme.recipe_ingredients i2\n" +
                                "inner join\n" +
                                "  feedme.recipe_ingredients i3\n" +
                                "where\n" +
                                "  UPPER(i1.ingredient_name) = ?\n" +
                                "  and UPPER(i2.ingredient_name)=?\n" +
                                "  and UPPER(i3.ingredient_name)=?\n" +
                                "  and i1.recipe_name=i2.recipe_name\n" +
                                "  and i2.recipe_name=i3.recipe_name\n" +
                                "  and r.recipe_name=i1.recipe_name\n" +
                                (noCuisine ? "" : "AND UPPER(r.recipe_cuisine)=?"),
                        RECIPE_SUMMARY_ROW_MAPPER);
    }

    @RequestMapping("/recipeByName")
    @ResponseBody
    List<RecipeSummary> recipeByName(@RequestParam String name) {
        return
                jdbcTemplate.query(
                        "select \n" +
                                "  r.recipe_name,\n" +
                                "  r.recipe_long_name, \n" +
                                "  r.recipe_description,\n" +
                                "  r.recipe_cuisine\n" +
                                "from\n" +
                                "   feedme.recipes r\n" +
                                "where\n" +
                                "  UPPER(r.recipe_long_name) LIKE ?\n",
                        RECIPE_SUMMARY_ROW_MAPPER,
                        "%" + name.toUpperCase() + "%");
    }

    @RequestMapping("/recipeById")
    @ResponseBody
    Recipe recipeById(@RequestParam String id) {
        var summary =
                jdbcTemplate.query(
                        "select \n" +
                                "  r.recipe_name,\n" +
                                "  r.recipe_long_name, \n" +
                                "  r.recipe_description,\n" +
                                "  r.recipe_cuisine\n" +
                                "from\n" +
                                "   feedme.recipes r\n" +
                                "where\n" +
                                "  UPPER(r.recipe_name)=?\n",
                        RECIPE_SUMMARY_ROW_MAPPER,
                        id.toUpperCase());

        if (summary.isEmpty()) {
            return null;
        } else if (summary.size() > 1) {
            throw new IllegalStateException("Two recipes in the database with the same ID");
        }

        var ingredients =
                jdbcTemplate.query(
                        "select\n" +
                                "  i.ingredient_name,\n" +
                                "  i.ingredient_description,\n" +
                                "  i.ingredient_quantity,\n" +
                                "  i.ingredient_quantity_unit\n" +
                                "from\n" +
                                "  feedme.recipe_ingredients i\n" +
                                "where\n" +
                                "  i.recipe_name = ?",
                        (rs, rn) -> new Ingredient(
                                rs.getString("ingredient_name"),
                                rs.getString("ingredient_description"),
                                rs.getDouble("ingredient_quantity"),
                                Ingredient.Unit.valueOfSoft(rs.getString("ingredient_quantity_unit"))),
                        summary.get(0).getId());

        var steps =
                jdbcTemplate.query(
                        "select\n" +
                                "  s.step_id,\n" +
                                "  s.step_time_minutes,\n" +
                                "  s.step_description\n" +
                                "from\n" +
                                "  feedme.recipe_steps s\n" +
                                "where\n" +
                                "  s.recipe_name = ?\n" +
                                "order by\n" +
                                "  s.step_id",
                        (rs, rn) -> new RecipeStep(
                                rs.getInt("step_id"),
                                rs.getInt("step_time_minutes"),
                                rs.getString("step_description")),
                        summary.get(0).getId());

        return new Recipe(summary.get(0), ingredients, steps);
    }


    @RequestMapping(value = "/user/{username}/allergies", method = RequestMethod.GET)
    List<String> allergies(@PathVariable("username") String username) {
        return
                jdbcTemplate.queryForList(
                        "select\n" +
                                "  ingredient_name\n" +
                                "from\n" +
                                "  feedme.allergies\n" +
                                "where\n" +
                                "  user_name=?",
                        String.class,
                        username);
    }

    @RequestMapping(value = "/user/{username}/allergies", method = RequestMethod.POST)
    List<String> setAllergies(@PathVariable("username") String username, @RequestBody List<String> allergies) {
        deleteAllAllergies(username);
        updateAllergies(username, allergies);
        return allergies(username);
    }

    @RequestMapping(value = "/user/{username}/allergies", method = RequestMethod.PUT)
    List<String>  updateAllergies(@PathVariable("username") String username, @RequestBody List<String> allergies) {
        for (String allergy : allergies) {
            jdbcTemplate.execute("INSERT INTO feedme.allergies VALUES('" + username +"', '" + allergy + "')");
        }
        return allergies(username);
    }

    @RequestMapping(value = "/user/{username}/allergies", method = RequestMethod.DELETE)
    List<String>  deleteAllAllergies(@PathVariable("username") String username) {
        jdbcTemplate.execute("delete from feedme.allergies where user_name='" + username + "'");
        return Collections.emptyList();
    }
}

