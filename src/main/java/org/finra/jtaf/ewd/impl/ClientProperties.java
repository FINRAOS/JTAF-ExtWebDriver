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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;
import org.apache.log4j.Logger;

/**
 * Enables storage of and access to driver and browser configuration.
 * 
 */
public class ClientProperties {
    private final Logger logger = Logger.getLogger(ClientProperties.class.getPackage().getName());

    private URL client;

    private final PropertiesConfiguration config;
    private final PropertiesConfigurationLayout propertiesConfigurationLayout;

    private final String browser;
    private final String browserVersion;
    private final String proxy;
    private final String proxyHttps;

    private int browserInitPositionX = 0;
    private int browserInitPositionY = 0;

    private final String os;
    private final String osVersion;

    private final String maxRequestTimeoutString; // For backwards-compatibility
    private int maxRequestTimeout = 18000;
    private String maxPageWaitString;
    private int maxPageWait = 18000;
    private final String appearWaitTimeString;
    private int appearWaitTime;
    private final int maxDownloadWaitTime;
    private final String downloadFolder;
    private final String uploadFolder;

    private final String maxAllowedSessions;
    private final String binaryPath;
    private final String webDriverIEDriver;
    private final String webDriverChromeDriver;

    private boolean isHighlight;
    private final Map<String, String> highlightColorMap;
    
    private final String firefoxProfileFolder;
    private final String firefoxPropertiesFile;
    private final List<String> firefoxExtensions = new ArrayList<String>();

    private final String tempFolderNameContainsList;
    private int numberOfDaysToKeepTempFolders = 7;

    private final boolean debugMode;
    private final boolean doTaskKill;
    private final boolean selectLastFrame;

    // Selenium Grid or Sauce labs
    private final boolean useGrid;
    private final String gridUrl;
    private final String gridPlatform;
    private final String gridProperties;

