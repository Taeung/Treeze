package com.treeze.data.Survey;
import java.util.ArrayList;


public interface SurveyType {
	
	public ArrayList<String> getSurveyExamplesInfo(); 
	
	public void fillOutSurvey(String answerOfSurvey);
	
	public void collectAnswerofSurvey();
	
	public ArrayList<Integer> getReultOfSurvey(); 
}