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

import java.util.Map;

/**
 * HighlightProvider is a simple interface meant for ExtWebDriver
 * implementations which support highlighting. Widget classes should check to
 * see if a session is an instance of this interface, and also the value of
 * isHighlight(), before performing highlighting-related operations.
 * 
 */
public interface HighlightProvider {

	/**
	 * Sets whether to highlight elements when performing actions
	 * 
	 * @param isHighlight
	 *            whether to allow highlight
	 */
	public void setIsHighlight(boolean isHighlight);
	
	public void setHighlightColors(Map<String,String> colors);

	public boolean isHighlight();

	/**
	 * Get the highlight color based on mode
	 * 
	 * @param mode
	 *            the mode for which to get color
	 * @return String the color for the mode
	 */
	public String getHighlightColor(String mode);

}