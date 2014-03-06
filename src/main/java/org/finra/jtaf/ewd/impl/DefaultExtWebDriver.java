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
package org.finra.jtaf.ewd.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.ccil.cowan.tagsoup.Parser;
import org.finra.jtaf.ewd.ExtWebDriver;
import org.finra.jtaf.ewd.HighlightProvider;
import org.finra.jtaf.ewd.TimeOutException;
import org.finra.jtaf.ewd.widget.IElement;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.element.Element;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.google.common.collect.Lists;

/**
 * DefaultExtWebDriver is the default implementation of the ExtWebDriver
 * interface to support more advanced window and frame handling on top of
 * WebDriver
 * 
 */
public class DefaultExtWebDriver implements ExtWebDriver, HighlightProvider {
	private static Logger logger = Logger.getLogger(ExtWebDriver.class
			.getPackage().getName());

	/**
	 * The default maximum time that waiting methods should wait
	 */
	private long maxRequestTimeout;

	/**
	 * The underlying WebDriver instance
	 */
	private WebDriver wd;

	/**
	 * The configuration properties for this instance
	 */
	private ClientProperties cp;

	/**
	 * State tracking whether to use JavaScript for typing actions
	 */
	private boolean useJavascriptType;

	/**
	 * State tracking whether to use JavaScript for click actions
	 */
	private boolean useJavascriptClick;

	/**
	 * State tracking whether to focus on the window when clicking
	 */
	private boolean doFocusOnClick;

	/**
	 * Whether highlighting is turned on
	 */
	private boolean isHighlight;

	/**
	 * The list of currentWindowIds when they were last stored
	 */
	private Set<String> currentWindowIds;

	/**
	 * Tracking the frame selection hierarchy
	 */
	private FrameNode lastSelectedFrame;

	/**
	 * current session identifier
	 */
	private String sessionId;

	/**
	 * 
	 */
	private Map<String, String> highlightColorMap;

	/**
	 * Constructor for ExtWebDriver
	 * 
	 */
	public DefaultExtWebDriver() {
		useJavascriptType = false;
		useJavascriptClick = false;
		doFocusOnClick = true;
		currentWindowIds = new HashSet<String>();
	}

	@Override
	public void setWrappedDriver(WebDriver wd) {
		this.wd = wd;
	}

	/**
	 * Sets the client configuration to use
	 * 
	 * @param cp
	 *            client configuration
	 */
	public void setClientProperties(ClientProperties cp) {
		this.cp = cp;
	}

	@Override
	public void setTypeMode(boolean useJavascript) {
		useJavascriptType = useJavascript;
	}

	@Override
	public boolean isJavascriptTypeMode() {
		return useJavascriptType;
	}

	@Override
	public void setClickMode(boolean useJavascript) {
		useJavascriptClick = useJavascript;
	}

	@Override
	public boolean isJavascriptClickMode() {
		return useJavascriptClick;
	}

	@Override
	public void setFocusOnClick(boolean doFocus) {
		doFocusOnClick = doFocus;
	}

	@Override
	public boolean isFocusOnClick() {
		return doFocusOnClick;
	}

	@Override
	public void setIsHighlight(boolean isHighlight) {
		this.isHighlight = isHighlight;
	}

	@Override
	public boolean isHighlight() {
		return isHighlight;
	}

	@Override
	public void setHighlightColors(Map<String, String> colorMap) {
		this.highlightColorMap = colorMap;

	}

	@Override
	public String getHighlightColor(String highlightMode) {
		return highlightColorMap.get("highlight." + highlightMode);
	}

	@Override
	public void open(String url) {
		wd.navigate().to(url);
	}

	@Override
	public void back() {
		wd.navigate().back();
	}

	@Override
	public void closeCurrentBrowser() {
		wd.close();
		lastSelectedFrame = null;
	}

	@Override
	public void close() {
		String browser = cp.getBrowser();
		// TODO: confirm that this is to compensate for the lack of unload
		// performed by the IEDriver and add other drivers which need this
		if (browser.equalsIgnoreCase("ie")
				|| browser.equalsIgnoreCase("iexplore")
				|| browser.equalsIgnoreCase("*iexplore")) {
			eval("window.onbeforeunload = function(e){};");
		}
		wd.quit();
		lastSelectedFrame = null;
	}

	@Override
	public void forward() {
		wd.navigate().forward();
	}

