package com.ComeOnBaby.controller;

import com.ComeOnBaby.enums.MainPathEnum;
import com.ComeOnBaby.model.FertilizationGuide;
import com.ComeOnBaby.model.RecipeGuide;
import com.ComeOnBaby.service.FertilizationGuideService;
import com.ComeOnBaby.service.RecipeGuideService;
import com.ComeOnBaby.util.ImageEditFunctions;
import com.ComeOnBaby.util.SaveFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Controller
@RequestMapping("/guide")
@SessionAttributes("roles")
public class GuideController {

    @Autowired
    RecipeGuideService recipeGuideService;

    //Recipe guide all methods

    @RequestMapping(value = "/featured-recipes", method = RequestMethod.GET)
    public ModelAndView featuredRecipes() {
        ModelAndView featuredRecipes = new ModelAndView("featuredRecipes");

        ArrayList<RecipeGuide> recipeGuides = (ArrayList<RecipeGuide>) recipeGuideService.getAllRecipeGuide();

        featuredRecipes.addObject("recipeGuide", recipeGuides);

        return featuredRecipes;
    }


    @RequestMapping(value = "/add-recipe", method = RequestMethod.GET)
    public ModelAndView addRecipe() {
        ModelAndView addRecipe = new ModelAndView("featuredRecipesEdit");
        addRecipe.addObject("isNew", true);
        return addRecipe;
    }

    @RequestMapping(value = "/edit-recipe/{recipeId}", method = RequestMethod.GET)
    public ModelAndView editRecipe(@PathVariable Long recipeId) {
        ModelAndView editRecipe = new ModelAndView("featuredRecipesEdit");

        RecipeGuide recipeGuide = recipeGuideService.getRecipeGuideById(recipeId);

        editRecipe.addObject("isNew", false);
        editRecipe.addObject("recipeGuide", recipeGuide);

        return editRecipe;

    }

    @RequestMapping(value = "/delete-image-from-recipe-guide/{recipeId}", method = RequestMethod.GET)
    public ModelAndView deleteImageFromGuide(@PathVariable(value = "recipeId") Long recipeId) {

        RecipeGuide recipeGuide = recipeGuideService.getRecipeGuideById(recipeId);

        ImageEditFunctions.deleteImage(recipeGuide.getImageThumbnail());
        recipeGuide.setImageThumbnail(null);
        recipeGuideService.updateRecipeGuide(recipeGuide);

        return new ModelAndView("redirect:/guide/edit-recipe/" + recipeGuide.getId());
    }

    @RequestMapping(value = "/delete-image-from-fertilization-guide/{guideId}", method = RequestMethod.GET)
    public ModelAndView deleteImageFromFertilization(@PathVariable(value = "guideId") Long guideId) {

        FertilizationGuide fertilizationGuide = fertilizationGuideService.getFertilizationGuideById(guideId);

        ImageEditFunctions.deleteImage(fertilizationGuide.getImage());
        fertilizationGuide.setImage(null);
        fertilizationGuideService.updateFertilizationGuide(fertilizationGuide);

        return new ModelAndView("redirect:/guide/edit-fertilization/" + fertilizationGuide.getId());
    }

    @RequestMapping(value = "/save-new-recipe", method = RequestMethod.POST)
    public ModelAndView saveNewRecipe(@RequestParam(value = "id") String id,
                                      @RequestParam("title") String title,
                                      @RequestParam("url") String naverUrl,
                                      @RequestParam("filePicture[]") MultipartFile[] files) {

        RecipeGuide recipeGuide = new RecipeGuide();

        if (id.equals("")) {

            recipeGuide = new RecipeGuide();

            recipeGuide.setDate(new Date());
            recipeGuide.setTitle(title);
            recipeGuide.setUrlNaver(naverUrl);

            recipeGuideService.addNewRecipeGuide(recipeGuide);
            //Save to file

            if (!files[0].isEmpty()) {
                String pathToSaveFile = "pictures/";
                SaveFile saveFile = new SaveFile(pathToSaveFile, files);
                ArrayList<String> fileNames = saveFile.saveFileAndGetName();
                recipeGuide.setImageThumbnail(MainPathEnum.mainWebPath + "show-image/" + fileNames.get(0));
                recipeGuideService.updateRecipeGuide(recipeGuide);
            }

        } else {

            recipeGuide = recipeGuideService.getRecipeGuideById(Long.valueOf(id));

            recipeGuide.setDate(new Date());
            recipeGuide.setTitle(title);
            recipeGuide.setUrlNaver(naverUrl);

            //Save to file
            if (files.length != 0) {
                if (!files[0].isEmpty()) {
                    String pathToSaveFile = "pictures/";
                    SaveFile saveFile = new SaveFile(pathToSaveFile, files);
                    ArrayList<String> fileNames = saveFile.saveFileAndGetName();
                    if (recipeGuide.getImageThumbnail()!=null) ImageEditFunctions.deleteImage(recipeGuide.getImageThumbnail());
                    recipeGuide.setImageThumbnail(MainPathEnum.mainWebPath + "show-image/" + fileNames.get(0));
                }
            }
            recipeGuideService.updateRecipeGuide(recipeGuide);
        }

        return new ModelAndView("redirect:/guide/edit-recipe/" + recipeGuide.getId());
    }