    /**
     * Constructs a {@code ClientProperties} from the given file.
     * 
     * @param filePath
     *            the file to be loaded
     */
    public ClientProperties(String filePath) {
        URL clientPath = this.getClass().getClassLoader().getResource(filePath);
        this.config = new PropertiesConfiguration();
        this.config.setDelimiterParsingDisabled(true);
        try {
            client = clientPath;
            // Disable delimiting values (default is comma delimited)
            this.config.load(client);
        } catch (ConfigurationException e) {
            String message = "Client configuration could not be loaded from file: \""
                    + filePath + "\"";
            this.logger.error(message, e);
            throw new RuntimeException(message, e);
        }
        propertiesConfigurationLayout = config.getLayout();

        browser = load("browser", "htmlunit", "Browser name. See browsers supported by WebDriver.");
        browserVersion = load("browser.version", null, "Version of the browser (if applicable).");
        proxy = load("proxy", null, null);
        proxyHttps = load("proxy.https", null, null);

        String browserInitPositionXStr = load("browser.init.position.x", "0",
                "Horizontal position for moving browser to. Useful for debugging tests.");
        try {
            browserInitPositionX = Integer.parseInt(browserInitPositionXStr);
        } catch (NumberFormatException e) {
            logger.error("Error parsing '"
                    + browserInitPositionXStr
                    + "' (value of 'browser.init.position.x' property from client properties file) as integer. Please fix your test configuration.",e);
        }

        String browserInitPositionYStr = load("browser.init.position.y", "0",
                "Vertical position for moving browser to. Useful for debugging tests.");
        try {
            browserInitPositionY = Integer.parseInt(browserInitPositionYStr);
        } catch (NumberFormatException e) {
            logger.error("Error parsing '"
                    + browserInitPositionYStr
                    + "' (value of 'browser.init.position.y' property from client properties file) as integer. Please fix your test configuration.",e);
        }

        os = load("os", null, null);
        osVersion = load("os.version", null, null);
        maxPageWaitString = load("maxPageWait", "30000",
                "Standard maximum page wait timeout throughout your automation project (in milliseconds)");
        try {
            maxPageWait = Integer.parseInt(maxPageWaitString);
        } catch (NumberFormatException e) {
            logger.error("Error parsing '"
                    + maxPageWaitString
                    + "'",e);
        }

        appearWaitTimeString = load("appearWaitTime", "5000",
                "Maximum time for waiting of element appear (in milliseconds)");
        try {
            appearWaitTime = Integer.parseInt(appearWaitTimeString);
        } catch (NumberFormatException e) {
            logger.error("Error parsing '"
                    + appearWaitTimeString
                    + "'",e);
        }

        maxRequestTimeoutString = load("maxRequestTimeout", "30000",
                "Standard maximum request wait timeout throughout your automation project (in milliseconds)");
        try {
            maxRequestTimeout = Integer.parseInt(maxRequestTimeoutString);
        } catch (NumberFormatException e) {
            logger.error("Error parsing '"
                    + maxRequestTimeoutString
                    + "'",e);
        }

        maxDownloadWaitTime = Integer.parseInt(load("download.time", "30000",
                "Maximum download wait timeout"));
        downloadFolder = load("download.folder", null, "Default download folder");
        binaryPath = load(
                "binaryPath",
                null,
                "Path to Firefox executable (if you want to use specific version installed on your machine instead of default FF installation)");
        webDriverIEDriver = load("webdriver.ie.driver", null, "Path to IEDriverServer.exe");
        webDriverChromeDriver = load("webdriver.chrome.driver", null,
                "Path to chromedriver executable");

        String uploadFolderStr = load("upload.folder", null,
                "Default folder to grab files from to perform upload");
        if (uploadFolderStr != null && !uploadFolderStr.equals("")) {
            File temp = new File(uploadFolderStr);
            uploadFolder = temp.getAbsolutePath();
        } else {
            uploadFolder = ".";
        }
        firefoxProfileFolder = load("firefoxProfile.folder", null,
                "Path to custom Firefox profile (setup Firefox profile)");
        firefoxPropertiesFile = load(
                "firefoxProfile.file",
                null,
                "Properties file containing configuration you want to load to current Firefox profile (setup Firefox properties file)");

        // Check before 'webdriver.doTaskKill'
        String useGridStr = load("useGrid", "false",
                "Setting for running tests against Selenium Grid or Sauce Labs");
        useGrid = (useGridStr != null && useGridStr.equalsIgnoreCase("true"));

        // Check after 'useGrid'
        String taskCheck = load("webdriver.doTaskKill", "true",
                "Gracefully kill all the driver server processes at the beginning of execution");
        if (taskCheck != null) {
            if (taskCheck.equalsIgnoreCase("false") || taskCheck.equalsIgnoreCase("0")
                    || taskCheck.equalsIgnoreCase("no") || useGrid) {
                doTaskKill = false;
            } else if ((taskCheck.equalsIgnoreCase("true") || taskCheck.equalsIgnoreCase("1") || taskCheck
                    .equalsIgnoreCase("yes"))) {
                doTaskKill = true;
            } else {
                logger.fatal("Property 'doTaskKill' is not within range of accepted values. (Range of accepted values are '1'/'0', 'Yes'/'No' and 'True'/'False')");
                doTaskKill = true;
            }
        } else {
            // Default value
            doTaskKill = true;
        }

        String numberOfDaysToKeepTempFoldersStr = load(
                "numberOfDaysToKeepTempFolders",
                "7",
                "Specify the period of which you want to keep temporary WebDriver folders created in temp directory");
        try {
            numberOfDaysToKeepTempFolders = Integer.parseInt(numberOfDaysToKeepTempFoldersStr);
        } catch (Exception e) {
        }

        tempFolderNameContainsList = load("tempFolderNameContainsList", null,
                "Comma separated list of folders to clean with webDriver temp files");

        for (int i = 1; config.containsKey("firefoxProfile.extension." + Integer.toString(i)); i++) {
            String ext = config.getString("firefoxProfile.extension." + Integer.toString(i));
            firefoxExtensions.add(ext);
        }

        String highlight = load("highlight", "false", "Highlighting web elements during execution");
        if (highlight.equalsIgnoreCase("true") || highlight.equalsIgnoreCase("yes")
                || highlight.equalsIgnoreCase("1")) {
            isHighlight = true;
        } else if (highlight.equalsIgnoreCase("false") || highlight.equalsIgnoreCase("no")
                || highlight.equalsIgnoreCase("0")) {
            isHighlight = false;
        } else {
            logger.fatal("Error parsing client property 'highlight' ('" + highlight
                    + "'). It can be one of 'true / false', 'yes / no', '1 / 0'.");
        }

        highlightColorMap = new HashMap<String, String>();
        loadColorMapRgb();
        maxAllowedSessions = load("maxAllowedSessions", null, null);

        String debug = load("debugMode", "false",
                "Test debug mode. If it is on, highlight will be turned on by default");

        // If debug is on, then turn highlight on
        if (debug != null && debug.equalsIgnoreCase("true")) {
            debugMode = true;
            isHighlight = true;
        } else {
            debugMode = false;
        }

        String selectLastFrameStr = load("selectLastFrame", "true",
                "Feature to select last frame automatically");
        if (selectLastFrameStr != null && selectLastFrameStr.equalsIgnoreCase("false")) {
            selectLastFrame = false;
        } else {
            selectLastFrame = true;
        }

        gridUrl = load("grid.url",
                "http://username-string:access-key-string@ondemand.saucelabs.com:80/wd/hub",
                "Sauce labs URL (e.g. 'http://username-string:access-key-string@ondemand.saucelabs.com:80/wd/hub')");
        gridPlatform = load("grid.platform", "Windows 7",
                "Selenium Grid OS Platform name (e.g. 'Windows 7')");
        gridProperties = load("grid.properties", "record-screenshots=true",
                "Space separated Selenium Grid properties (e.g. 'record-screenshots=true')");
    }

