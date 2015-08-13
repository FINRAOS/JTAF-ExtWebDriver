/*
 * (C) Copyright 2013 Java Test Automation Framework Contributors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.finra.jtaf.ewd.widget;

import java.util.List;
import java.util.Map;

/**
 * This is the interface for the Table widget
 * 
 */
public interface ITable extends IContainerElement
{
    /**
     * Implementing this method would allow for obtaining all of the headers in
     * a table
     * 
     * @return a list containing all of the headers as Strings
     * @throws WidgetException
     */
    public List<String> getTableHeaders() throws WidgetException;

    /**
     * Implementing this method would allow for getting data from a specific
     * cell in the table
     * 
     * @param rowNumber
     *            - the desired row in the table
     * @param columnNumber
     *            - the desired column in the table
     * @return the text of the specified cell
     * @throws WidgetException
     */
    public String getTableRowColumnData(int rowNumber, int columnNumber) throws WidgetException;

    /**
     * Implementing this method would allow for getting all of the values in
     * table
     * 
     * @return a list containing all of the text in a table
     * @throws WidgetException
     */
    public List<String[]> getTableDataInArray() throws WidgetException;

    /**
     * Implementing this method would allow for getting all of text in the table
     * as a map
     * 
     * @return a List containing maps all of the text in a table as
     * @throws WidgetException
     */
    public List<Map<String, String>> getTableDataInMap() throws WidgetException;

    /**
     * Implementing this method would allow for getting the total number of rows
     * in the table
     * 
     * @return the number of rows in the table
     * @throws WidgetException
     */
    public int getTableRowCount() throws WidgetException;

    /**
     * Implementing this method would allow for getting the total number of
     * columns in the table
     * 
     * @return the number of columns in the table
     * @throws WidgetException
     */
    public int getTableColumnCount() throws WidgetException;

    /**
     * Implementing this method would allow for checking the table to see if an
     * item is present in a table
     * 
     * @param item
     *            - the map
     * @return
     * @throws WidgetException
     */
    public boolean isItemExist(Map<String, String> item) throws WidgetException;

    /**
     * Implementing this element would allow for getting the row number in a
     * table that contains specific information
     * 
     * @param item
     *            - a map used to check against each row until a match is found
     * @return the row number containing the text
     * @throws WidgetException
     */
    public int getRowNumber(Map<String, String> item) throws WidgetException;
}
