package com.hansung.treeze.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Course.class)
public class Course_ {

	public static volatile SingularAttribute<Course, Long> lectureId;
	public static volatile SingularAttribute<Course, String> lectureName;
	public static volatile SingularAttribute<Course, String> studentEmail;
	
}