package com.ComeOnBaby.controller;


import com.ComeOnBaby.comparator.CommentByDateComparator;
import com.ComeOnBaby.model.AppUser;
import com.ComeOnBaby.model.Blog;
import com.ComeOnBaby.model.Comment;
import com.ComeOnBaby.model.Notice;
import com.ComeOnBaby.service.AppUserService;
import com.ComeOnBaby.service.BlogService;
import com.ComeOnBaby.service.CommentsService;
import com.ComeOnBaby.util.ImageEditFunctions;
import com.ComeOnBaby.util.SaveFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


@Controller
@RequestMapping("/my")
@SessionAttributes("roles")
public class StoryAndRecipesController {

    @Autowired
    BlogService blogService;

    @RequestMapping(value = "/story", method = RequestMethod.GET)
    public ModelAndView mySuccessStory() {

        ModelAndView mySuccessStory = new ModelAndView("mySuccessStory");
        ArrayList<Blog> blogs = (ArrayList<Blog>) blogService.findBlogByType(3);
        mySuccessStory.addObject("type", 3);
        mySuccessStory.addObject("title", "자유 게시판");
        mySuccessStory.addObject("blogs", blogs);
        return mySuccessStory;
    }

    @RequestMapping(value = "/recipes", method = RequestMethod.GET)
    public ModelAndView myRecipes() {

        ModelAndView myRecipes = new ModelAndView("mySuccessStory");
        ArrayList<Blog> blogs = (ArrayList<Blog>) blogService.findBlogByType(2);
        myRecipes.addObject("type", 2);
        myRecipes.addObject("blogs", blogs);
        myRecipes.addObject("title", "나의 레시피");
        return myRecipes;
    }

