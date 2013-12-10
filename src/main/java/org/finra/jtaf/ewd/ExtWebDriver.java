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

package org.finra.jtaf.ewd;

import java.util.List;
import java.util.Map;

import org.finra.jtaf.ewd.widget.IElement;
import org.openqa.selenium.WebDriver;

/**
 * ExtWebDriver provides extensions to the WebDriver API to support the widget
 * framework and permit changes to the implementing configuration
 * 
 */
public interface ExtWebDriver extends WebDriver {

	/**
	 * StaleWindowIdListException will be thrown when the stored window ids
	 * cannot be usefully compared with the current window ids
	 * 
	 * @see ExtWebDriver#storeCurrentWindowIds()
	 */
	static class StaleWindowIdListException extends Exception {
		/**
		 * generated SerialVersionID
		 */
		private static final long serialVersionUID = 9151151542707242145L;
		/**
		 * 
		 * the window ids retrieved when the exception occurred
		 */
		private final List<String> currentWindowIds;
		/**
		 * 
		 * the window ids stored when the exception occurred
		 */
		private final List<String> storedWindowIds;

		/**
		 * @param message
		 *            an explanation of the exception
		 * @param cause
		 *            the exception which was caught, prompting the current
		 *            exception
		 * @param storedWindowIds
		 *            the window ids stored when the exception occurred
		 * @param currentWindowIds
		 *            the window ids retrieved when the exception occurred
		 */
		public StaleWindowIdListException(String message, Throwable cause,
				List<String> storedWindowIds, List<String> currentWindowIds) {
			super(message, cause);
			this.storedWindowIds = storedWindowIds;
			this.currentWindowIds = currentWindowIds;
		}

		/**
		 * @param message
		 *            an explanation of the exception
		 * @param storedWindowIds
		 *            the window ids stored when the exception occurred
		 * @param currentWindowIds
		 *            the window ids retrieved when the exception occurred
		 */
		public StaleWindowIdListException(String message,
				List<String> storedWindowIds, List<String> currentWindowIds) {
			super(message);
			this.storedWindowIds = storedWindowIds;
			this.currentWindowIds = currentWindowIds;
		}

		/**
		 * @return the window ids stored when the exception occurred
		 */
		public List<String> getStoredWindowIds() {
			return this.storedWindowIds;
		}

		/**
		 * 
		 * @return the window ids retrieved when the exception occurred
		 */
		public List<String> getCurrentWindowIds() {
			return this.currentWindowIds;
		}

	}

	/**
	 * Replaces the underlying WebDriver instance
	 * 
	 * @param wd
	 *            the new WebDriver instance
	 */
	public void setWrappedDriver(WebDriver wd);

	/**
	 * Configures the use of JavaScript for entry of input fields, rather than
	 * relying on the native WebDriver methods
	 * 
	 * @param useJavaScript
	 *            true to enable the use of JavaScript, default is false
	 */
	public void setTypeMode(boolean useJavaScript);

	/**
	 * Indicates the current setting for entry of text in input fields
	 * 
	 * @return true if JavaScript is currently being used rather than native
	 *         WebDriver methods
	 */
	public boolean isJavascriptTypeMode();

	/**
	 * Configures the use of JavaScript for clicking on interactive elements,
	 * rather than relying on the native WebDriver methods
	 * 
	 * @param useJavaScript
	 *            true to enable the use of JavaScript, default is false
	 */
	public void setClickMode(boolean useJavaScript);

	/**
	 * Indicates the current setting for clicking on interactive elements
	 * 
	 * @return true if JavaScript is currently being used rather than native
	 *         WebDriver methods
	 */
	public boolean isJavascriptClickMode();

	/**
	 * One of several options to determine if the focus method should actually
	 * focus
	 * 
	 * @param doFocus
	 *            true to force focus to the window as part of the click action
	 * 
	 * @see #focus()
	 */
	public void setFocusOnClick(boolean doFocus);

	/**
	 * Indicates the current setting for setting window focus as part of a click
	 * action
	 * 
	 * @return true if focus will be set as part of click actions
	 */
	public boolean isFocusOnClick();

	/**
	 * Sets the maximum timeout value for actions
	 * 
	 * @param timeout
	 *            String representation milliseconds
	 */
	public void setMaxRequestTimeout(String timeout);

	/**
	 * Opens the browser
	 * 
	 * @param url
	 *            the web address for initial navigation
	 */
	public void open(String url);

	/**
	 * Simulates browser Back button
	 */
	public void back();

	/**
	 * Closes the browser window, but does not kill IEDriverServer.exe, use this
	 * if you changed browsers mid-session and want to close the window.
	 * 
	 * if you are using IEDriverServer.exe to run your tests you must use
	 * close() if you are done with the WebDriver session or execution will
	 * hang.
	 * 
	 * @see org.openqa.selenium.WebDriver#close()
	 */
	public void closeCurrentBrowser();

	/**
	 * Simulates browser Forward button
	 */
	public void forward();

	/**
	 * Reloads the current URL
	 * 
	 */
	public void refresh();

