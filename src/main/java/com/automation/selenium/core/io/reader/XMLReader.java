package com.automation.selenium.core.io.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import com.automation.selenium.core.io.Res;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * XMLReader a class for reads xml files
 * @author Amanda Adams
 *
 */
public class XMLReader {
	
	private URL file;
	
	/**
	 * Public constructor
	 * 
	 * @param file the file name of the XML file
	 */
	public XMLReader(String file) {
		this.file = Res.getResource(file);
	}
	
	/**
	 * Method to read an XML file into an object
	 * @param <T> the class type to return
	 * 
	 * @param xmlType the class mapping type to load the XML file
	 * @param <T> the class type
	 * @return the object for use 
	 */
	public <T> T loadObject(Class<T> xmlType) {
		
		T object = null;
	
		XmlMapper xmlMapper = new XmlMapper();
		try {
			object = xmlMapper.readValue(readFileToString(), xmlType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return object;
		
	}
	
	/**
	 * Method to read an XML file into a list of object
	 * @param <T> Type of objects to populate in returned list
	 * 
	 * @param xmlType the class mapping type to load the XML file
	 * @param <T> the class type
	 * @return the list for use 
	 */
	public <T> List<T> loadList(Class<T> xmlType) {
		
		List<T> list = null;
		
		XmlMapper xmlMapper = new XmlMapper();
		try {
			list = xmlMapper.readValue(readFileToString(), 
					xmlMapper.getTypeFactory().constructCollectionType(List.class, xmlType));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * Method to read an XML file into a HashMap with a String key and an Object value
	 * @param <T> Type of objects to reference by string keys in returned HashMap
	 * 
	 * @param xmlType the class mapping type to load the XML file
	 * @param <T> the class type
	 * @param keyField the field to use to access the key on the object
	 * @return the HashMap for use 
	 */
	public <T> HashMap<String, T> loadHashMap(Class<T> xmlType, String keyField) {
		
		HashMap<String, T> map = new HashMap<String, T>();
		Method method = null;
		try {
			method = xmlType.getDeclaredMethod("get" + keyField);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		List<T> items = loadList(xmlType);
		if (items != null) {
			for (T item : items) {
				String keyValue = "";
				try {
					keyValue = (String) method.invoke(item);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				
				map.put(keyValue, item);
			}
			
		}
		
		return map;
	}
	
	private String readFileToString() {
		BufferedReader br = null;
		InputStream is = null;
		
		StringBuilder sb = new StringBuilder();
		
		try {
			is = new FileInputStream(file.getFile());
			br = new BufferedReader(new InputStreamReader(is));
			
			String line;
			while ((line = br.readLine()) != null) {
			   sb.append(line);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return sb.toString();
	}	
}