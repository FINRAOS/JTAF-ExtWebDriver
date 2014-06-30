/*
 * (C) Copyright 2013 Java Test Automation Framework Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.finra.jtaf.ewd.widget.element.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.finra.jtaf.ewd.ExtWebDriver;
import org.finra.jtaf.ewd.session.SessionManager;
import org.finra.jtaf.ewd.widget.ITable;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;

public class TableTest {

    public static String url = "http://localhost:29090/simpleapp/staticwidgetsapp.html";
    public ExtWebDriver wd;
	
	protected String tableLocator = "//table[@id=\"tableTest1\"]";
	protected String badTableLocator = "//table[@id=\"badTable\"]";
	protected String noSuchTableLocator = "//table[@id=\"noSuchTable\"]";
	protected List<String> expectedTableTest1Headers = new ArrayList<String>();
    
	@Before
    public void setup() throws Exception {
        wd = SessionManager.getInstance().getCurrentSession();
        
        expectedTableTest1Headers.add("Header 1");
        expectedTableTest1Headers.add("Header 2");
        expectedTableTest1Headers.add("Header 3");
        expectedTableTest1Headers.add("Header 4");
    }

    @After
    public void teardown() {
        wd.close();
        SessionManager.getInstance().removeSession(wd);
    }
    
    @Test
    public void testConstructorNoSuchTable() throws WidgetException{
    	boolean exceptionThrown = false;
    	try {
    		wd.open(url);
    		new Table(noSuchTableLocator);
    	} catch (NoSuchElementException nsee){
    		exceptionThrown = true;
    	}
    	Assert.assertFalse(exceptionThrown);
    }
    
    @Test
    public void testGetTableHeaders1() throws WidgetException{
    	wd.open(url);
    	ITable table = new Table(tableLocator);
    	List<String> actualHeaders = table.getTableHeaders();
    	Assert.assertEquals("Table header sizes are different!", expectedTableTest1Headers.size(), actualHeaders.size());
    	for (int i=0;i<actualHeaders.size();i++){
    		Assert.assertEquals("Table header string didn't match!", expectedTableTest1Headers.get(i), actualHeaders.get(i).trim());
    	}
    }
    
    @Test
    public void testGetTableRowColumnData1() throws WidgetException{
    	wd.open(url);
    	ITable table = new Table(tableLocator);
    	String cellValue = table.getTableRowColumnData(2, 2);
    	Assert.assertEquals("Table cell value didn't match!", "Row 2: Cell 2", cellValue.trim());
    	cellValue = table.getTableRowColumnData(3, 1);
    	Assert.assertEquals("Table cell value didn't match!", "Row 3: Cell 1", cellValue.trim());
    }
    
    
    @Test
    public void testGetTableDataInArray() throws WidgetException{
    	wd.open(url);
    	ITable table = new Table(tableLocator);
    	List<String[]> dataAsArray = table.getTableDataInArray();
    	Assert.assertEquals("Table data didn't match", dataAsArray.get(0)[0].trim(), "Row 1: Cell 1");
    	Assert.assertEquals("Table data didn't match", dataAsArray.get(0)[1].trim(), "Row 1: Cell 2");
    	Assert.assertEquals("Table data didn't match", dataAsArray.get(1)[0].trim(), "Row 2: Cell 1");
    }
    
    @Test
    public void testGetTableDataInMap() throws WidgetException{
     	wd.open(url);
    	ITable table = new Table(tableLocator);
    	List<Map<String,String>> dataAsMap = table.getTableDataInMap();
    	Assert.assertEquals("Table data didn't match", dataAsMap.get(0).get("Header 1").trim(), "Row 1: Cell 1");
    	Assert.assertEquals("Table data didn't match", dataAsMap.get(0).get("Header 2").trim(), "Row 1: Cell 2");
    	Assert.assertEquals("Table data didn't match", dataAsMap.get(1).get("Header 1").trim(), "Row 2: Cell 1");
   
    }
    
    @Test
    public void testGetTableRowCount() throws WidgetException{
    	wd.open(url);
    	ITable table = new Table(tableLocator);
    	int tableCount = table.getTableRowCount();
    	Assert.assertEquals("Error matching row count", tableCount, 3);
    }
    
    @Test
    public void testGetTableColumnCount() throws WidgetException{
    	wd.open(url);
    	ITable table = new Table(tableLocator);
    	int tableCount = table.getTableColumnCount();
    	Assert.assertEquals("Error matching row count", tableCount, 4);
    }
    
    @Test
    public void testIsItemExist() throws WidgetException{
    	wd.open(url);
    	ITable table = new Table(tableLocator);
    	Map<String, String> map = new HashMap<String,String>();
    	map.put("Header 1", "Row 1: Cell 1");
    	boolean isExist = table.isItemExist(map);
    	Assert.assertTrue(isExist);
    }
    
    @Test
    public void testGetRowNumber() throws WidgetException{
    	wd.open(url);
    	ITable table = new Table(tableLocator);
    	Map<String, String> map = new HashMap<String,String>();
    	map.put("Header 1", "Row 1: Cell 1");
    	int rowNumber = table.getRowNumber(map);
    	Assert.assertEquals(rowNumber, 1);
    }
    
    @Test
    public void testIsElementPresentNoSuchTable() throws WidgetException{
    	wd.open(url);
    	ITable table = new Table(noSuchTableLocator);
    	Assert.assertFalse(table.isElementPresent());
    }
    
    @Test
    public void testGetTableHeadersException() throws WidgetException{
    	boolean exceptionThrown = false;
    	try {
    		wd.open(url);
    		ITable table = new Table(badTableLocator);
    		table.getTableHeaders();
    	} catch (WidgetException we){
    		exceptionThrown = true;
    	}
    	Assert.assertTrue(exceptionThrown);
    }

    @Test
    public void testGetTableRowColumnDataException() throws WidgetException{
    	boolean exceptionThrown = false;
    	
    	try {
    		wd.open(url);
    		ITable table = new Table(badTableLocator);
    		table.getTableRowColumnData(1, 1);
    	} catch (WidgetException we){
    		exceptionThrown = true;
    	}
    	Assert.assertTrue(exceptionThrown);
    }

    @Test
    public void testGetTableDataInArrayException() throws WidgetException{
    	boolean exceptionThrown = false;
    	try {
    		wd.open(url);
    		ITable table = new Table(badTableLocator);
    		table.getTableDataInArray();
    	} catch (WidgetException we){
    		exceptionThrown = true;
    	}
    	Assert.assertTrue(exceptionThrown);
    }
    @Test
    public void testGetTableDataInMapException() throws WidgetException{
    	boolean exceptionThrown = false;
    	try {
    		wd.open(url);
    		ITable table = new Table(badTableLocator);
    		table.getTableDataInMap();
    	} catch (WidgetException we){
    		exceptionThrown = true;
    	}
    	Assert.assertTrue(exceptionThrown);
    }

    @Test
    public void testGetTableRowCountException() throws WidgetException{
    	boolean exceptionThrown = false;
    	try {
    		wd.open(url);
    		ITable table = new Table(badTableLocator);
    		table.getTableRowCount();
    	} catch (WidgetException we){
    		exceptionThrown = true;
    	}
    	Assert.assertTrue(exceptionThrown);
    }

    @Test
    public void testIsItemExistException() throws WidgetException{
    	boolean exceptionThrown = false;
    	try {
    		wd.open(url);
    		ITable table = new Table(badTableLocator);
    		table.isItemExist(null);
    	} catch (WidgetException we){
    		exceptionThrown = true;
    	}
    	Assert.assertTrue(exceptionThrown);
    }
    
    @Test
    public void testGetTableColumnCountException() throws WidgetException{
    	boolean exceptionThrown = false;
    	try {
    		wd.open(url);
    		ITable table = new Table(badTableLocator);
    		table.getTableColumnCount();
    	} catch (WidgetException we){
    		exceptionThrown = true;
    	}
    	Assert.assertTrue(exceptionThrown);
    }
}
