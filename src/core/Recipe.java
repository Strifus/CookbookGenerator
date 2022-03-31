package core;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Recipe {
	private String name;
	private String category;
	
	public Size size;
	public Time time;
	public ArrayList<Ingredient> ingredients;
	public ArrayList<Preparation> preparation;

	public Recipe(String name) {
		this.setName(name);
		this.setCategory("");
		this.size = new Size("", "");
		this.time = new Time("", "");
		
		this.ingredients = new ArrayList<Ingredient>();
		this.preparation = new ArrayList<Preparation>();
	}

	/**
	 * Adds ingredient containing name, unit and value to recipe.
	 * 
	 * @param ingredient Ingredient to add
	 */
	public void addIngredient(Ingredient ingredient) {
		this.ingredients.add(ingredient);
		
	}
	
	/**
	 * Clears the list with the ingredients.
	 */
	public void clearIngredient() {
		this.ingredients.clear();
	}
	
	/**
	 * Adds preparation step to recipe.
	 * 
	 * @param step Description of preparation step.
	 */
	public void addPreparation(Preparation step) {
		this.preparation.add(step);
	}
	
	/**
	 * Clears the list with preparation steps.
	 */
	public void clearPreparation() {
		this.preparation.clear();
	}

	/**
	 * Gets the name of the recipe.
	 * 
	 * @return Recipe title
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the recipe.
	 * 
	 * @param name Recipe title
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the category, e.g. Starter, Main, Dessert,..., of the recipe.
	 * 
	 * @return Recipe category
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * Sets the category, e.g. Starter, Main, Dessert,..., of the recipe.
	 * 
	 * @param category Recipe category
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * Writes whole recipe to file in JSON format.
	 * 
	 * @param fileName Recipe file name
	 */
	public void writeJson(String fileName) {
		try {
		    // Create Gson instance
	        Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
	
		    // Create a writer
		    Writer writer = Files.newBufferedWriter(Paths.get(fileName));
	
		    // Convert recipe object to JSON file
		    gson.toJson(this, writer);
	
		    // Close writer
		    writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads recipe from JSON file.
	 * 
	 * @param fileName Recipe file name
	 * @return Recipe object
	 */
	public Recipe readJson(String fileName) {
		Recipe recipe = new Recipe("Default Recipe Title");

		try {     
	        // Create Gson instance
	        Gson gson = new Gson();
	
		    // Create a reader
		    Reader reader = Files.newBufferedReader(Paths.get(fileName));
	
		    // convert JSON string to Book object
		    recipe = gson.fromJson(reader, Recipe.class);
	
		    // Close writer
		    reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return recipe;
	}

}
