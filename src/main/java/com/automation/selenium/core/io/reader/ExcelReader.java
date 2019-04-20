package com.automation.selenium.core.io.reader;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.automation.selenium.core.io.Res;


/**
 * A class for working with Excel files
 * @author Amanda Adams
 *
 */
public class ExcelReader {

    private final List<Map<String, String>> data;
    private List<String> columns;

    /**
     * Constructor to create an Excel Reader
     * 
     * @param filePath the file name of the excel file
     * @param spreadsheetName the sheet name of the excel file
     * @throws IOException the exception throws if the method can't read the file
     */
    public ExcelReader(String filePath, String spreadsheetName) throws IOException {
        data = read(Res.getResource(filePath), spreadsheetName);
    }

    /**
     * Gets the data from the excel sheet in a List of Hashtables
     * format
     *
     * @return the data from the excel sheet
     */
    public List<Map<String, String>> data() {
        return data;
    }
    
    /**
     * Method to return data in an object array for TestNG data providers
     * 
     * @return the object for use as a data provider
     */
    public Object[][] convertedData() {
    	Object[][] convert = new Object[data.size()][columns.size()];
		for (int i = 0; i < data.size(); i++){
			Object[] item = new Object[columns.size()];
			Map<String, String> subList = data.get(i);
			for (int j = 0; j < columns.size(); j++) {
				item[j] = subList.get(columns.get(j));
			}
			convert[i] = item;
		}
		
		return convert;
    }

    /**
     * Get all values associated with the specified column throughout the entire
     * sheet
     *
     * @param column the column name 
     * @return values the list with the values of the column
     */
    public List<String> getValues(String column) {
        List<String> columnValues = new ArrayList<String>();
        for (Map<String, String> ht : data()) {
            columnValues.add(ht.get(column));
        }
        return columnValues;
    }

    private List<Map<String, String>> read(URL sheetUrl, String sheetName) throws IOException {
        List<Map<String, String>> contents = new ArrayList<Map<String, String>>();
        Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(sheetUrl.openStream());
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		}
		
		Sheet sheet = workbook.getSheet(sheetName);
        
        Iterator<Row> rows = sheet.iterator();
        columns = new ArrayList<String>();
        
        boolean firstRow = true;
        while (rows.hasNext()) {
            Row row = rows.next();
            Iterator<Cell> cells = row.cellIterator();
            if (firstRow) {
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    columns.add(cell.getStringCellValue());
                }
                firstRow = false;
            } else {
                Map<String, String> rowData = new HashMap<String, String>();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    rowData.put(columns.get(cell.getColumnIndex()), cell.toString());
                }
                contents.add(rowData);
            }
        }
        return contents;
    }
}