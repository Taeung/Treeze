/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/*$Id: NodeKeyListener.java,v 1.16.18.2 2006/01/12 23:10:12 christianfoltin Exp $*/

package freemind.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import freemind.Frame.SurveyFrame;
import freemind.json.ArrayLecture;
import freemind.json.ArrayTicket;
import freemind.json.FreemindGson;
import freemind.json.Lecture;
import freemind.json.Ticket;
import freemind.json.TmpTicket;
import freemind.json.TreezeData;
//import freemind.main.ProfileFrame.LectureListItem;
import freemind.modes.MindMapNode;
import freemind.modes.NodeAdapter;

/**
 * The KeyListener which belongs to the node and cares for Events like C-D
 * (Delete Node). It forwards the requests to NodeController.
 */
public class NodeKeyListener implements KeyListener {

	private Controller c;
	private KeyListener mListener;
	private FreemindManager fManager;
	private boolean pressedShiftKey = false; 
	
	public NodeKeyListener(Controller controller) {
		c = controller;
		fManager = FreemindManager.getInstance();
	}

	public void register(KeyListener listener) {
		this.mListener = listener;
	}

	public void deregister() {
		mListener = null;
	}

	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_SHIFT){
			pressedShiftKey = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_F5) {
			if(pressedShiftKey){
				if(c.getSlideShow().getfocus() == null)
					return;
				
				c.getSlideShow().show();
				
				c.getSlideShow().sendPosition();
				return;
			}
 			c.startSlideShow();
			
		} else if (e.getKeyCode() == KeyEvent.VK_F6) {
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_F4){
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_F3){
						
		}
		else if(e.getKeyCode() == KeyEvent.VK_F8){
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_F9){
			//set Q node
			
//			if (!fManager.isAddQuestionNodeInfo()) {
//				fManager.setAddQuestionNodeInfo(true);
//
//				c.addQNode.addNodeForQuestion(c.getMc().getRootNode());
//
//				// modify Q node
//				c.addQNode.modifyForQuestion(c.getMc().getRootNode());
//				c.getMc().edit.stopEditing();
//
//				/*
//				modify last node, why not change
//				in modifyForQuestion() method?????
//				*/
//				NodeAdapter tmp = (NodeAdapter)c.getMc().getSelected();
//				tmp.setText("Q");
//				tmp.setNodeTypeStr("Question");
//				c.getMap().nodeChanged(tmp);
//
//				System.out.println("NodeKeyListener : set QuestionNodeInfo");
//
//			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_F10){
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_F16){
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_F15){
		}
		else if(e.getKeyCode() == KeyEvent.VK_F14){
		}
 		
		if (mListener != null)
			mListener.keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		if (mListener != null)
			mListener.keyReleased(e);
		if(e.getKeyCode() == KeyEvent.VK_SHIFT)
			pressedShiftKey = false;
	}

	public void keyTyped(KeyEvent e) {
		if (mListener != null)
			mListener.keyTyped(e);
	}
	
}
	

