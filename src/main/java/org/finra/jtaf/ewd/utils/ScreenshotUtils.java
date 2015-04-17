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

package org.finra.jtaf.ewd.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.finra.jtaf.ewd.session.SessionManager;
import org.finra.jtaf.ewd.timer.WidgetTimeoutException;
import org.finra.jtaf.ewd.widget.IElement;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

/***
 * Utilities to take and compare screenshots of elements
 * 
 * @author Pulitand
 *
 */
public class ScreenshotUtils {

	private static Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);
	
	/***
	 * You can use this method to take your control pictures. Note that the file should be a png.
	 * 
	 * @param element
	 * @param toSaveAs
	 * @throws IOException 
	 * @throws WidgetException 
	 * @throws WidgetTimeoutException 
	 * @throws Exception
	 */
	public static void takeScreenshotOfElement(IElement element, File toSaveAs) throws IOException, WidgetException {
		
		for(int i = 0; i < 10; i++) { //Loop up to 10x to ensure a clean screenshot was taken
			log.info("Taking screen shot of locator " + element.getByLocator() + " ... attempt #" + (i+1));
			
			//Scroll to element
			element.scrollTo();
			
			//Take picture of the page
			WebDriver wd = SessionManager.getInstance().getCurrentSession().getWrappedDriver();
			File screenshot;
			boolean isRemote = false;
			if(!(wd instanceof RemoteWebDriver)) {
				screenshot = ((TakesScreenshot) wd).getScreenshotAs(OutputType.FILE);		
			}
			else {
				Augmenter augmenter = new Augmenter();
				screenshot = ((TakesScreenshot) augmenter.augment(wd)).getScreenshotAs(OutputType.FILE);
				isRemote = true;
			}
			BufferedImage fullImage = ImageIO.read(screenshot);
			WebElement ele = element.getWebElement();
			
			//Parse out the picture of the element
			Point point = ele.getLocation();
			int eleWidth = ele.getSize().getWidth();
			int eleHeight = ele.getSize().getHeight();
			int x; 
			int y;
			if(isRemote) {
				x = ((Locatable)ele).getCoordinates().inViewPort().getX();
				y = ((Locatable)ele).getCoordinates().inViewPort().getY();
			}
			else {
				x = point.getX();
				y = point.getY();
			}
			log.debug("Screenshot coordinates x: "+x+", y: "+y);
			BufferedImage eleScreenshot = fullImage.getSubimage(x, y, eleWidth, eleHeight);	
			ImageIO.write(eleScreenshot, "png", screenshot);
			
			//Ensure clean snapshot (sometimes WebDriver takes bad pictures and they turn out all black)
			if(!isBlack(ImageIO.read(screenshot))) {
				FileUtils.copyFile(screenshot, toSaveAs);
				break;
			}
		}
	}
	
	/***
	 * Prereq: The page on which you are taking the screenshot is fully loaded
	 * 
	 * Take a screenshot of the element identified by element and save the file as toSaveAs
	 * (Note that this file should be saved as a png).
	 * Test that the control picture, controlPicture, is both the same size as,
	 * and, has a similarity value greater than or equal to the threshold.
	 * 
	 * @param element - the element to be tested
	 * @param controlPicture - the file of the picture that will serve as the control
	 * @param toSaveAs - for example, save the file at "testData/textFieldWidget/screenshot.png"
	 * @param threshold - you are asserting that the similarity between the two pictures
	 * is a double greater than or equal to this double (between 0.0 and 1.0)
	 * @throws Exception 
	 */
	public static boolean isSimilarToScreenshot(IElement element, File controlPicture, File
			toSaveAs, double threshold) throws IOException, WidgetException {
		
		takeScreenshotOfElement(element, toSaveAs);
		
		log.info("Screenshot was successful. Comparing against control...");

		BufferedImage var = ImageIO.read(toSaveAs);
		BufferedImage cont = ImageIO.read(controlPicture);
		
		return isSimilar(var, cont, threshold);
	}
	
	/***
	 * Prereq: The page on which you are taking the screenshot is fully loaded
	 *
	 * Take a screenshot of the element identified by element and save the file as toSaveAs
	 * (Note that this file should be saved as a png).
	 * Test that the control picture, controlPicture, is both the same size as,
	 * and, has a similarity value greater than or equal to the default threshold of .85.
	 * 
	 * @param element - the element to be tested
	 * @param controlPicture - the file of the picture that will serve as the control
	 * @param toSaveAs - for example, save the file at "testData/textFieldWidget/screenshot.png"
	 * @throws WidgetException 
	 * @throws IOException 
	 * @throws Exception 
	 */
	public static boolean isSimilarToScreenshot(IElement element, File controlPicture, 
			File toSaveAs) throws IOException, WidgetException {
		return isSimilarToScreenshot(element, controlPicture, toSaveAs, .85);
	}
	
	private static boolean isBlack(BufferedImage var) {
		double[] varArr = new double[var.getWidth()*var.getHeight()*3];
		//unroll pixels
		for(int i = 0; i < var.getHeight(); i++) {
			for(int j = 0; j < var.getWidth(); j++) {
				varArr[i*var.getWidth() + j + 0] = new Color(var.getRGB(j, i)).getRed();
				varArr[i*var.getWidth() + j + 1] = new Color(var.getRGB(j, i)).getGreen();
				varArr[i*var.getWidth() + j + 2] = new Color(var.getRGB(j, i)).getBlue();
			}
		}
		
		//test if all are black
		for(int i=0;i!=varArr.length;i++){
			if(varArr[i] != 0)
				return false;
		}
		return true;
	}
	
	private static boolean isSimilar(BufferedImage var, BufferedImage cont, double threshold) {
		return similarity(var, cont) >= threshold;
	}
	
	//Returns a double between 0 and 1.0
	private static double similarity(BufferedImage var, BufferedImage cont) {
		
		double[] varArr = new double[var.getWidth()*var.getHeight()*3];
		double[] contArr = new double[cont.getWidth()*cont.getHeight()*3];
		
		if (varArr.length!=contArr.length)
			throw new IllegalStateException("The pictures are different sizes!");
		
		//unroll pixels
		for(int i = 0; i < var.getHeight(); i++) {
			for(int j = 0; j < var.getWidth(); j++) {
				varArr[i*var.getWidth() + j + 0] = new Color(var.getRGB(j, i)).getRed();
				contArr[i*cont.getWidth() + j + 0] = new Color(cont.getRGB(j, i)).getRed();
				varArr[i*var.getWidth() + j + 1] = new Color(var.getRGB(j, i)).getGreen();
				contArr[i*cont.getWidth() + j + 1] = new Color(cont.getRGB(j, i)).getGreen();
				varArr[i*var.getWidth() + j + 2] = new Color(var.getRGB(j, i)).getBlue();
				contArr[i*cont.getWidth() + j + 2] = new Color(cont.getRGB(j, i)).getBlue();
			}
		}

		double mins=0;
		double maxs=0;
		for(int i=0;i!=varArr.length;i++){
			if (varArr[i]>contArr[i]){
				mins+=contArr[i];
				maxs+=varArr[i];
			} else {
				mins+=varArr[i];
				maxs+=contArr[i];
			}
		}
		return mins/maxs;
	}


}
