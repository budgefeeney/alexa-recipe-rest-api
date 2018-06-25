Recipe REST Service
=====================
An API with the ability to search for recipes by name, ID, ingredient or cuisin.

Works with an embedded H2 database.

Partially works with a Neo4j database, however you need to comment/uncomment code and re-compile.

Build & Run Steps
=====================

To build
```
[alexa-service]$ cd frameworks/Java/spring/
[spring]$ mvn clean package
```

To run

```
[spring]$ java -jar target/hello-spring*.jar
```

This creates a database in your home directory, with files of the form `alexa.*.db`. 

You can't start this up if there are old database files lying around


APIs
======================
This provides the following APIs for H2. For Neo4j (commented out by default) only the recipesByIngredient work

```
/user/{username}/recipeByIngredient
Params: ingredient1, [cuisine]
Returns: RecipeSummary
Notes: username can be anything, and is ignored

/user/{username}/recipeByTwoIngredients
Params: ingredient1, ingredient2, [cuisine]
Returns: RecipeSummary

/user/{username}/recipeByThreeIngredients
Params: ingredient1, ingredient2, ingredient3, [cuisine]
Returns: RecipeSummary

/recipeByName
Params: name
Returns: Recipe summary
Notes: This can work on fragments of name, e.g you can just put "pie" in here to get all pies

/recipeById
Params: id
Returns: Recipe detail
Notes: This is what you call to get the recipe ingredients and steps
```

A sample call is http://ec2-34-244-187-65.eu-west-1.compute.amazonaws.com:8080/recipeById?id=CourgetteLinguine (edited)

