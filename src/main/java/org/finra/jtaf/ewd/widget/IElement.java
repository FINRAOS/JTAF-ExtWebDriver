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
package org.finra.jtaf.ewd.widget;

import org.finra.jtaf.ewd.timer.WidgetTimeoutException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * This is the base interface for all widgets
 * 
 */
public interface IElement {
    /**
     * Implementing this method would allow for obtaining the XPath, ID, name,
     * CSS Selector, class name, or tag name to the element
     * 
     * @return the locator to the element in the form of a String.
     * @deprecated getLocator is no longer reliable when IElements can be created from Bys
     */
    String getLocator();

    /**
     * Implementing this method would allow for obtaining the XPath, ID, name,
     * CSS Selector, class name, or tag name to the element as By-Object.
     * 
     * @return the locator to the element in the form of a {@link By}-Object.
     */
    By getByLocator();
    
    /**
     * Implementing this method would allow for checking if an element is
     * present or not
     * 
     * @return true if the element is present or false if it isn't
     * @throws WidgetException
     */
    boolean isElementPresent() throws WidgetException;

    /**
     * Implementing this method would allow for checking if an element is
     * present or not
     * 
     * @param time
     *            - the length of time this method will wait for this element to
     *            be present
     * @return true if the element is present or false if it isn't
     * @throws WidgetException
     */
    boolean isElementPresent(long time) throws WidgetException;

    /**
     * Implementing this method would allow for checking if an element is
     * present or not
     * 
     * @param useJavaXpath
     *            - if true, it attempts to find element using Java XPath API
     *            and if false it attempts to find the element using the
     *            WebDriver's API
     * 
     * @return true if the element is present or false if it isn't
     * @throws WidgetException
     */
    boolean isElementPresent(boolean useJavaXpath) throws WidgetException;

    /**
     * Implementing this method would allow for checking if an element is Not
     * present
     * 
     * @throws WidgetException
     * @return
     */
    boolean isElementNotPresent() throws WidgetException;

    /**
     * Implementing this method would allow for checking if an element is Not
     * present within [timeout] milliseconds
     * 
     * @param timeout
     *            Milliseconds
     * @throws WidgetException
     * @return
     */
    boolean isElementNotPresent(long timeout) throws WidgetException;

    /**
     * Implementing this method would allow for checking if an element is
     * visible or not
     * 
     * @return true if the element is visible or false if it isn't
     * @throws WidgetException
     */
    boolean isElementVisible() throws WidgetException;

    /**
     * Implementing this method would allow for checking if an element is
     * visible or not within a designated length of time
     * 
     * @param time
     *            - the length of time this method will wait for this element to
     *            be present
     * @return true if the element is visible or false if it isn't
     * @throws WidgetException
     */
    boolean isElementVisible(long time) throws WidgetException;

    /***
     * Implementing this method would allow for checking whether an element
     * is within the bounds of the window
     * @return
     * @throws WidgetException
     */
    boolean isWithinBoundsOfWindow() throws WidgetException;
    
    /**
     * Implementing this method would allow for the test to wait until the
     * element is present before progressing
     * 
     * @throws WidgetException
     */
    void waitForElementPresent() throws WidgetException, WidgetTimeoutException;

    /**
     * Implementing this method would allow for the test to wait a designated
     * length of time until the element is present before progressing
     * 
     * @param time
     *            - the length of time this method will wait for this element to
     *            be present
     * @throws WidgetException
     */
    void waitForElementPresent(long time) throws WidgetException, WidgetTimeoutException;

    /**
     * Implementing this method would allow for the test to wait until the
     * element is no longer present before progressing
     * 
     * @throws WidgetException
     */
    void waitForElementNotPresent() throws WidgetException, WidgetTimeoutException;

    /**
     * Implementing this method would allow for the test to wait a designated
     * length of time until the element is no longer present before progressing
     * 
     * @param time
     *            - the length of time this method will wait for this element to
     *            be present
     * @throws WidgetException
     */
    void waitForElementNotPresent(long time) throws WidgetException, WidgetTimeoutException;

    /**
     * Implementing this method would allow for the test to wait until the
     * element is visible before progressing
     * 
     * @throws WidgetException
     */
    void waitForVisible() throws WidgetException, WidgetTimeoutException;

