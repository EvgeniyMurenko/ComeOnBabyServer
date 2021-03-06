package com.ComeOnBaby.service;


import com.ComeOnBaby.model.RecipeGuide;

import java.util.List;

public interface RecipeGuideService {

    void addNewRecipeGuide(RecipeGuide recipeGuide);

    void updateRecipeGuide(RecipeGuide recipeGuide);

    void deleteRecipeGuide(RecipeGuide recipeGuide);

    List<RecipeGuide> getAllRecipeGuide();

    RecipeGuide getRecipeGuideById(long id);
}
