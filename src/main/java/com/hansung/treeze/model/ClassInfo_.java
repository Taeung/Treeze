package com.hansung.treeze.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ClassInfo.class)
public class ClassInfo_ {
	
	public static volatile SingularAttribute<ClassInfo, Integer> classId;
	public static volatile SingularAttribute<ClassInfo, String> className;
	public static volatile SingularAttribute<ClassInfo, String>  classIP;
	public static volatile SingularAttribute<ClassInfo, Integer> port;
	public static volatile SingularAttribute<ClassInfo, String> lectureName;
	public static volatile SingularAttribute<ClassInfo, String> professorEmail;

	
}