    /**
     * Implementing this method would allow for the test to wait until a
     * designated length of time the element is visible before progressing
     * 
     * @param time
     *            - the length of time this method will wait for this element to
     *            be visible
     * @throws WidgetException
     */
    void waitForVisible(long time) throws WidgetException;

    /**
     * Implementing this method would allow for the test to wait until the
     * element is no longer visible before progressing
     * 
     * @throws WidgetException
     */
    void waitForNotVisible() throws WidgetException, WidgetTimeoutException;

    /**
     * Implementing this method would allow for the test to wait a designated
     * length of time until the element is no longer visible before progressing
     * 
     * @param time
     *            - the length of time this method will wait for this element to
     *            be not visible
     * @throws WidgetException
     */
    void waitForNotVisible(long time) throws WidgetException;

    /**
     * Implementing this method would allow for obtaining the text of the
     * Element
     * 
     * @return the text of the element as a String
     * @throws WidgetException
     */
    String getText() throws WidgetException;

    /**
     * Implementing this method would allow for checking if the element has a
     * specific attribute
     * 
     * @param attributeName
     *            - the attribute that is being checked for
     * @return true if the element has this attribute and false if it does not
     * @throws WidgetException
     */
    boolean isAttributePresent(String attributeName) throws WidgetException;

    /**
     * Implementing this method would allow for getting an attribute of an
     * element
     * 
     * @param attributeName
     *            - the attribute that is being grabbed
     * @return the attribute's value
     * @throws WidgetException
     */
    String getAttribute(String attributeName) throws WidgetException;

    /**
     * Implementing this method would allow for keeping the test from
     * progressing until an element's attribute has the desired value
     * 
     * @param attributeName
     *            - the attribute that is having it's value waited on
     * @param attributeValue
     *            - the value that the test waits for the attribute to have
     * @throws WidgetException
     */
    void waitForAttributeEqualTo(String attributeName, String attributeValue)
            throws WidgetException, WidgetTimeoutException;

    /**
     * Implementing this method would allow for keeping the test from
     * progressing until an element's attribute has the desired value by the
     * specified amount of time
     * 
     * @param attributeName
     *            - the attribute that is having it's value waited on
     * @param attributeValue
     *            - the value that the test waits for the attribute to have
     * @param time
     *            - the length of time this method will wait for the attribute
     *            to be equal to the desired value
     * 
     * @throws WidgetException
     */
    void waitForAttributeEqualTo(String attributeName, String attributeValue, long time)
            throws WidgetException, WidgetTimeoutException;

    /**
     * Implementing this method would allow for keeping the test from
     * progressing until an element's attribute no longer has the desired value
     * 
     * @param attributeName
     *            - the attribute that is having it's value waited on
     * @param attributeValue
     *            - the value that the test waits for the attribute to no longer
     *            have
     * @throws WidgetException
     */
    void waitForAttributeNotEqualTo(String attributeName, String attributeValue)
            throws WidgetException, WidgetTimeoutException;

    /**
     * Implementing this method would allow for keeping the test from
     * progressing until an element's attribute no longer has the desired value
     * by the specified amount of time
     * 
     * @param attributeName
     *            - the attribute that is having it's value waited on
     * @param attributeValue
     *            - the value that the test waits for the attribute to no longer
     *            have
     * @param time
     *            - the length of time this method will wait for the attribute
     *            to be no longer have to the desired value
     * @throws WidgetException
     */
    void waitForAttributeNotEqualTo(String attributeName, String attributeValue, long time)
            throws WidgetException, WidgetTimeoutException;

    /**
     * Waits for specific attribute value to match specific pattern within
     * period of specified timeout
     * 
     * @param attributeName
     *            Name of the attribute
     * @param pattern
     *            String pattern to match
     * @param timeout
     *            Milliseconds
     * @throws WidgetException
     */
    void waitForAttribute(String attributeName, String pattern, long timeout)
            throws WidgetException;

    /**
     * Waits for specific attribute value o match specific pattern within period
     * of maxRequestTimeout
     * 
     * @param attributeName
     *            Name of the attribute
     * @param pattern
     *            String pattern to match
     * @throws WidgetException
     */
    void waitForAttribute(String attributeName, String pattern) throws WidgetException;

