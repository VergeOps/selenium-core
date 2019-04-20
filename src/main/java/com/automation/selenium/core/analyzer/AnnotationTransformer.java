package com.automation.selenium.core.analyzer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
/**
 * 
 * Class to instance the Retry.class to the RetryAnalizer
 * @author Amanda Adams
 *
 */
public class AnnotationTransformer implements IAnnotationTransformer {

    /* (non-Javadoc)
     * @see org.testng.IAnnotationTransformer#transform(org.testng.ITestAnnotation, java.lang.Class, java.lang.reflect.Constructor, java.lang.reflect.Method)
     */
	@SuppressWarnings("rawtypes")
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(Retry.class);
	}

}