    /**
     * Loads the given key/value pair into the configuration.
     * <p>
     * If the configuration already contains the given key, no change is made to
     * the configuration.
     * 
     * @param key
     *            the key to be put into the configuration
     * @param defaultValue
     *            the value to be put into the configuration; if {@code null},
     *            then no change is made to the configuration
     * @param comment
     *            a comment to be set for the key/value pair; {@code null}
     *            values permitted
     * @return the newly set value, or the current value if the configuration
     *         already contains the given key
     */
    private final String load(String key, String defaultValue, String comment) {
        if (config.getProperty(key) != null) {
            return config.getString(key);
        } else {
            if (defaultValue != null) {
                try {
                    config.addProperty(key, defaultValue);
                    if (comment != null) {
                        propertiesConfigurationLayout.setComment(key, comment);
                    } else {
                        propertiesConfigurationLayout
                                .setComment(
                                        key,
                                        "Automatically added default value. Please see Client Properties documentation on ExtWebDriver homepage.");
                    }
                    config.save(config.getPath());
                } catch (ConfigurationException e) {
                    logger.fatal("Error saving updated property file ('" + config.getPath() + "')"
                            + e);
                }
            }
            return defaultValue;
        }
    }
    
	/**
	 * load the color mode and rgb values from the client properties file as
	 * key/value pairs in the highlighColorMap
	 * 
	 */
    private final void loadColorMapRgb() {
        Iterator<String> colorKeys = config.getKeys("highlight");
        if(colorKeys!=null){
            while(colorKeys.hasNext()){
            	String current = colorKeys.next();
          	  String[] splits = current.split("\\.");
          	  if(splits.length > 1){
          		  String val = config.getString(current);
          		  if(val.startsWith("rgb"))
          			  highlightColorMap.put(splits[1].toUpperCase(), val);
          		  else
          			logger.warn("Please check property " +current+ ". The highlight color has to specify RGB values in this format: eg. highlight.find=rgb(255,255,0)");
          	  }     		 
          	  else if(splits[0].equals("highlight")){
        
          		continue;
          	  }
            }
        }

        
        	//default load
      		logger.warn("No RGB property for highlight was provided. Colors set to default.");
        	if(!highlightColorMap.containsKey("find"))		
            	highlightColorMap.put("find".toUpperCase(), load("highlight.find", "rgb(255, 255, 0)", "color for highlight element during finding"));
            if(!highlightColorMap.containsKey("get"))		
            	highlightColorMap.put("get".toUpperCase(), load("highlight.get", "rgb(135, 206, 250)", "color for highlight element during finding"));
            if(!highlightColorMap.containsKey("put"))		
            	highlightColorMap.put("put".toUpperCase(), load("highlight.put", "rgb(152, 251, 152)", "color for highlight element during finding"));
       
        
     
      }

      public String getHighlightColor(String colorMode){
      	return highlightColorMap.get( colorMode.toUpperCase());
      }
      
      public Map<String, String> getHighlightColorMap(){
      	return this.highlightColorMap;
      }
      
    /**
     * Returns the name of the browser.
     * 
     * @return the name of the browser
     */
    public final String getBrowser() {
        return browser;
    }

    /**
     * Returns the version of the browser.
     * 
     * @return the version of the browser
     */
    public final String getBrowserVersion() {
        return browserVersion;
    }

    /**
     * Returns the proxy.
     * 
     * @return the proxy
     */
    public final String getProxy() {
        return proxy;
    }

    /**
     * Returns the name of the operating system.
     * 
     * @return the name of the operating system
     */
    public final String getOS() {
        return os;
    }

    /**
     * Returns the version of the operating system.
     * 
     * @return the version of the operating system
     */
    public final String getOSVersion() {
        return osVersion;
    }

    /**
     * Returns the maximum wait time for downloads.
     * 
     * @return the maximum wait time for downloads
     */
    public final int getMaxDownloadWaitTime() {
        return maxDownloadWaitTime;
    }

    /**
     * Returns the directory for downloads.
     * 
     * @return the directory for downloads
     */
    public final String getDownloadFolder() {
        return downloadFolder;
    }

    /**
     * Returns the directory for uploads.
     * 
     * @return the directory for uploads
     */
    public final String getUploadFolder() {
        return uploadFolder;
    }

    /**
     * Returns the maximum timeout for requests as a {@code String}.
     * 
     * @return the maximum timeout for requests as a {@code String}
     */
    public final String getMaxRequestTimeoutString() {
        return maxRequestTimeoutString;
    }

