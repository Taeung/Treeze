package com.hansung.treeze.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hansung.treeze.model.Note;
import com.hansung.treeze.model.Version;
import com.hansung.treeze.service.NoteService;

/*

11-1. 학생이 서버에게 Note정보를 보낸다.(송신)
- Rest : POST방식
- URL : http://113.198.84.74:8080/treeze/attachNotes
- Format : JSON
- Class name : Note
- Return Value :  Boolean

11-2. 학생이 서버로부터 Note정보를 갖고온다.(수신)
- Rest : GET방식
- URL : http://113.198.84.74:8080/treeze/getNotes?{classId}&{userEmail}&{position}
- Format : JSON
- Class name : Note
- Return Value :  Note 리스트를 json으로 

 * */

@Controller
public class NoteController {
	private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
	@Autowired private NoteService noteService;
	
	@RequestMapping(value="/createNote", method=RequestMethod.POST)
	public String createNote(Note model, ModelMap map) {
		
		Note note = noteService.getNote(model.getClassId(), model.getUserEmail(),model.getNodeId());

		if (note == null)
			note = noteService.saveNote(model);
		else{
			note.setClassId(model.getClassId());
			note.setUserEmail(model.getUserEmail());
			note.setNodeId(model.getNodeId());
			note.setContents(model.getContents());
			note = noteService.saveNote(note);
		}


		map.put("result", "success");

		return "jsonView";
	}
	
	@RequestMapping(value="/deleteNote", method=RequestMethod.POST)
	public String deleteNote(Note model, ModelMap map) {
		noteService.deleteNote(model);
		map.put("result", "success");

		return "jsonView";
	}

	@RequestMapping(value="/getNotes", method=RequestMethod.GET)
	public String getNotes(@RequestParam("classId") Long classId, @RequestParam("userEmail") String userEmail, ModelMap map) {

		map.put("Notes", noteService.getNotes(classId, userEmail));
		return "jsonView";	
	}
	
	@RequestMapping(value="/getNote", method=RequestMethod.GET)
	public String getNote(@RequestParam("classId") Long classId, @RequestParam("userEmail") String userEmail, @RequestParam("nodeId") String nodeId, ModelMap map) {

		map.put("Note", noteService.getNote(classId, userEmail,nodeId));
		return "jsonView";	
	}
	
}