package com.ncr.alexa.feedme;

import static java.util.Comparator.comparing;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public final class FeedMeController {

  public static void main(String[] args) {
    SpringApplication.run(FeedMeController.class, args);
  }

  @Autowired
  JdbcTemplate jdbcTemplate;

  @RequestMapping("/plaintext")
  @ResponseBody
  String plaintext() {
    return "Hello, World!";
  }

  @RequestMapping("/recipeByIngredient")
  @ResponseBody
  List<RecipeSummary> recipeByIngredient(@RequestParam String ingredient1, @Nullable @RequestParam String cuisine, @Nullable @RequestParam Integer page) {
    return
            jdbcTemplate.query(
                    "select \n" +
                            "  r.recipe_long_name, \n" +
                            "  r.recipe_description\n" +
                            "from\n" +
                            "   feedme.recipes r\n" +
                            "inner join\n" +
                            "   feedme.recipe_ingredients i\n" +
                            "on\n" +
                            "   r.recipe_name = i.recipe_name\n" +
                            "where\n" +
                            "  i.ingredient_name='" + ingredient1 + "'\n" + // lets take a little moment to remember poor Bobby tables...
                            (StringUtils.isBlank(cuisine) ? "" : "AND r.recipe_cusine='" + cuisine + "'"),
                    (rs, rn) -> new RecipeSummary(rs.getString("recipe_long_name"), rs.getString("recipe_description")));
  }

  @RequestMapping("/recipeByTwoIngredients")
  @ResponseBody
  List<RecipeSummary> recipeByTwoIngredients(@RequestParam String ingredient1, @RequestParam String ingredient2, @Nullable @RequestParam String cuisine, @Nullable @RequestParam Integer page) {
    return
            jdbcTemplate.query(
                    "select \n" +
                            "  r.recipe_long_name,\n" +
                            "  r.recipe_description\n" +
                            "from\n" +
                            "  feedme.recipes r\n" +
                            "inner join\n" +
                            "   feedme.recipe_ingredients i1\n" +
                            "inner join\n" +
                            "  feedme.recipe_ingredients i2\n" +
                            "where\n" +
                            "  i1.ingredient_name = '" + ingredient1 +"'\n" + // what do you mean '; DROP FROM users' is not a valid ingredient!
                            "  and i2.ingredient_name='" + ingredient2 + "'\n" +
                            "  and i1.recipe_name=i2.recipe_name\n" +
                            "  and r.recipe_name=i2.recipe_name\n" + // lets take a little moment to remember poor Bobby tables...
                            (StringUtils.isBlank(cuisine) ? "" : "AND r.recipe_cusine='" + cuisine + "'"),
                    (rs, rn) -> new RecipeSummary(rs.getString("recipe_long_name"), rs.getString("recipe_description")));
  }

  @RequestMapping("/recipeByName")
  @ResponseBody
  List<RecipeSummary> recipeByName(@RequestParam String name) {
    return
            jdbcTemplate.query(
                    "select \n" +
                            "  r.recipe_long_name, \n" +
                            "  r.recipe_description\n" +
                            "from\n" +
                            "   feedme.recipes r\n" +
                            "where\n" +
                            "  r.recipe_name='" + name + "'\n", // Nothing could ever go wrong with this...
                    (rs, rn) -> new RecipeSummary(rs.getString("recipe_long_name"), rs.getString("recipe_description")));
  }



  @RequestMapping("/db")
  @ResponseBody
  World db() {
    return randomWorld();
  }

  @RequestMapping("/queries")
  @ResponseBody
  World[] queries(@RequestParam String queries) {
    var worlds = new World[parseQueryCount(queries)];
    Arrays.setAll(worlds, i -> randomWorld());
    return worlds;
  }

  @RequestMapping("/updates")
  @ResponseBody
  World[] updates(@RequestParam String queries) {
    var worlds = new World[parseQueryCount(queries)];
    Arrays.setAll(worlds, i -> randomWorld());
    for (var world : worlds) {
      world.randomNumber = randomWorldNumber();
      jdbcTemplate.update(
          "UPDATE world SET randomnumber = ? WHERE id = ?",
          world.randomNumber,
          world.id);
    }
    return worlds;
  }

  @RequestMapping("/fortunes")
  @ModelAttribute("fortunes")
  List<Fortune> fortunes() {
    var fortunes =
        jdbcTemplate.query(
            "SELECT * FROM fortunes",
            (rs, rn) -> new Fortune(rs.getInt("id"), rs.getString("message")));

    fortunes.add(new Fortune(0, "Additional fortune added at request time."));
    fortunes.sort(comparing(fortune -> fortune.message));
    return fortunes;
  }

  private World randomWorld() {
    return jdbcTemplate.queryForObject(
        "SELECT * FROM world WHERE id = ?",
        (rs, rn) -> new World(rs.getInt("id"), rs.getInt("randomnumber")),
        randomWorldNumber());
  }

  private static int randomWorldNumber() {
    return 1 + ThreadLocalRandom.current().nextInt(10000);
  }

  private static int parseQueryCount(String textValue) {
    if (textValue == null) {
      return 1;
    }
    int parsedValue;
    try {
      parsedValue = Integer.parseInt(textValue);
    } catch (NumberFormatException e) {
      return 1;
    }
    return Math.min(500, Math.max(1, parsedValue));
  }

}