    @RequestMapping(value = "/husband", method = RequestMethod.GET)
    public ModelAndView myHusband() {

        ModelAndView myHusband = new ModelAndView("mySuccessStory");
        ArrayList<Blog> blogs = (ArrayList<Blog>) blogService.findBlogByType(4);
        myHusband.addObject("type", 4);
        myHusband.addObject("title", "남편 이야기");
        myHusband.addObject("blogs", blogs);
        return myHusband;
    }


    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editBlog(@PathVariable Long id) {

        ModelAndView editBlog = new ModelAndView("mySuccessStoryEdit");

        Blog blog = blogService.findById(id);

        String images = blog.getImages();
        List<String> imagesList = new ArrayList<>();

        if (images != null && !"".equals(images)){
            imagesList = Arrays.asList(images.split("<>"));
        }

        editBlog.addObject("images", imagesList);
        editBlog.addObject("isNew", false);
        editBlog.addObject("blog", blog);

        return editBlog;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView deleteBlog(@PathVariable Long id) {
        Blog blog = blogService.findById(id);

        String images = blog.getImages();
        List<String> imagesList = new ArrayList<>();

        if (images != null && !"".equals(images)){
            imagesList = Arrays.asList(images.split("<>"));

            if(imagesList.size() != 0){
                for (String fileName : imagesList){
                    ImageEditFunctions.deleteImage(fileName);
                }
            }
        }

        ModelAndView listBlogs = new ModelAndView("redirect:/my/" + getBlogType(blog));

        blogService.deleteBlog(blog);

        return listBlogs;
    }

    @RequestMapping(value = "/new/{type}", method = RequestMethod.GET)
    public ModelAndView newBlog(@PathVariable Integer type) {

        ModelAndView newBlog = new ModelAndView("mySuccessStoryEdit");

        newBlog.addObject("type", type);
        newBlog.addObject("isNew", true);

        return newBlog;

    }

    @RequestMapping(value = "/save-new-story", method = RequestMethod.POST)
    public ModelAndView saveNewStory(@RequestParam(value = "id") String id,
                                     @RequestParam("type") Integer type,
                                     @RequestParam("title") String title,
                                     @RequestParam("text") String text,
                                     @RequestParam("filePicture[]") MultipartFile[] files) {

        System.out.println("=======================save============= " + type);

        Blog blog;

        if (id.equals("")) {

            blog = new Blog();
            AppUser appUser = appUserService.findById((long) 1);

            blog.setDatetime(new Date());
            blog.setTitle(title);
            blog.setText(text);
            blog.setType(type);
            blog.setAppUser(appUser);
            blog.setId_user(appUser.getId());
            blogService.addNewBlog(blog);

            //Save to file

            System.out.println("================file length " + files.length);

            if (files.length >0 && !files[0].isEmpty()) {
                System.out.println("===============save file==========");
                String pathToSaveFile = "pictures/";
                SaveFile saveFile = new SaveFile(pathToSaveFile, files);
                ArrayList<String> fileNames = saveFile.saveFileAndGetName();

                String noticeFileNames = ImageEditFunctions.generateStringWithSeparatorFromArray(fileNames);

                blog.setImages(noticeFileNames);

                blogService.updateBlog(blog);
            }

        } else {

            blog = blogService.findById(Long.valueOf(id));

            blog.setDatetime(new Date());
            blog.setTitle(title);
            blog.setText(text);

            //Save to file
            String imagesFromNotice = blog.getImages();

            String imagesUpdated = ImageEditFunctions.updateImages(imagesFromNotice, files);
            blog.setImages(imagesUpdated);

            blogService.updateBlog(blog);
        }

        return new ModelAndView("redirect:/my/edit/" + blog.getId());
    }


    @Autowired
    CommentsService commentsService;

    @RequestMapping(value = "/comments/{id}", method = RequestMethod.GET)
    public ModelAndView comments(@PathVariable Long id) {

        ModelAndView comments = new ModelAndView("mySuccessStoryComments");

        Blog blog = blogService.findById(id);

        Set<Comment> commentsUnsorted = blog.getComments();
        ArrayList<Comment> commentsSorted = new ArrayList<>(commentsUnsorted);

        Collections.sort(commentsSorted, new CommentByDateComparator());

        comments.addObject("blog", blog);
        comments.addObject("comments", commentsSorted);

        return comments;

    }

    @Autowired
    AppUserService appUserService;

    @RequestMapping(value = "/write-comment", method = RequestMethod.POST)
    public ModelAndView writeComment(@RequestParam("id") Long blogId, @RequestParam("text") String commentsText) {
        ModelAndView comments = new ModelAndView("redirect:/my/comments/" + blogId);

        Blog blog = blogService.findById(blogId);
        Set<Comment> commentsSet = blog.getComments();

        //find Admin AppUser
        AppUser appUser = appUserService.findById((long) 1);

        //create Comment
        Comment comment = new Comment();
        comment.setBlog(blog);
        comment.setAppUser(appUser);
        comment.setDatetime(new Date());
        comment.setText(commentsText);
        commentsService.addNewComments(comment);

        //add Comment to Blog
        commentsSet.add(comment);
        blog.setComments(commentsSet);
        blogService.updateBlog(blog);


        return comments;
    }

    @RequestMapping(value = "/delete-comment/{id}/{blogId}", method = RequestMethod.GET)
    public ModelAndView deleteComment(@PathVariable Long id, @PathVariable Long blogId) {

        ModelAndView comments = new ModelAndView("redirect:/my/comments/" + blogId);

        Comment comment = commentsService.findCommentById(id);
        commentsService.deleteComments(comment);

        return comments;
    }

    @RequestMapping(value = "/delete-image-from-story/{blogId}/{imageIndex}", method = RequestMethod.GET)
    public ModelAndView deleteImageFromNotice(@PathVariable(value = "blogId") Long blogId, @PathVariable(value = "imageIndex") Integer imageIndex) {


        Blog blog = blogService.findById(blogId);

        String blogFilesNamesBeforeUpdate = blog.getImages();

        ArrayList<String> imagesList = new ArrayList(Arrays.asList(blogFilesNamesBeforeUpdate.split("<>")));
        ImageEditFunctions.deleteImage(imagesList.get(imageIndex.intValue()));

        imagesList.remove(imageIndex.intValue());

        String imageNamesToSave = ImageEditFunctions.generateStringWithSeparatorFromArray(imagesList);

        blog.setImages(imageNamesToSave);

        blogService.updateBlog(blog);


        return new ModelAndView("redirect:/my/edit/" + blogId);
    }


    private String getBlogType(Blog blog) {

        int type = blog.getType();
        String path = "";
        switch (type) {
            case 2:
                path = "recipes";
                break;
            case 3:
                path = "story";
                break;
            case 4:
                path = "husband";
                break;
        }
        return path;
    }

}
