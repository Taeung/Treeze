package com.hansung.treeze.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.hansung.treeze.model.Mindmap;
import com.hansung.treeze.service.ImageService;
import com.hansung.treeze.service.MindmapService;

@Controller
public class ImageController {
	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
    @Autowired
	private ImageService imageService;
    @Autowired private MindmapService mindmapService;
	@Resource(name="jsonView")
	private View jsonView;
	@Resource(name="imageView")
	private View imageView;
	@Resource(name="thumbnailView")
	private View thumbnailView;

	@RequestMapping(value="/upload/img")
	public String uploadImg(@RequestParam("lectureName") String lectureName,@RequestParam("upload") MultipartFile multipartFile,@RequestParam("xml") String mindmapXML, ModelMap map) {
		logger.info("uploadImg" + lectureName + mindmapXML);
		Mindmap mindmap = new Mindmap(mindmapXML);
		mindmapService.save(mindmap);
		map.put("file", imageService.uploadImage(multipartFile, lectureName));
		mindmap = mindmapService.findByXML(mindmapXML);
		return "uploadImage";
	}
	
	
	@RequestMapping(value="/img/{id}")
	public ModelAndView image(@PathVariable Long id) {
		return this.getFileModelAndView(imageView, id, null);	
	}
	
	@RequestMapping(value="/thumb/{id}/{size}")
	public ModelAndView thumbnail(@PathVariable Long id, @PathVariable Integer size) {
		return this.getFileModelAndView(thumbnailView, id, size);	
	}
	
	private ModelAndView getFileModelAndView(View view, Long id, Integer size){
		ModelAndView modelAndView = new ModelAndView(view);
		modelAndView.addObject("uploadedFile", imageService.getUploadedImage(id));
		modelAndView.addObject("displaySize", size);
		return modelAndView;	
	}
}
