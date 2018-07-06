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
 * The interface for the Table widget which is selectable
 * 
 */
public interface IInteractiveTable extends ITable, IInteractiveElement
{
    /**
     * Implementing this method would allow for selecting a specific row in a
     * table based on the row number
     * 
     * @param rowNumber
     *            - the desired row to be selected
     * @throws WidgetException
     */
    void selectRow(int rowNumber) throws WidgetException;

    /**
     * Implementing this method would allow for selecting a specific row in a
     * table based on contents of the row
     * 
     * @param item
     *            - a map containing key-value pairs, both of which are Strings,
     *            used to select the row that contains the same values
     * @throws WidgetException
     */
    void selectRow(Map<String, String> item) throws WidgetException;

    /**
     * Implementing this method would allow for selecting a specific row in a
     * table based on contents of the row
     * 
     * @param item
     *            - a map containing key-value pairs, with the key as a String
     *            and the value as an Object, used to select the row that
     *            contains the same values
     * @throws WidgetException
     */
    void selectRowElement(Map<String, Object> item) throws WidgetException;

    /**
     * Implementing this method would allow for selecting multiple rows in a
     * table based on their row numbers
     * 
     * @param rowNumbers
     *            - an array containing all of the row numbers
     * @throws WidgetException
     */
    void selectRows(int... rowNumbers) throws WidgetException;

    /**
     * Implementing this method would allow for selecting specific rows in a
     * table based on contents of the rows
     * 
     * @param items
     *            - a map containing key-value pairs, both of which are Strings,
     *            used to select the row that contains the same values
     * @throws WidgetException
     */
    void selectRows(List<Map<String, String>> items) throws WidgetException;

    /**
     * Implementing this method would allow for selecting specific rows in a
     * table based on contents of the row
     * 
     * @param items
     *            - a map containing key-value pairs, with the key as a String
     *            and the value as an Object, used to select the row that
     *            contains the same values
     * @throws WidgetException
     */
    void selectRowsElements(List<Map<String, Object>> items) throws WidgetException;

    /**
     * Implementing this method would allow for double clicking on a specific
     * row in a table based on the row number
     * 
     * @param rowNumber
     *            - the number indicating which row is to be double clicked
     * @throws WidgetException
     */
    void doubleClickRow(int rowNumber) throws WidgetException;

    /**
     * Implementing this method would allow for double clicking on a specific
     * cell in a table based on the row and column numbers
     * 
     * @param rowNumber
     *            - the number indicating which row is to be double clicked
     * @throws WidgetException
     */
    void doubleClickRow(int rowNumber, int columnNumber) throws WidgetException;

    /**
     * Implementing this method would allow for double clicking on a specific
     * row in a table based on contents of the row
     * 
     * @param item
     *            - a map containing key-value pairs, both of which are Strings,
     *            used to select the row that contains the same values
     * @throws WidgetException
     */
    void doubleClickRow(Map<String, String> item) throws WidgetException;

    /**
     * Implementing this method would allow for double clicking a specific row
     * in a table based on contents of the row
     * 
     * @param item
     *            - a map containing key-value pairs, with the key as a String
     *            and the value as an Object, used to select the row that
     *            contains the same values
     * @throws WidgetException
     */
    void doubleClickRowElement(Map<String, Object> item) throws WidgetException;

    /**
     * Implementing this method would allow for double clicking on a specific
     * cell in a table based on contents of the row
     * 
     * @param item
     *            - a map containing key-value pairs, both of which are Strings,
     *            used to select the row that contains the same values
     * @throws WidgetException
     */
    void doubleClickRow(Map<String, String> item, int columnNumber) throws WidgetException;

    /**
     * Implementing this method would allow for double clicking a specific cell
     * in a table based on contents of the row
     * 
     * @param item
     *            - a map containing key-value pairs, with the key as a String
     *            and the value as an Object, used to select the row that
     *            contains the same values
     * @throws WidgetException
     */
    public void doubleClickRowElement(Map<String, Object> item, int columnNumber)
        throws WidgetException;

    /**
     * Implementing this method would allow for getting all of the values of all
     * the selected rows as a list of String arrays
     * 
     * @return a List containing the selected rows values which are contained in
     *         a String array
     * @throws WidgetException
     */
    List<String[]> getSelectedRowsInArray() throws WidgetException;

    /**
     * Implementing this method would allow for getting all of the elements of
     * all the selected rows as a list of Object arrays
     * 
     * @return a List containing the selected rows values which are contained in
     *         a Object array
     * @throws WidgetException
     */
    List<Object[]> getSelectedRowsElementsInArray() throws WidgetException;

    /**
     * Implementing this method would allow for getting all of the key-value
     * pairs of all the selected rows
     * 
     * @return a List containing the selected rows key-values which are
     *         contained in a string-string Map
     * @throws WidgetException
     */
    List<Map<String, String>> getSelectedRowsInMap() throws WidgetException;

    /**
     * Implementing this method would allow for getting all of the key-value
     * pairs of all the selected rows
     * 
     * @return a List containing the selected rows key-values which are
     *         contained in a string-string Map
     * @throws WidgetException
     */
    List<Map<String, Object>> getSelectedRowsElementsInMap() throws WidgetException;
}