	@Override
	public void refresh() {
		wd.navigate().refresh();
	}

	@Override
	public void storeCurrentWindowIds() {
		currentWindowIds = wd.getWindowHandles();
	}

	// TODO: verify that focus is not needed on other browsers
	@Override
	public void focus() {
		if (cp.getDebugMode() == true
				&& (cp.getBrowser().equalsIgnoreCase("ie")
						|| cp.getBrowser().equalsIgnoreCase("*iexplore") || cp
						.getBrowser().equalsIgnoreCase("iexplore")))

			windowFocus();
		else if (doFocusOnClick)
			windowFocus();
	}

	/**
	 * Convenience method for {@link #focus()}
	 * 
	 */
	private void windowFocus() {
		this.eval("window.focus()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.finra.jtaf.ewd.ExtWebDriver#selectWindow(java.lang.String) TODO:
	 * use {@link org.finra.jtaf.ewd.timer.WaitForConditionTimer}
	 */
	@Override
	public void selectWindow(String windowId) {
		long endTime = System.currentTimeMillis() + maxRequestTimeout;

		Set<String> currentWindowHandles = wd.getWindowHandles();
		boolean found = false;
		while (!found && System.currentTimeMillis() < endTime) {
			if (currentWindowHandles.contains(windowId)) {
				found = true;
			} else {
				currentWindowHandles = wd.getWindowHandles();
			}
		}

		if (found)
			wd.switchTo().window(windowId);
		else
			throw new TimeOutException("Could not select " + windowId
					+ " within " + maxRequestTimeout + " milliseconds");
	}

	@Override
	public String selectPopupWindow() throws StaleWindowIdListException {
		if (currentWindowIds == null && currentWindowIds.size() > 0) {
			if (currentWindowIds == null) {
				throw new NullPointerException(
						"WebDriver returned a null set of WindowIds to storeCurrentWindowIds()");
			}
			throw new StaleWindowIdListException(
					"Must set current window IDs by caling storeCurrentWindowIds() before using this function",
					Lists.newArrayList(currentWindowIds), Lists.newArrayList(wd
							.getWindowHandles()));
		}
		String windowId = null;

		Set<String> windowIds = null;
		long endTime = System.currentTimeMillis() + maxRequestTimeout;
		boolean found = false;
		while (!found && System.currentTimeMillis() < endTime) {
			windowIds = wd.getWindowHandles();
			if (windowIds.size() == (currentWindowIds.size() + 1)) {
				windowIds.removeAll(currentWindowIds);
				if (windowIds.size() != 1) {
					throw new StaleWindowIdListException(
							"Invalid set of current window IDs",
							Lists.newArrayList(currentWindowIds),
							Lists.newArrayList(windowIds));
				}
				windowId = windowIds.iterator().next();
				selectWindow(windowId);
				found = true;
			}
		}

		if (!found) {
			throw new StaleWindowIdListException(
					"Must set current window IDs by caling storeCurrentWindowIds() before using this function",
					Lists.newArrayList(currentWindowIds), Lists
							.newArrayList(windowIds));
		}
		return windowId;
	}

	@Override
	public String[] getAllWindowIds() {
		Object[] windowIds = wd.getWindowHandles().toArray();
		String[] windowIdStringArray = new String[windowIds.length];
		int index = 0;
		for (Object o : windowIds) {
			windowIdStringArray[index] = (String) o;
			index++;
		}

		return windowIdStringArray;
	}

	/**
	 * Returns the current window handle ID
	 * 
	 * @return the window handle ID
	 */
	@Override
	public String getWindowId() {
		return wd.getWindowHandle();
	}

	/**
	 * Maximizes the browser window
	 */
	@Override
	public void currentWindowMaximize() {
		wd.manage().window().maximize();
	}

	/**
	 * Scrolls the current browser window to specific position
	 * 
	 * @param i
	 *            Horizontal position
	 * @param j
	 *            Vertical position
	 */
	@Override
	public void windowScroll(int i, int j) {
		eval("window.scroll(" + i + "," + j + ");");
	}

	/**
	 * Scrolls the current browser by specific amount
	 * 
	 * @param i
	 *            Horizontal scroll amount
	 * @param j
	 *            Vertical scroll amount
	 */
	@Override
	public void windowScrollBy(int i, int j) {
		eval("window.scrollBy(" + i + "," + j + ");");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.finra.jtaf.ewd.ExtWebDriver#eval(java.lang.String)
	 * 
	 * TODO: use {@link org.finra.jtaf.ewd.timer.WaitForConditionTimer}
	 */
	@Override
	public void eval(String javaScript) {
		try {
			// TODO: add configuration for JavaScript executor
			((JavascriptExecutor) wd).executeScript(javaScript);

			// TODO: catch specific exceptions. If wd is not a JavaScript
			// executor, don't bother waiting.
		} catch (Exception e) {
			long time = System.currentTimeMillis() + 2000;
			boolean success = false;
			while (!success && System.currentTimeMillis() < time) {
				try {
					((JavascriptExecutor) wd).executeScript(javaScript);
					success = true;
				} catch (Exception e2) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						// TODO: log
					}
					e = e2;
				}
			}

			if (!success) {
				// TODO: use specific exception type(s) for eval issues
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Executes JavaScript in the context of an element
	 * 
	 * @param javaScript
	 *            the script to execute
	 * @param element
	 *            the object on which to execute
	 * 
	 *            TODO: use
	 *            {@link org.finra.jtaf.ewd.timer.WaitForConditionTimer}
	 */
	private void eval(String javaScript, WebElement element) {
		try {
			// TODO: add configuration for JavaScript executor
			((JavascriptExecutor) wd).executeScript(javaScript);

			// TODO: catch specific exceptions. If wd is not a JavaScript
			// executor, don't bother waiting.
		} catch (Exception e) {
			long time = System.currentTimeMillis() + 2000;
			boolean success = false;
			while (!success && System.currentTimeMillis() < time) {
				try {
					((JavascriptExecutor) wd)
							.executeScript(javaScript, element);
					success = true;
				} catch (Exception e2) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						// TODO: log
					}
					e = e2;
				}
			}

			if (!success) {
				// TODO: use specific exception type(s) for eval issues
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public String getHtmlSource() {
		selectLastFrame();
		return wd.getPageSource();
	}

	@Override
	public Map<String, String> getGeneratedHtmlSource() {
		try {

			if (lastSelectedFrame != null) {
				wd.switchTo().defaultContent();
			}

			FrameNode rootNode = new FrameNode();
			return getGeneratedHtmlSourceRecursive(rootNode, "FRAME[root]");

		} catch (Exception e) {
			// TODO: log
			return new HashMap<String, String>();
		} finally {
            doSelectLastFrame();
		}
	}

	/**
	 * Gets the current html source from the dom for the every frame in the
	 * current window recursively
	 * 
	 * @param currentFrame
	 *            the current FrameNode
	 * @param currFrameId
	 *            the locator of the current frame
	 * @return a map of each frame locator to the generated source
	 * 
	 * @see #getGeneratedHtmlSource() TODO: check/enforce currFrameId format
	 */
	private Map<String, String> getGeneratedHtmlSourceRecursive(
			FrameNode currentFrame, String currFrameId) throws Exception {

		Map<String, String> frames = new HashMap<String, String>();

		if (!currentFrame.isRoot()) {
			wd.switchTo().defaultContent();

			Stack<FrameNode> framesToSelect = new Stack<FrameNode>();
			FrameNode currFrame = currentFrame;
			while (currFrame.getParent() != null) {
				framesToSelect.push(currFrame);
				currFrame = currFrame.getParent();
			}

			while (!framesToSelect.isEmpty()) {
				FrameNode fn = framesToSelect.pop();
				wd.switchTo().frame(fn.getFrame());
			}
		} else {
			wd.switchTo().defaultContent();
		}

		// Add current frame

		frames.put(currFrameId, wd.getPageSource());

		int iframeCount = wd.findElements(By.xpath("//iframe")).size();
		if (iframeCount == 0) {
			return frames;
		}
		for (int i = 1; i <= iframeCount; i++) {
			if (!currentFrame.isRoot()) {
				wd.switchTo().defaultContent();

				Stack<FrameNode> framesToSelect = new Stack<FrameNode>();
				FrameNode currFrame = currentFrame;
				while (currFrame.getParent() != null) {
					framesToSelect.push(currFrame);
					currFrame = currFrame.getParent();
				}

				while (!framesToSelect.isEmpty()) {
					FrameNode fn = framesToSelect.pop();
					wd.switchTo().frame(fn.getFrame());
				}
			} else {
				wd.switchTo().defaultContent();
			}

            FrameNode nextFrame = new FrameNode(currentFrame, "(//iframe)[" + i
					+ "]", this.getWrappedDriver().findElement(By.xpath("(//iframe)[" + i + "]")));
			String nextFrameId = currFrameId.substring(0,
					currFrameId.length() - 1) + "-" + i + "]";

			// Recursively add next frames
			frames.putAll(getGeneratedHtmlSourceRecursive(nextFrame,
					nextFrameId));
		}

		return frames;
	}

    @Override
	public void selectFrame(IElement element) throws Exception {
		try {
			WebElement frameElement = element.getWebElement();
			wd.switchTo().frame(frameElement);

			if (lastSelectedFrame != null) {
				FrameNode parentNode = lastSelectedFrame;
				lastSelectedFrame = new FrameNode(parentNode, element.getLocator(),
						frameElement);
			} else {
				FrameNode parentNode = new FrameNode();
				lastSelectedFrame = new FrameNode(parentNode, element.getLocator(),
						frameElement);
			}
			// TODO: look for a specific exception
		} catch (Exception e) {
			wd.switchTo().defaultContent();

			if (lastSelectedFrame == null) {
				WebElement frameElement = element.getWebElement();
				wd.switchTo().frame(frameElement);

				FrameNode parentNode = new FrameNode();
				lastSelectedFrame = new FrameNode(parentNode, element.getLocator(),
						frameElement);
			} else {
				Stack<FrameNode> framesToSelect = new Stack<FrameNode>();
				FrameNode currFrame = lastSelectedFrame;
				while (currFrame.getParent() != null) {
					framesToSelect.push(currFrame);
					currFrame = currFrame.getParent();
				}

				while (!framesToSelect.isEmpty()) {
					FrameNode fn = framesToSelect.pop();
					wd.switchTo().frame(fn.getFrame());
				}
			}
		}
	}

	/**
	 * Selects the top most frame
	 */
	@Override
	public void unselectFrame() {
		wd.switchTo().defaultContent();
		lastSelectedFrame = null;
	}

	@Override
	public void selectLastFrame() {
		if (!cp.shouldSelectLastFrame())
			return;

		doSelectLastFrame();
	}

    private void doSelectLastFrame() {
        try {
            if (lastSelectedFrame != null) {
                Stack<FrameNode> framesToSelect = new Stack<FrameNode>();
                FrameNode currFrame = lastSelectedFrame;
                while (currFrame.getParent() != null) {
                    framesToSelect.push(currFrame.getParent());
                    currFrame = currFrame.getParent();
                }

                while (!framesToSelect.isEmpty()) {
                    FrameNode fn = framesToSelect.pop();
                    if (!fn.isRoot()) {
                        wd.switchTo().frame(fn.getFrame());
                    } else {
                        wd.switchTo().defaultContent();
                    }
                }

                try {
                    wd.switchTo().frame(lastSelectedFrame.getFrame());
                    // TODO: look for a specific exception
                } catch (Exception e) {
                    // TODO: log
                }
            }
            // TODO: look for a specific exception
        } catch (Exception e) {
            logger.info("Unable to select the frame", e);
        }
    }

	@Override
	public String evaluateXpath(String xpath) throws Exception {
		XPathFactory xpathFac = XPathFactory.newInstance();
		XPath theXpath = xpathFac.newXPath();

		String html = getHtmlSource();
		html = html.replaceAll(">\\s+<", "><");
		InputStream input = new ByteArrayInputStream(html.getBytes());

		XMLReader reader = new Parser();
		reader.setFeature(Parser.namespacesFeature, false);
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();

		DOMResult result = new DOMResult();
		transformer.transform(new SAXSource(reader, new InputSource(input)),
				result);

		Node htmlNode = result.getNode(); // This code gets a Node from the
											// result.
		return (String) theXpath.evaluate(xpath, htmlNode,
				XPathConstants.STRING);
	}

	@Override
	public void confirmNativeDialog() throws Exception {
		Alert alert = wd.switchTo().alert();
		alert.accept();
	}

	@Override
	public void cancelNativeDialog() {
		Alert alert = wd.switchTo().alert();
		alert.dismiss();
	}

	@Override
	public String getNativeDialogText() {
		Alert alert = wd.switchTo().alert();
		return alert.getText();
	}

	// TODO: determine if there is a better option than returning null
	@Override
	public String getBrowserName() {
		if (wd != null) {
			Capabilities capabilities = ((HasCapabilities) wd)
					.getCapabilities();
			if (capabilities != null) {
				return capabilities.getBrowserName();
			}
			return null;
		}
		return null;
	}

	// TODO: determine if there is a better option than returning null
	@Override
	public String getBrowserVersion() {
		if (wd != null) {
			Capabilities capabilities = ((HasCapabilities) wd)
					.getCapabilities();
			if (capabilities != null) {
				return capabilities.getVersion();
			}
			return null;
		}
		return null;
	}

	/**
	 * 
	 * TreeNode with parent references only to represent the path for a specific
	 * child frame
	 * 
	 */
	private class FrameNode {

		private boolean isRoot;
		private final FrameNode parentNode;
		private final String iframeLocator;
		private final WebElement frame;

		/**
		 * Default constructor for FrameNode
		 * 
		 */
		FrameNode() {
			this.setRoot(true);
			this.parentNode = null;
			this.iframeLocator = null;
			this.frame = null;
		}

		/**
		 * Specific constructor for FrameNode
		 * 
		 * @param parentNode
		 *            The parent FrameNode if it exists
		 * @param iframeLocator
		 *            The locator of the frame element
		 * @param frame
		 *            the WebElement frame itself
		 */
		FrameNode(FrameNode parentNode, String iframeLocator, WebElement frame) {
			this.setRoot(false);
			this.parentNode = parentNode;
			this.iframeLocator = iframeLocator;
			this.frame = frame;
		}

		/**
		 * Gets parent FrameNode
		 * 
		 * @return parent FrameNode
		 */
		FrameNode getParent() {
			return parentNode;
		}

		/**
		 * Gets the frame
		 * 
		 * @return WebElement representing the frame
		 */
		WebElement getFrame() {
			return frame;
		}

		/**
		 * Gets the locator for the frame
		 *
		 * @return String representing the locator of the frame element
		 */
		String getFrameLocator() {
			return iframeLocator;
		}

		/**
		 * Is the FrameNode representing the root of the page
		 * 
		 * @return the FrameNode the root
		 */
		boolean isRoot() {
			return isRoot;
		}

		/**
		 * Sets the FrameNode as the root
		 * 
		 * @param isRoot
		 *            is the FrameNode the root
		 */
		void setRoot(boolean isRoot) {
			this.isRoot = isRoot;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof FrameNode
					&& ((FrameNode) o).getFrameLocator().equals(
                    this.getFrameLocator())) {
				return true;
			}
			return false;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((iframeLocator == null) ? 0 : iframeLocator.hashCode());
			result = prime * result
					+ ((parentNode == null) ? 0 : parentNode.hashCode());
			return result;
		}
	}
	
	@Override
	public WebElement findElement(By arg0) {
		return wd.findElement(arg0);
	}

	@Override
	public List<WebElement> findElements(By arg0) {
		return wd.findElements(arg0);
	}

	@Override
	public void get(String arg0) {
		wd.get(arg0);
	}

	@Override
	public String getCurrentUrl() {
		return wd.getCurrentUrl();
	}

	@Override
	public String getPageSource() {
		return wd.getPageSource();
	}

	@Override
	public String getTitle() {
		return wd.getTitle();
	}

	@Override
	public String getWindowHandle() {
		return wd.getWindowHandle();
	}

	@Override
	public Set<String> getWindowHandles() {
		return wd.getWindowHandles();
	}

	@Override
	public Options manage() {
		return wd.manage();
	}

	@Override
	public Navigation navigate() {
		return wd.navigate();
	}

	@Override
	public void quit() {
		wd.quit();
	}

	@Override
	public TargetLocator switchTo() {
		return wd.switchTo();
	}

	@Override
	public void setMaxRequestTimeout(String timeout) {
		this.maxRequestTimeout = Long.parseLong(timeout);
	}

	@Override
	public long getMaxRequestTimeout() {
		return maxRequestTimeout;
	}

	@Override
	public WebDriver getWrappedDriver() {
		return wd;
	}

	@Override
	public int getXpathCount(String string) {
		return wd.findElements(By.xpath(string)).size();
	}

	@Override
	public void setSessionId(String id) {
		this.sessionId = id;
	}

	@Override
	public String getSessionId() {
		return this.sessionId;
	}
}