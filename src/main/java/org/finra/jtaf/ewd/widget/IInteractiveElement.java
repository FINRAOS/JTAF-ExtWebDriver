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

/**
 * Base interface for all elements that user can interact with (click, type,
 * check, etc)
 * 
 */
public interface IInteractiveElement extends IReadableElement {
    /**
     * Implementing this method would allow for clicking on the interactive
     * element
     * 
     * @throws WidgetException
     */
    public void click() throws WidgetException;

    /**
     * Implementing this method would allow for double clicking on the
     * interactive element
     * 
     * @throws WidgetException
     */
    public void doubleClick() throws WidgetException;

    /**
     * Implementing this method would allow for right clicking on the
     * interactive element
     * 
     * @throws WidgetException
     */
    public void rightClick() throws WidgetException;

    /**
     * Implementing this method would allow for checking if the interactive
     * element is enabled
     * 
     * @return true if the element is enabled and false if it isn't
     * @throws WidgetException
     */
    public boolean isEnabled() throws WidgetException;

    /**
     * Implementing this method would allow for drag and dropping a specific
     * element
     * 
     * @param element
     *            - the element that is to be drag and dropped
     * @throws WidgetException
     */
    public void dragAndDrop(IElement element) throws WidgetException;

    /**
     * Implementing this method would allow for drag and dropping by a specific
     * X/Y offset
     * 
     * @param element
     *            - the element that is to be drag and dropped
     * @throws WidgetException
     */
    public void dragAndDropByOffset(int xOffset, int yOffset) throws WidgetException;

    /**
     * Implementing this method would allow for 'pushing' specific key is down
     * 
     * @param theKey
     *            - the key to be 'pushed' down
     * @throws WidgetException
     */
    public void keyDown(Keys theKey) throws WidgetException;

    /**
     * Implementing this method would allow for releasing specific key
     * 
     * @param theKey
     *            - the key to be released
     * @throws WidgetException
     */
    public void keyUp(Keys theKey) throws WidgetException;

    /**
     * Implementing this method would allow for clicking on an element and
     * remaining that way
     * 
     * @throws WidgetException
     */
    public void clickAndHold() throws WidgetException;

    /**
     * Implementing this method would allow for releasing the element that is
     * being held
     * 
     * @throws WidgetException
     */
    public void releaseClickAndHold() throws WidgetException;

    /**
     * Implementing this method would allow for sending keys to an element like
     * the down arrow to a textfield to select a value from the type-ahead or
     * past input
     * 
     * @param keysToSend
     *            - the keys to be sent to the interactive element
     * @throws WidgetException
     */
    public void sendKeys(CharSequence keysToSend) throws WidgetException;

    /**
     * Implementing this method would allow for typing a value to the said
     * target
     * 
     * @param text
     *            - the text you want typed onto the interactive element
     * @throws WidgetException
     */
    public void type(String text) throws WidgetException;

    /**
     * Implementing this method would allow for appending to an already typed
     * value
     * 
     * @param locator
     *            XPath, ID, name, CSS Selector, class name, or tag name
     * @param text
     *            Text to append
     */
    public void typeAppend(String text) throws WidgetException;

    /**
     * Implementing this method would allow for triggering the mouse move event
     * on this interactive element
     * 
     * @throws WidgetException
     */
    public void mouseMove() throws WidgetException;

    /**
     * Implementing this method would allow for moving the mouse to an area
     * outside of the element
     * 
     * @throws WidgetException
     */
    public void mouseMoveOut() throws WidgetException;

    /**
     * Implementing this method would allow for moving the mouse over the
     * element
     * 
     * @throws WidgetException
     */
    public void mouseOver() throws WidgetException;
}
