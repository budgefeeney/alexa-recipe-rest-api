package com.ncr.alexa.feedme;

import static java.util.Comparator.comparing;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.xml.SqlXmlFeatureNotImplementedException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Neo4J controller.
 */
@Controller
@EnableAutoConfiguration
public final class FeedMeController {

  public static void main(String[] args) {
    SpringApplication.run(FeedMeControllerH2.class, args);
  }

//  @Autowired
//  JdbcTemplate jdbcTemplate;
//
//  @RequestMapping("/plaintext")
//  @ResponseBody
//  String plaintext() {
//    return "Hello, World!";
//  }
//
//
//  private static RowMapper<RecipeSummary> RECIPE_SUMMARY_MAPPER = new RowMapper<RecipeSummary>() {
//    @Override
//    public RecipeSummary mapRow(ResultSet rs, int i) throws SQLException {
//      System.out.println ("Mapping row " + i);
//      try {
//        HashMap<String,Object> map =
//                new ObjectMapper().readValue(rs.getString(1), HashMap.class);
//
//        return new RecipeSummary(
//                String.valueOf(map.get("id")),
//                String.valueOf(map.get("name")),
//                String.valueOf(map.get("description")),
//                String.valueOf(map.get("cuisine")));
//
//      } catch (IOException ioe) {
//        throw new SQLException("Unparseable return type" + ioe.getMessage(), ioe);
//      }
//    }
//  };
//
//
//  @RequestMapping("/user/{username}/recipeByIngredient")
//  @ResponseBody
//  List<RecipeSummary> recipeByIngredient(
//          @PathVariable("username") String username,
//          @RequestParam String ingredient1,
//          @Nullable @RequestParam String cuisine,
//          @Nullable @RequestParam Integer page) {
//
//    return jdbcTemplate.query (
//            "match \n" +
//                    "\t(r:Recipe), (i1:Ingredient)\n" +
//                    (StringUtils.isBlank(cuisine) ? "" : "\t, (c:Cuisine) \n") +
//                    "where \n" +
//                    "\t(r)--(i1) and i1.name =~ '(?i)" + ingredient1 + "'\n" +
//                    (StringUtils.isBlank(cuisine) ? "" : "\tand c.name =~ '(?i)" + cuisine + "'\n") +
//                    "return r",
//            RECIPE_SUMMARY_MAPPER);
//  }
//
//  @RequestMapping("/user/{username}/recipeByTwoIngredients")
//  @ResponseBody
//  List<RecipeSummary> recipeByTwoIngredients(
//          @PathVariable("username") String username,
//          @RequestParam String ingredient1,
//          @RequestParam String ingredient2,
//          @Nullable @RequestParam String cuisine,
//          @Nullable @RequestParam Integer page) {
//    return jdbcTemplate.query (
//            "match \n" +
//                    "\t(r:Recipe), (i1:Ingredient), (i2:Ingredient)\n" +
//                    (StringUtils.isBlank(cuisine) ? "" : "\t, (c:Cuisine) \n") +
//                    "where \n" +
//                    "\t(r)--(i1) and i1.name =~ '(?i)" + ingredient1 + "'\n" +
//                    "\tand (r)--(i2) and i2.name =~ '(?i)" + ingredient2 + "' \n" +
//                    "\tand (i1) <> (i2) \n" +
//                    (StringUtils.isBlank(cuisine) ? "" : "\tand c.name =~ '(?i)" + cuisine + "'\n") +
//                    "return r",
//            RECIPE_SUMMARY_MAPPER);
//  }
//
//  @RequestMapping("/user/{username}/recipeByThreeIngredients")
//  @ResponseBody
//  List<RecipeSummary> recipeByThreeIngredients(
//          @PathVariable("username") String username,
//          @RequestParam String ingredient1,
//          @RequestParam String ingredient2,
//          @RequestParam String ingredient3,
//          @Nullable @RequestParam String cuisine,
//          @Nullable @RequestParam Integer page) {
//    throw new UnsupportedOperationException("Three ingredient search not supported");
//    // It broke the DB!
////    return
////            jdbcTemplate.query(
////                    "match \n" +
////                            "(r:Recipe), (i1:Ingredient), (i2:Ingredient), (i3:Ingredient)\n" +
////                            (StringUtils.isBlank(cuisine) ? "" : ", (c:Cuisine) \n") +
////                            "where \n" +
////                            "(r)--(i1) and i1.name =~ '(?i)" + ingredient1 + "' \n" +
////                            "and (r)--(i2) and i2.name =~ '(?i)" + ingredient2 + "' \n" +
////                            "and (r)--(i3) and i3.name =~ '(?i)"  + ingredient3 +"' \n" +
////                            "and (i1) <> (i2) and (i2) <> (i1) and (i1) <> (i3) \n" +
////                            (StringUtils.isBlank(cuisine) ? "" : "and c.name =~ '(?i)" + cuisine + "' \n") +
////                            "return r",
////                    (rs, rn) -> new RecipeSummary(
////                                  rs.getString("recipe_name"),
////                                  rs.getString("recipe_long_name"),
////                                  rs.getString("recipe_description"),
////                                  rs.getString("recipe_cuisine")));
//  }
//
//  @RequestMapping("/recipeByName")
//  @ResponseBody
//  List<RecipeSummary> recipeByName(@RequestParam String name) {
//    return
//            jdbcTemplate.query(
//                    "select \n" +
//                            "  r.recipe_name,\n" +
//                            "  r.recipe_long_name, \n" +
//                            "  r.recipe_description,\n" +
//                            "  r.recipe_cuisine\n" +
//                            "from\n" +
//                            "   feedme.recipes r\n" +
//                            "where\n" +
//                            "  UPPER(r.recipe_long_name) LIKE '%" + name.toUpperCase() + "%'\n", // Nothing could ever go wrong with this...
//                    (rs, rn) -> new RecipeSummary(
//                            rs.getString("recipe_name"),
//                            rs.getString("recipe_long_name"),
//                            rs.getString("recipe_description"),
//                            rs.getString("recipe_cuisine")));
//  }
//
//  @RequestMapping("/recipeById")
//  @ResponseBody
//  Recipe recipeById(@RequestParam String id) {
//    var summary =
//            jdbcTemplate.query(
//                    "select \n" +
//                            "  r.recipe_name,\n" +
//                            "  r.recipe_long_name, \n" +
//                            "  r.recipe_description,\n" +
//                            "  r.recipe_cuisine\n" +
//                            "from\n" +
//                            "   feedme.recipes r\n" +
//                            "where\n" +
//                            "  UPPER(r.recipe_name)='" + id.toUpperCase() + "'\n", // Guess who hasn't done their NCR security course...
//                    (rs, rn) -> new RecipeSummary(
//                            rs.getString("recipe_name"),
//                            rs.getString("recipe_long_name"),
//                            rs.getString("recipe_description"),
//                            rs.getString("recipe_cuisine")));
//
//    if (summary.isEmpty()) {
//      return null;
//    } else if (summary.size() > 1) {
//      throw new IllegalStateException("Two recipes in the database with the same ID");
//    }
//
//    var ingredients =
//            jdbcTemplate.query(
//                    "select\n" +
//                            "  i.ingredient_name,\n" +
//                            "  i.ingredient_description,\n" +
//                            "  i.ingredient_quantity,\n" +
//                            "  i.ingredient_quantity_unit\n" +
//                            "from\n" +
//                            "  feedme.recipe_ingredients i\n" +
//                            "where\n" +
//                            "  i.recipe_name = '" + summary.get(0).getId() + "'",
//                    (rs, rn) -> new Ingredient(
//                            rs.getString("ingredient_name"),
//                            rs.getString("ingredient_description"),
//                            rs.getDouble("ingredient_quantity"),
//                            Ingredient.Unit.valueOf(rs.getString("ingredient_quantity_unit"))));
//
//    var steps =
//            jdbcTemplate.query(
//                    "select\n" +
//                            "  s.step_id,\n" +
//                            "  s.step_time_minutes,\n" +
//                            "  s.step_description\n" +
//                            "from\n" +
//                            "  feedme.recipe_steps s\n" +
//                            "where\n" +
//                            "  s.recipe_name = '" + summary.get(0).getId() + "'\n" +
//                            "order by\n" +
//                            "  s.step_id",
//                    (rs, rn) -> new RecipeStep(
//                            rs.getInt("step_id"),
//                            rs.getInt("step_time_minutes"),
//                            rs.getString("step_description")));
//
//    return new Recipe(summary.get(0), ingredients, steps);
//  }
//
//
//  @RequestMapping(value = "/user/{username}/allergies", method = RequestMethod.GET)
//  List<String> allergies(@PathVariable("username") String username) {
//    return
//            jdbcTemplate.queryForList(
//                    "select\n" +
//                            "  ingredient_name\n" +
//                            "from\n" +
//                            "  feedme.allergies\n" +
//                            "where\n" +
//                            "  user_name='" + username + "'", // I trust you guys, we're all internet friends...
//                    String.class);
//  }
//
//  @RequestMapping(value = "/user/{username}/allergies", method = RequestMethod.POST)
//  List<String> setAllergies(@PathVariable("username") String username, @RequestBody List<String> allergies) {
//    deleteAllAllergies(username);
//    updateAllergies(username, allergies);
//    return allergies(username);
//  }
//
//  @RequestMapping(value = "/user/{username}/allergies", method = RequestMethod.PUT)
//  List<String>  updateAllergies(@PathVariable("username") String username, @RequestBody List<String> allergies) {
//    for (String allergy : allergies) {
//      jdbcTemplate.execute("INSERT INTO feedme.allergies VALUES('" + username +"', '" + allergy + "')");
//    }
//    return allergies(username);
//  }
//
//  @RequestMapping(value = "/user/{username}/allergies", method = RequestMethod.DELETE)
//  List<String>  deleteAllAllergies(@PathVariable("username") String username) {
//    jdbcTemplate.execute("delete from feedme.allergies where user_name='" + username + "'");
//    return Collections.emptyList();
//  }
//
//
//
//  @RequestMapping("/db")
//  @ResponseBody
//  World db() {
//    return randomWorld();
//  }
//
//  @RequestMapping("/queries")
//  @ResponseBody
//  World[] queries(@RequestParam String queries) {
//    var worlds = new World[parseQueryCount(queries)];
//    Arrays.setAll(worlds, i -> randomWorld());
//    return worlds;
//  }
//
//  @RequestMapping("/updates")
//  @ResponseBody
//  World[] updates(@RequestParam String queries) {
//    var worlds = new World[parseQueryCount(queries)];
//    Arrays.setAll(worlds, i -> randomWorld());
//    for (var world : worlds) {
//      world.randomNumber = randomWorldNumber();
//      jdbcTemplate.update(
//          "UPDATE world SET randomnumber = ? WHERE id = ?",
//          world.randomNumber,
//          world.id);
//    }
//    return worlds;
//  }
//
//  @RequestMapping("/fortunes")
//  @ModelAttribute("fortunes")
//  List<Fortune> fortunes() {
//    var fortunes =
//        jdbcTemplate.query(
//            "SELECT * FROM fortunes",
//            (rs, rn) -> new Fortune(rs.getInt("id"), rs.getString("message")));
//
//    fortunes.add(new Fortune(0, "Additional fortune added at request time."));
//    fortunes.sort(comparing(fortune -> fortune.message));
//    return fortunes;
//  }
//
//  private World randomWorld() {
//    return jdbcTemplate.queryForObject(
//        "SELECT * FROM world WHERE id = ?",
//        (rs, rn) -> new World(rs.getInt("id"), rs.getInt("randomnumber")),
//        randomWorldNumber());
//  }
//
//  private static int randomWorldNumber() {
//    return 1 + ThreadLocalRandom.current().nextInt(10000);
//  }
//
//  private static int parseQueryCount(String textValue) {
//    if (textValue == null) {
//      return 1;
//    }
//    int parsedValue;
//    try {
//      parsedValue = Integer.parseInt(textValue);
//    } catch (NumberFormatException e) {
//      return 1;
//    }
//    return Math.min(500, Math.max(1, parsedValue));
//  }


}