	/**
	 * Stores handles for all existing windows for later comparison
	 * 
	 * @see #selectPopupWindow()
	 */
	public void storeCurrentWindowIds();

	/**
	 * Request focus for the active window with the current WebDriver
	 */
	public void focus();

	/**
	 * Sets the active window for the current WebDriver
	 * 
	 * @param windowId the window id string 
	 * 
	 */
	public void selectWindow(String windowId);

	/**
	 * Sets the most recently opened browser window as the active windwo for the
	 * current WebDriver. Assumes that
	 * {@link ExtWebDriver#storeCurrentWindowIds()} was called before the window
	 * opened and that only one window has opened since.
	 * 
	 * @return the Id of the newly opened window
	 * @throws StaleWindowIdListException
	 *             when the difference between the stored window ids and the
	 *             current window ids is more than one window
	 */
	public String selectPopupWindow() throws StaleWindowIdListException;

	/**
	 * Retrieves the windowHandle IDs
	 * 
	 * @return array of current window ids
	 * 
	 */
	public String[] getAllWindowIds();

	/**
	 * Returns the current window handle ID
	 * 
	 * @return the window Id for the active window for the webdriver
	 */
	public String getWindowId();

	/**
	 * Maximizes the current browser window
	 */
	public void currentWindowMaximize();

	/**
	 * Scrolls the current browser window to specific position
	 * 
	 * @param i
	 *            Horizontal position
	 * @param j
	 *            Vertical position
	 */

	public void windowScroll(int i, int j);

	/**
	 * Scrolls the current browser by specific amount
	 * 
	 * @param i
	 *            Horizontal scroll amount
	 * @param j
	 *            Vertical scroll amount
	 */
	public void windowScrollBy(int i, int j);

	/**
	 * Executes JavaScript
	 * 
	 * @param javaScript the script to execute
	 */
	public void eval(String javaScript);

	/**
	 * Returns the original HTML source of the current frame
	 * 
	 * @return original HTML source of the current frame
	 */
	public String getHtmlSource();

	/**
	 * Retrieves current state of the DOM in HTML format for each of the
	 * document's frames.
	 * 
	 * @return A map of HTMLs. Each key describes the location of the FRAME
	 *         relative to the root DOM. The value is the HTML associated with
	 *         the key.
	 * 
	 *         A key example is FRAME[root-2-1], which identifies the IFRAME
	 *         that is the 1st IFRAME found underneath the 2nd IFRAME underneath
	 *         the root DOM.
	 */
	public Map<String, String> getGeneratedHtmlSource();

    /**
	 * Selects a new frame from within the current frame
	 * 
	 * @param iframeElement
	 *            the object representing the iframe element to select
	 */
	public void selectFrame(IElement iframeElement) throws Exception;

	/**
	 * Selects the top most frame of the current window
	 */
	public void unselectFrame();

	/**
	 * Evaluates an XPath in JavaScript to return a string value based on the
	 * resulting node(s) or XPath function results
	 * 
	 * @param XPath
	 *            the XPath to evaluate
	 * @return the string value representing the XPath
	 * @throws Exception
	 *             if the evaluation of the XPath fails
	 */
	public String evaluateXpath(String XPath) throws Exception;

	/**
	 * Clicks ok on the open alert dialog OR confirmation dialog attached to the
	 * current window
	 * 
	 * @throws Exception
	 *             if the native dialog does not exist
	 * 
	 */
	public void confirmNativeDialog() throws Exception;

	/**
	 * Clicks cancel on the open confirmation dialog attached to the current
	 * window
	 */
	public void cancelNativeDialog();

	/**
	 * Retrieves the text of the open native dialog attached to the current
	 * window
	 * 
	 * @return the dialog text
	 */
	public String getNativeDialogText();

	/**
	 * Retrieves the name of the browser currently being operated by the
	 * webdriver
	 * 
	 * @return the browser name
	 */
	public String getBrowserName();

	/**
	 * Retrieves the version of the browser currently being operated by the
	 * webdriver
	 * 
	 * @return the browser version
	 */
	public String getBrowserVersion();

	/**
	 * Gets the timeout used by default when waiting on actions
	 * 
	 * @return the default max request timeout
	 */
	public long getMaxRequestTimeout();

	/**
	 * Gets the underlying WebDriver instance
	 * 
	 * @return the current WebDriver
	 * 
	 */
	public WebDriver getWrappedDriver();

	/**
	 * Returns the count of nodes that would be returned by an XPath expression
	 * 
	 * @param XPath
	 *            the XPAth expression to count
	 * @return the number of nodes which match
	 */
	public int getXpathCount(String XPath);

	/**
	 * Sets the current session to be used for all actions on ExtWebDdriver and
	 * Widgets
	 * 
	 * @param id
	 *            the identifier for the session
	 */
	public void setSessionId(String id);

	/**
	 * Gets the id of the current session
	 * 
	 * @return the identifier for the session
	 */
	public String getSessionId();

	/**
	 * Selects the previously selected frame
	 */
	public void selectLastFrame();

}