    /**
     * Waits for specific attribute value not match specific pattern within
     * period of specified timeout
     * 
     * @param attributeName
     *            Name of the attribute
     * @param pattern
     *            String pattern to match
     * @param timeout
     *            Milliseconds
     * @throws WidgetException
     */
    void waitForNotAttribute(String attributeName, String pattern, long timeout)
            throws WidgetException;

    /**
     * Waits for specific attribute value to not match specific pattern within
     * period of maxRequestTimeout
     * 
     * @param attributeName
     *            Name of the attribute
     * @param pattern
     *            String pattern to match
     * @throws WidgetException
     */
    void waitForNotAttribute(String attributeName, String pattern) throws WidgetException;

    /**
     * Implementing this method would allow for keeping the test from
     * progressing until an element has text
     * 
     * @throws WidgetException
     */
    void waitForText() throws WidgetException, WidgetTimeoutException;

    /**
     * Implementing this method would allow for keeping the test from
     * progressing until an element has text
     * 
     * 
     * @param time
     *            -the length of time this method will wait for the text
     * 
     * @throws WidgetException
     * @throws WidgetTimeoutException
     */
    void waitForText(long time) throws WidgetException, WidgetTimeoutException;

    /**
     * Waits for element to be enabled within period of maxRequestTimeout
     * 
     * @throws WidgetException
     */
    void waitForEnabled() throws WidgetException;

    /**
     * Waits for element to be enabled within period of specified time
     * 
     * @param time
     *            Milliseconds
     * 
     * @throws WidgetException
     */
    void waitForEnabled(long time) throws WidgetException;

    /**
     * Implementing this method would allow for obtaining the X-coordinate of
     * the element
     * 
     * @return the element's X-coordinate as an integer
     * 
     * @throws WidgetException
     */
    int getLocationX() throws WidgetException;

    /**
     * Implementing this method would allow for obtaining the Y-coordinate of
     * the element
     * 
     * @return the element's Y-coordinate as an integer
     * 
     * @throws WidgetException
     */
    int getLocationY() throws WidgetException;

    /**
     * Implementing this method would allow for checking to see if the element's
     * text contains a specific substring
     * 
     * @param text
     *            - the text that is being checked for
     * @return true if the text is present and false if it is not
     * @throws WidgetException
     */
    boolean hasText(String text) throws WidgetException;

    /**
     * Implementing this method would allow for getting the CSS value given the
     * properties name
     * 
     * @param propertyName
     *            - the property to be checked
     * @return the value of the property
     * @throws WidgetException
     */
    String getCssValue(String propertyName) throws WidgetException;

    /**
     * Implementing this method would allow for highlighting this element
     * 
     * @throws WidgetException
     */
    void highlight() throws WidgetException;

    /**
     * Implementing this method would allow for highlighting this element a
     * specific color
     * 
     * @param color
     *            - the color to be used when highlighting the element
     * @throws WidgetException
     */
    void highlight(String color) throws WidgetException;

    /**
     * Implementing this method would allow for getting this element as a
     * WebElement
     * 
     * @return this element as a WebElement
     * @throws WidgetException
     */
    WebElement getWebElement() throws WidgetException;

    /**
     * Returns true if the element is enabled.
     * 
     * @return true if this element is enabled and false if it is not
     */
    boolean isEnabled() throws WidgetException;

    /**
     * Implementing this method would allow for forcing an element to fire a
     * specific event
     * 
     * @param event
     *            - the event that this element should fire
     * @throws WidgetException
     */
    void fireEvent(String event) throws WidgetException;

    /**
     * Implementing this method would allow for getting the text of all of this
     * element's child nodes
     * 
     * @return an array containing the text of all the child nodes
     * @throws WidgetException
     */
    String[] getChildNodesValuesText() throws WidgetException;

    /**
     * Returns inner HTML of the element
     * 
     * @return the inner HTML as a string
     * @throws WidgetException
     */
    String getInnerHTML() throws WidgetException;

    /**
     * Implementing this method would allow to execute a javascript on the
     * element
     * 
     * @param javascript
     *            the javascript code you want to execute
     * @throws WidgetException
     */
    void eval(String javascript) throws WidgetException;
    
    /**
     * Implementing this method would allow for the element to be scrolled to on the page
     * 
     * @throws WidgetException
     */
    void scrollTo() throws WidgetException;
    
    /**
     * Implementing this method would allow for the element to be brought into focus
     * 
     * @throws WidgetException
     */
    void focusOn() throws WidgetException;

}