    /**
     * Returns the maximum time out for requests as an {@code int}.
     * 
     * @return the maximum time out for requests as an {@code int}
     */
    public final int getMaxRequestTimeout() {
        return maxRequestTimeout;
    }

    /**
     * Returns the maximum wait time for pages.
     * 
     * @return the maximum wait time for pages
     */
    public int getMaxPageWait() {
        return maxPageWait;
    }

    /**
     * Returns the maximum wait time for elements to appear.
     * 
     * @return the maximum wait time for elements to appear
     */
    public int getAppearWaitTime() {
        return appearWaitTime;
    }

    /**
     * Returns the maximum allowed number of sessions.
     * 
     * @return the maximum allowed number of sessions
     */
    public String getMaxAllowedSessions() {
        return maxAllowedSessions;
    }

    /**
     * Returns the path to the Firefox executable binary.
     * 
     * @return the path to the Firefox executable binary
     */
    public String getBinaryPath() {
        return binaryPath;
    }

    /**
     * Returns the path to IEDriverServer.exe.
     * 
     * @return the path to IEDriverServer.exe
     */
    public String getWebDriverIEDriver() {
        return webDriverIEDriver;
    }

    /**
     * Returns the path to chromedriver.exe.
     * 
     * @return the path to chromedriver.exe
     */
    public String getWebDriverChromeDriver() {
        return webDriverChromeDriver;
    }

    /**
     * Returns the name of the browser.
     * 
     * @return the name of the browser
     */
    public String getFirefoxProfileFolder() {
        return firefoxProfileFolder;
    }

    /**
     * Returns the path to the Firefox profile.
     * 
     * @return the path to the Firefox profile
     */
    public String getFirefoxProfileFile() {
        return firefoxPropertiesFile;
    }

    /**
     * Returns whether elements will be highlighted during execution.
     * 
     * @return {@code true} if and only if elements will be highlighted during
     *         execution
     */
    public boolean isHighlight() {
        return isHighlight;
    }


    /**
     * Returns a {@code List} of Firefox extensions.
     * 
     * @return a {@code List} of Firefox extensions
     */
    public List<String> getFirefoxExtensions() {
        return firefoxExtensions;
    }

    /**
     * Returns the number of days temporary folders are kept.
     * 
     * @return the number of days temporary folders are kept
     */
    public int getNumberOfDaysToKeepTempFolders() {
        return numberOfDaysToKeepTempFolders;
    }

    /**
     * Returns the list of folder names to be cleaned with the temp folders.
     * 
     * @return the list of folder names to be cleaned with the temp folders
     */
    public String getTempFolderNameContainsList() {
        return tempFolderNameContainsList;
    }

    /**
     * Returns whether debug mode is enabled.
     * 
     * @return {@code true} if and only if debug mode is enabled
     */
    public boolean getDebugMode() {
        return debugMode;
    }

    /**
     * Returns whether running driver services are killed at the beginning of
     * execution.
     * 
     * @return {@code true} if and only if running driver services are killed at
     *         the beginning of execution
     */
    public boolean isKillTasksAtStartup() {
        return doTaskKill;
    }

    /**
     * Returns whther the last frame will be automatically selected.
     * 
     * @return {@code true} if and only if the last frame will be automatically
     *         selected
     */
    public boolean shouldSelectLastFrame() {
        return selectLastFrame;
    }

    /**
     * Returns whether Selenium Grid or Saucelabs will be used.
     * 
     * @return {@code true} if and only if Selenium Grid or Saucelabs will be
     *         used
     */
    public boolean isUseGrid() {
        return useGrid;
    }

    /**
     * Returns the Selenium Grid platform.
     * 
     * @return the Selenium Grid platform
     */
    public String getGridPlatform() {
        return gridPlatform;
    }

    /**
     * Returns the Selenium Grid properties.
     * 
     * @return the Selenium Grid properties
     */
    public String getGridProperties() {
        return gridProperties;
    }

    /**
     * Returns the Saucelabs URL.
     * 
     * @return the Saucelabs URL
     */
    public String getGridUrl() {
        return gridUrl;
    }

    /**
     * Returns the HTTPS proxy.
     * 
     * @return the HTTPS proxy
     */
    public String getProxyHttps() {
        return proxyHttps;
    }

    /**
     * Returns the initial horizontal offset of the browser window.
     * 
     * @return the initial horizontal offset of the browser window
     */
    public int getBrowserInitPositionX() {
        return browserInitPositionX;
    }

    /**
     * Returns the initial vertical offset of the browser window.
     * 
     * @return the initial vertical offset of the browser window
     */
    public int getBrowserInitPositionY() {
        return browserInitPositionY;
    }

    /**
     * Returns the URL represented by this {@code ClientProperties}.
     * 
     * @return the URL represented by this {@code ClientProperties}
     */
    public URL getClient() {
        return client;
    }
}