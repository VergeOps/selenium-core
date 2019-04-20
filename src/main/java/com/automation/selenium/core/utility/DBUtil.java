package com.automation.selenium.core.utility;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * Class for making basic SQL calls leveraging MyBatis
 * @author Amanda Adams
 *
 */
public class DBUtil {

	private static HashMap<String, SqlSessionFactory> sessionMap;
	private static Logger logger = LogManager.getLogger();

	private static SqlSessionFactory getSessionFactory(String config) {
		if (sessionMap == null)
			sessionMap = new HashMap<String, SqlSessionFactory>();

		if (!sessionMap.containsKey(config)) {
			InputStream inputStream = null;
			try {
				inputStream = Resources.getResourceAsStream(config);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, System.getProperties());
			sessionMap.put(config, sqlSessionFactory);
		}

		return sessionMap.get(config);
	}

	/**
     * Method to query database using a specified config and select statement
     * 
     * @param config Configuration file with db settings
     * @param select Id of query to execute
     * @return  A list of the type of item specified by the select statement
     */
    public static List<?> getItems(String config, String select) {

            SqlSession session = getSessionFactory(config).openSession();
            switchSchema(session);
            List<?> items = session.selectList(select);
            session.close();
             
            return items;
    }
    
    /**
     * Method to query database using a specified config and select statement
     * 
     * @param config Configuration file with db settings
     * @param select Id of query to execute
     * @param parameters HashMap to use in creating prepared statements
     * @return  A list of the type of item specified by the select statement
     */
    public static List<?> getItems(String config, String select, HashMap<String, Object> parameters) {

            SqlSession session = getSessionFactory(config).openSession();
            switchSchema(session);
            List<?> items = session.selectList(select, parameters);
            session.close();
             
            return items;
    }
    
    /**
     * Method to execute an update statement
     * 
     * @param config Configuration file with db settings
     * @param update Id of query to execute
     * @return  Number of records altered by update
     */
    public static int executeUpdate(String config, String update) {
    	 SqlSession session = getSessionFactory(config).openSession();
    	 switchSchema(session);
         int rowsAltered = session.update(update);
         session.commit();
         session.close();
         
         return rowsAltered;
    }
    
    /**
     * Method to execute an update statement
     * 
     * @param config Configuration file with db settings
     * @param update Id of query to execute
     * @param parameters HashMap to use in creating prepared statements
     * @return  Number of records altered by update
     */
    public static int executeUpdate(String config, String update, HashMap<String, Object> parameters) {
   	 SqlSession session = getSessionFactory(config).openSession();
   	    switchSchema(session);
        int rowsAltered = session.update(update, parameters);
        session.commit();
        session.close();
        
        return rowsAltered;
   }
    
    /**
     * change the db schema in the specified SQL session
     * 
     * the schema to change to, is the one specified via "schema.name" env variable 
     * 
     * @param session the SQL Session to apply the schema change
     */
    public static void switchSchema(SqlSession session) {
    	
      logger.info("Entering schema method");
      if (System.getProperty(PropertyConstants.SCHEMA_NAME_PROPERTY) != null
          && !System.getProperty(PropertyConstants.SCHEMA_NAME_PROPERTY).equals("")) {
        try {
          logger.info("Switching to schema: " + System.getProperty(PropertyConstants.SCHEMA_NAME_PROPERTY));
          session.getConnection().setSchema(System.getProperty(PropertyConstants.SCHEMA_NAME_PROPERTY));
        } catch (SQLException e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }   
      }
    }
}