    @RequestMapping(value = "/delete-recipe/{id}", method = RequestMethod.GET)
    public ModelAndView deleteRecipe(@PathVariable Long id) {

        RecipeGuide recipeGuide = recipeGuideService.getRecipeGuideById(id);

        ImageEditFunctions.deleteImage(recipeGuide.getImageThumbnail());

        recipeGuideService.deleteRecipeGuide(recipeGuide);

        return new ModelAndView("redirect:/guide/featured-recipes");
    }


    //Fertilization guide all methods

    @Autowired
    FertilizationGuideService fertilizationGuideService;

    @RequestMapping(value = "/fertilization", method = RequestMethod.GET)
    public ModelAndView fertilization() {

        ModelAndView fertilizationGuide = new ModelAndView("fertilizationGuide");

        ArrayList<FertilizationGuide> fertilizationGuides = (ArrayList<FertilizationGuide>) fertilizationGuideService.getAllFertilizationGuide();

        fertilizationGuide.addObject("fertilizationGuides", fertilizationGuides);

        return fertilizationGuide;
    }

    @RequestMapping(value = "/add-fertilization", method = RequestMethod.GET)
    public ModelAndView addFertilization() {

        ModelAndView addFertilization = new ModelAndView("fertilizationGuideEdit");

        addFertilization.addObject("isNew", true);

        return addFertilization;
    }

    @RequestMapping(value = "/edit-fertilization/{id}", method = RequestMethod.GET)
    public ModelAndView editFertilization(@PathVariable Long id) {
        ModelAndView editFertilization = new ModelAndView("fertilizationGuideEdit");

        FertilizationGuide fertilizationGuide = fertilizationGuideService.getFertilizationGuideById(id);

        editFertilization.addObject("isNew", false);
        editFertilization.addObject("fertilizationGuide", fertilizationGuide);

        return editFertilization;
    }

    @RequestMapping(value = "/save-new-fertilization", method = RequestMethod.POST)
    public ModelAndView saveNewFertilization(@RequestParam(value = "id") String id,
                                             @RequestParam("title") String title,
                                             @RequestParam("url") String url,
                                             @RequestParam("filePicture[]") MultipartFile[] files) {

        FertilizationGuide fertilizationGuide = new FertilizationGuide();

        if (id.equals("")) {

            fertilizationGuide = new FertilizationGuide();

            fertilizationGuide.setDate(new Date());
            fertilizationGuide.setTitle(title);
            fertilizationGuide.setUrl(url);

            fertilizationGuideService.addNewFertilizationGuide(fertilizationGuide);

            //Save to file

            if (!files[0].isEmpty()) {
                String pathToSaveFile = "pictures/";
                SaveFile saveFile = new SaveFile(pathToSaveFile, files);
                ArrayList<String> fileNames = saveFile.saveFileAndGetName();
                if (fertilizationGuide.getImage()!=null) ImageEditFunctions.deleteImage(fertilizationGuide.getImage());
                fertilizationGuide.setImage(MainPathEnum.mainWebPath + "show-image/" + fileNames.get(0));
                fertilizationGuideService.updateFertilizationGuide(fertilizationGuide);
            }

        } else {

            fertilizationGuide = fertilizationGuideService.getFertilizationGuideById(Long.valueOf(id));

            fertilizationGuide.setDate(new Date());
            fertilizationGuide.setTitle(title);
            fertilizationGuide.setUrl(url);

            System.out.println("================= url === " + url) ;
            fertilizationGuideService.updateFertilizationGuide(fertilizationGuide);

            //Save to file
            if (files.length != 0) {
                if (!files[0].isEmpty()) {
                    String pathToSaveFile = "pictures/";
                    SaveFile saveFile = new SaveFile(pathToSaveFile, files);
                    ArrayList<String> fileNames = saveFile.saveFileAndGetName();
                    if (fertilizationGuide.getImage()!=null) ImageEditFunctions.deleteImage(fertilizationGuide.getImage());
                    fertilizationGuide.setImage(MainPathEnum.mainWebPath + "show-image/" + fileNames.get(0));
                    fertilizationGuideService.updateFertilizationGuide(fertilizationGuide);
                }
            }

        }

        return new ModelAndView("redirect:/guide/edit-fertilization/" + fertilizationGuide.getId());
    }

    @RequestMapping(value = "/delete-fertilization/{id}", method = RequestMethod.GET)
    public ModelAndView deleteFertilization(@PathVariable Long id) {

        FertilizationGuide fertilizationGuide = fertilizationGuideService.getFertilizationGuideById(id);

        ImageEditFunctions.deleteImage(fertilizationGuide.getImage());
        fertilizationGuideService.deleteFertilizationGuide(fertilizationGuide);

        return new ModelAndView("redirect:/guide/fertilization");
    }

}
