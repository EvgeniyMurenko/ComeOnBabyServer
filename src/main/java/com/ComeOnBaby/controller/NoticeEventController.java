package com.ComeOnBaby.controller;


import com.ComeOnBaby.model.Notice;
import com.ComeOnBaby.service.NoticeService;
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
import java.util.List;

@Controller
@RequestMapping("/notice")
@SessionAttributes("roles")
public class NoticeEventController {


    @Autowired
    NoticeService noticeService;


    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public ModelAndView noticeEvents() {
        ModelAndView noticeEvents = new ModelAndView("noticeEvent");

        ArrayList<Notice> notices = (ArrayList<Notice>) noticeService.getAllNotice();

        noticeEvents.addObject("notices", notices);

        return noticeEvents;
    }

    @RequestMapping(value = "/add-notice", method = RequestMethod.GET)
    public ModelAndView addNotice() {
        ModelAndView addNotice = new ModelAndView("noticeEventEdit");
        addNotice.addObject("isNew", true);
        return addNotice;
    }


    @RequestMapping(value = "/edit-notice/{noticeId}", method = RequestMethod.GET)
    public ModelAndView editNotice(@PathVariable Long noticeId) {
        ModelAndView editNotice = new ModelAndView("noticeEventEdit");

        Notice notice = noticeService.get(noticeId);
        String images = notice.getImages();

        List<String> imagesList = Arrays.asList(images.split("<>"));

        editNotice.addObject("images", imagesList);
        editNotice.addObject("isNew", false);
        editNotice.addObject("notice", notice);

        return editNotice;

    }


    @RequestMapping(value = "/delete-notice/{id}", method = RequestMethod.GET)
    public ModelAndView deleteNotice(@PathVariable Long id) {

        Notice notice = noticeService.get(id);

        ArrayList<String> imagesList = new ArrayList(Arrays.asList(notice.getImages().split("<>")));

        if(imagesList.size() != 0){
            for (String fileName : imagesList){
                ImageEditFunctions.deleteImage(fileName);
            }
        }

        noticeService.deleteNotice(notice);

        return new ModelAndView("redirect:/notice/events");
    }

    @RequestMapping(value = "/delete-image-from-notice/{noticeId}/{imageIndex}", method = RequestMethod.GET)
    public ModelAndView deleteImageFromNotice(@PathVariable(value = "noticeId") Long noticeId, @PathVariable(value = "imageIndex") Integer imageIndex) {


        Notice notice = noticeService.get(noticeId);

        String noticeFilesNamesBeforeUpdate = notice.getImages();


        ArrayList<String> imagesList = new ArrayList(Arrays.asList(noticeFilesNamesBeforeUpdate.split("<>")));
        ImageEditFunctions.deleteImage(imagesList.get(imageIndex.intValue()));

        imagesList.remove(imageIndex.intValue());

        String imageNamesToSave = ImageEditFunctions.generateStringWithSeparatorFromArray(imagesList);

        notice.setImages(imageNamesToSave);

        noticeService.updateNotice(notice);


        return new ModelAndView("redirect:/notice/edit-notice/" + noticeId);
    }

    @RequestMapping(value = "/save-new-notice", method = RequestMethod.POST)
    public ModelAndView saveNewRecipe(@RequestParam(value = "id") String id, @RequestParam("title") String title,
                                      @RequestParam("text") String noticeText, @RequestParam("filePicture[]") MultipartFile[] files) {

        Notice notice;

        if (id.equals("")) {

            notice = new Notice();

            notice.setDate(new Date());
            notice.setTitle(title);
            notice.setText(noticeText);


            //Save to file
            if (files != null && !files[0].isEmpty()) {
                String pathToSaveFile = "pictures/";
                SaveFile saveFile = new SaveFile(pathToSaveFile, files);

                ArrayList<String> fileNames = saveFile.saveFileAndGetName();

                String noticeFileNames = ImageEditFunctions.generateStringWithSeparatorFromArray(fileNames);

                notice.setImages(noticeFileNames);

                noticeService.addNewNotice(notice);
            }else{
                notice.setImages("");

                noticeService.addNewNotice(notice);
            }

        } else {

            notice = noticeService.get(Long.valueOf(id));

            notice.setDate(new Date());
            notice.setTitle(title);
            notice.setText(noticeText);

            String imagesFromNotice = notice.getImages();

            String imagesUpdated = ImageEditFunctions.updateImages(imagesFromNotice, files);
            notice.setImages(imagesUpdated);

            noticeService.updateNotice(notice);
        }

        return new ModelAndView("redirect:/notice/edit-notice/" + notice.getId());
    }


}
