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
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.finra.jtaf.ewd.ExtWebDriver;
import org.finra.jtaf.ewd.session.SessionFactory;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

/**
 * SessionPool functionality used by SessionManager instances.
 *
 *
 */

public class DefaultSessionFactory implements SessionFactory {
    private static final long MILLISECONDS_IN_DAY = 86400000;
    private static final Object lock = new Object();
    private static final Log log = LogFactory.getLog(DefaultSessionFactory.class);
    private static boolean executedTaskKill = false;

    /*
     * (non-Javadoc)
     * 
     * @see org.finra.jtaf.ewd.session.SessionFactory
     */
    @Override
    public void cleanup(Map<String, String> options) throws Exception {

        ClientProperties properties = new ClientProperties(options.get("client"));

        if (!executedTaskKill) {
            synchronized (lock) {
                if (properties.isKillTasksAtStartup()) {
                    if (properties.getBrowser().equalsIgnoreCase("ie")
                            || properties.getBrowser().equalsIgnoreCase("iexplore")
                            || properties.getBrowser().equalsIgnoreCase("*iexplore")) {
                        try {
                            Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe /T");
                        } catch (IOException e) {
                            log.warn("Taskkill failed to kill any rogue IEDriverServer.exe tasks");
                        }
                        try {
                            Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe /T");
                        } catch (IOException e) {
                            log.warn("Taskkill failed to kill any rogue Internet Explorer browsers");
                        }
                    } else if (properties.getBrowser().equalsIgnoreCase("chrome")) {
                        if (properties.getOS() == null
                                || properties.getOS().equalsIgnoreCase("windows")) {
                            try {
                                Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");

                            } catch (IOException e) {
                                log.warn("Taskkill failed to kill any rogue chromedriver.exe processes");
                            }
                            try {
                                Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
                            } catch (IOException e) {
                                log.warn("Taskkill failed to kill any rogue chrome browsers");

                            }
                        } else if (properties.getOS().equalsIgnoreCase("linux")) {
                            try {
                                Runtime.getRuntime().exec("killall -9 chromedriver");
                            } catch (IOException e) {
                                log.warn("Taskkill failed to kill any rogue chromedriver.exe processes");
                            }
                            try {
                                Runtime.getRuntime().exec("killall -9 chrome");
                            } catch (IOException e) {
                                log.warn("Taskkill failed to kill any rogue chrome browsers");
                            }
                        } else if (properties.getOS().equalsIgnoreCase("mac")) {
                            try {
                                Runtime.getRuntime().exec("killall -KILL chromedriver");
                            } catch (IOException e) {
                                log.warn("Taskkill failed to kill any rogue chromedriver tasks");
                            }
                            try {
                                Runtime.getRuntime().exec("killall -KILL chrome");
                            } catch (IOException e) {
                                log.warn("Taskkill failed to kill any rogue chrome browsers");
                            }
                        } else {
                            log.warn("Taskkill failed to kill any rogue chromedriver or chrome tasks because the OS"
                                    + "provided is either incorrect or not supported");
                        }
                    } else if (properties.getBrowser().equalsIgnoreCase("firefox")
                            || properties.getBrowser().equalsIgnoreCase("*firefox")) {
                        if (properties.getOS() == null
                                || properties.getOS().equalsIgnoreCase("windows")) {
                            // there is no taskkill for FirefoxDriver because
                            // there is no "server" used for Firefox
                            try {
                                Runtime.getRuntime().exec("taskkill /F /IM firefox.exe /T");

                            } catch (IOException e) {
                                log.warn("Taskkill failed to kill any rogue firefox browsers");
                            }
                        } else if (properties.getOS().equalsIgnoreCase("linux")) {
                            try {
                                Runtime.getRuntime().exec("killall -9 firefox");
                            } catch (IOException e) {
                                log.warn("Taskkill failed to kill any rogue firefox browsers");
                            }
                        } else if (properties.getOS().equalsIgnoreCase("mac")) {
                            try {
                                Runtime.getRuntime().exec("killall -KILL firefox");
                            } catch (IOException e) {
                                log.warn("Taskkill failed to kill any rogue firefox browsers");
                            }
                        } else {
                            log.warn("Taskkill failed to kill any rogue firefox tasks because the OS"
                                    + "provided is either incorrect or not supported");
                        }
                    }
                }
                executedTaskKill = true;
            }
        }

        removeWebDriverTempOldFolders(properties);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.finra.jtaf.ewd.session.SessionFactory
     */
    @Override
    public DesiredCapabilities createCapabilities(Map<String, String> options) throws Exception {

        ClientProperties properties = new ClientProperties(options.get("client"));

        final String browser = properties.getBrowser();

        if (properties.isUseGrid()) {
            DesiredCapabilities capabilities = null;

            if (properties.getGridUrl().length() == 0) {
                throw new Exception(
                        "You must provide 'grid.url' to use Selenium Grid in client property file");
            }
            if (browser.length() == 0) {
                throw new Exception(
                        "You must provide 'browser' to use Selenium Grid  in client property file");
            }
            if (properties.getGridPlatform().length() == 0) {
                throw new Exception(
                        "You must provide 'grid.platform' to use Selenium Grid in client property file");
            }

            if (browser.equalsIgnoreCase("ie") || browser.equalsIgnoreCase("iexplore")
                    || browser.equalsIgnoreCase("*iexplore")) {
                capabilities = DesiredCapabilities.internetExplorer();
            } else if ((browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("*firefox"))) {
                capabilities = DesiredCapabilities.firefox();
            } else if (browser.equalsIgnoreCase("chrome")) {
                capabilities = DesiredCapabilities.chrome();
            } else if (browser.equalsIgnoreCase("safari")) {
                capabilities = DesiredCapabilities.safari();
            } else if (browser.equalsIgnoreCase("opera")) {
                capabilities = DesiredCapabilities.opera();
            } else if (browser.equalsIgnoreCase("android")) {
                capabilities = DesiredCapabilities.android();
            } else if (browser.equalsIgnoreCase("ipad")) {
                capabilities = DesiredCapabilities.ipad();
            } else if (browser.equalsIgnoreCase("iphone")) {
                capabilities = DesiredCapabilities.iphone();
            } else if (browser.equalsIgnoreCase("MS Edge") || browser.equalsIgnoreCase("MicrosoftEdge")) {
            	capabilities = DesiredCapabilities.edge();
			} else {
                throw new Exception("Unsupported browser: " + browser
                        + " Please refer to documentation for supported browsers.");
            }

            capabilities.setVersion(properties.getBrowserVersion());

            String platform = properties.getGridPlatform();
            if (platform != null && platform.length() > 0) {
                capabilities.setCapability("platform", platform);
            } else {
                // Default to Windows 7
                capabilities.setCapability("platform", "Windows 7");
            }

            // Set Proxy
            String proxyStr = properties.getProxy();
            String proxyHttps = properties.getProxyHttps();
            Proxy proxy = null;
            if (proxyStr != null && !proxyStr.equals("")) {
                proxy = new Proxy();
                proxy.setHttpProxy(proxyStr);

            }
            if (proxyHttps != null && !proxyHttps.equals("")) {
                if (proxy == null) {
                    proxy = new Proxy();
                }
                proxy.setSslProxy(proxyHttps);
            }

            if (proxy != null) {
                capabilities.setCapability(CapabilityType.PROXY, proxy);
            }

            // preparing data for session name
            String computerName = null;
            try {
                computerName = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
            }

            capabilities.setCapability("name", "JTAF EWD client=" + properties.getClient()
                    + "; started from " + computerName);

            String gridProperties = properties.getGridProperties();
            if (gridProperties != null && gridProperties.length() > 0) {
                String[] gridPropertiesSlpitted = gridProperties.split(" ");
                for (String gridPropertiesSlpittedCurrent : gridPropertiesSlpitted) {
                    if (gridPropertiesSlpittedCurrent != null
                            && gridPropertiesSlpittedCurrent.length() > 0) {
                        String[] propertyNameAndValue = gridPropertiesSlpittedCurrent.split("=");
                        if (propertyNameAndValue.length == 2) {
                            capabilities.setCapability(propertyNameAndValue[0],
                                    propertyNameAndValue[1]);
                        }
                    }
                }
            }

            return capabilities;
        } else {
            log.debug("browser [" + browser + "]");

            // Turning off all console logs using java.util.logging
            Handler[] h = java.util.logging.LogManager.getLogManager().getLogger("").getHandlers();
            for (int i = 0; i < h.length; i++) {
                if (h[i] instanceof ConsoleHandler) {
                    h[i].setLevel(Level.OFF);
                }
            }

            String proxyProperty = properties.getProxy();
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            if (proxyProperty != null) {
                Proxy proxy = new Proxy();
                proxy.setHttpProxy(proxyProperty).setFtpProxy(proxyProperty).setSslProxy(
                        proxyProperty);
                desiredCapabilities = new DesiredCapabilities();
                if (browser != null && browser.equalsIgnoreCase("chrome")) {
                    // chrome way of proxy initialization
                    desiredCapabilities.setCapability("chrome.switches", Arrays
                            .asList("--proxy-server=http://" + proxy));
                } else {
                    // ff and ie way of proxy initialization
                    desiredCapabilities.setCapability(CapabilityType.PROXY, proxy);
                }
            }

            if (properties.getDownloadFolder() != null
                    && properties.getDownloadFolder().length() > 0) {
                // '0' means to download to the desktop, '1' means to download
                // to the default "Downloads" directory, '2' means to use the
                // directory you specify in "browser.download.dir"
                desiredCapabilities.setCapability("browser.download.folderList", 2);
                desiredCapabilities.setCapability("browser.download.dir", System
                        .getProperty("user.dir")
                        + System.getProperty("file.separator") + properties.getDownloadFolder());
                desiredCapabilities
                        .setCapability(
                                "browser.helperApps.neverAsk.saveToDisk",
                                "text/csv, application/octet-stream, application/pdf, application/vnd.fdf, application/x-msdos-program, application/x-unknown-application-octet-stream, application/vnd.ms-powerpoint, application/excel, application/vnd.ms-publisher, application/x-unknown-message-rfc822, application/vnd.ms-excel, application/msword, application/x-mspublisher, application/x-tar, application/zip, application/x-gzip,application/x-stuffit,application/vnd.ms-works, application/powerpoint, application/rtf, application/postscript, application/x-gtar, video/quicktime, video/x-msvideo, video/mpeg, audio/x-wav, audio/x-midi, audio/x-aiff");
                desiredCapabilities.setCapability("browser.helperApps.alwaysAsk.force", false);
                desiredCapabilities.setCapability("browser.download.manager.showWhenStarting",
                        false);
            }
            return desiredCapabilities;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.finra.jtaf.ewd.session.SessionFactory
     */
    @Override
    public WebDriver createInnerDriver(Map<String, String> options, DesiredCapabilities capabilities)
            throws Exception {
        ClientProperties properties = new ClientProperties(options.get("client"));

        WebDriver wd = null;
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities(capabilities);
        String browser = properties.getBrowser();

        if (properties.isUseGrid()) {
            RemoteWebDriver remoteWebDriver = new RemoteWebDriver(new URL(properties.getGridUrl()),
                    capabilities);
            remoteWebDriver.setFileDetector(new LocalFileDetector());
            wd = remoteWebDriver;
        }else {
            if (browser == null || browser.equals("")) {
                throw new RuntimeException(
                        "Browser cannot be null. Please set 'browser' in client properties. Supported browser types: IE, Firefox, Chrome, Safari, HtmlUnit.");
            } else if (browser.equalsIgnoreCase("ie") || browser.equalsIgnoreCase("iexplore")
                    || browser.equalsIgnoreCase("*iexplore")) {
                String webdriverIEDriver = properties.getWebDriverIEDriver();

                if (webdriverIEDriver != null) {
                    System.setProperty("webdriver.ie.driver", webdriverIEDriver);
                }

                String browserVersion = properties.getBrowserVersion();

                if (browserVersion == null || browserVersion.equals("")) {
                    throw new RuntimeException(
                            "When using IE as the browser, please set 'browser.version' in client properties");
                } else {
                    if (browserVersion.startsWith("9")) {
                        desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                        desiredCapabilities
                                .setCapability(
                                        InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                                        true);
                        wd = new InternetExplorerDriver(desiredCapabilities);
                    } else {
                        wd = new InternetExplorerDriver(desiredCapabilities);
                    }
                }
            } else if ((browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("*firefox"))) {
                final String ffProfileFolder = properties.getFirefoxProfileFolder();
                final String ffProfileFile = properties.getFirefoxProfileFile();
                final String path = properties.getBinaryPath();

                if (path != null) {
                    FirefoxBinary fireFox = getFFBinary(path);
                    FirefoxProfile ffp = null;

                    if (ffProfileFolder != null) {
                        ffp = new FirefoxProfile(new File(ffProfileFolder));
                    } else {
                        ffp = new FirefoxProfile();
                    }

                    if (ffProfileFile != null) {
                        addPreferences(ffp, ffProfileFile);
                    }

                    addPreferences(ffp);

                    List<String> ffExtensions = properties.getFirefoxExtensions();
                    if (ffExtensions != null && ffExtensions.size() > 0) {
                        addExtensionsToFirefoxProfile(ffp, ffExtensions);
                    }

                    wd = new FirefoxDriver(fireFox, ffp, desiredCapabilities);
                } else {
                    wd = new FirefoxDriver(desiredCapabilities);
                }
            } else if (browser.equalsIgnoreCase("chrome")) {

                String webdriverChromeDriver = properties.getWebDriverChromeDriver();

                if (webdriverChromeDriver != null) {
                    System.setProperty("webdriver.chrome.driver", webdriverChromeDriver);
                }

                wd = new ChromeDriver(desiredCapabilities);
            } else if (browser.equalsIgnoreCase("safari")) {
                wd = new SafariDriver(desiredCapabilities);
            } else if (browser.equalsIgnoreCase("MS Edge") || browser.equalsIgnoreCase("MicrosoftEdge")
            		|| browser.equalsIgnoreCase("edge")) {
                wd = new EdgeDriver(desiredCapabilities);
            } else if (browser.equalsIgnoreCase("htmlunit")) {
                wd = new HtmlUnitDriver(desiredCapabilities);
                ((HtmlUnitDriver) wd).setJavascriptEnabled(true);
            } else {
                throw new Exception("Unsupported browser type: " + browser
                        + ". Supported browser types: IE, Firefox, Chrome, Safari, HtmlUnit.");
            }

            // move browser windows to specific position. It's useful for
            // debugging...
            final int browserInitPositionX = properties.getBrowserInitPositionX();
            final int browserInitPositionY = properties.getBrowserInitPositionY();
            if (browserInitPositionX != 0 || browserInitPositionY != 0) {
                wd.manage().window().setSize(new Dimension(1280, 1024));
                wd.manage().window().setPosition(new Point(browserInitPositionX, browserInitPositionY));
            }
        }

        return wd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.finra.jtaf.ewd.session.SessionFactory
     */
    @Override
    public ExtWebDriver createNewSession(Map<String, String> options, WebDriver wd) {
        DefaultExtWebDriver selenium = new DefaultExtWebDriver();
        selenium.setWrappedDriver(wd);

        // Get client properties file
        ClientProperties properties = new ClientProperties(options.get("client"));

        // Set client properties (specific to our factory/implementation)
        selenium.setClientProperties(properties);

        // Set timeout value
        selenium.setMaxRequestTimeout(properties.getMaxRequestTimeoutString());

        // Highlighting
                selenium.setHighlightColors(properties.getHighlightColorMap());
                selenium.setIsHighlight(properties.isHighlight());

        return selenium;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.finra.jtaf.ewd.session.SessionFactory
     */
    @Override
    public Map<String, String> createDefaultOptions() {
        HashMap<String, String> ret = new HashMap<String, String>();
        // Add any default options needed here (like path to default properties)

        ret.put("client", "client.properties");

        return ret;
    }

    /**
     *
     * @param ffp
     *            for use in setting the firefox profile for the tests to use
     *            when running firefox
     */
    private static void addPreferences(FirefoxProfile ffp) {
        ffp.setPreference("capability.policy.default.HTMLDocument.readyState", "allAccess");
        ffp.setPreference("capability.policy.default.HTMLDocument.compatMode", "allAccess");
        ffp.setPreference("capability.policy.default.Document.compatMode", "allAccess");
        ffp.setPreference("capability.policy.default.Location.href", "allAccess");
        ffp.setPreference("capability.policy.default.Window.pageXOffset", "allAccess");
        ffp.setPreference("capability.policy.default.Window.pageYOffset", "allAccess");
        ffp.setPreference("capability.policy.default.Window.frameElement", "allAccess");
        ffp.setPreference("capability.policy.default.Window.frameElement.get", "allAccess");
        ffp.setPreference("capability.policy.default.Window.QueryInterface", "allAccess");
        ffp.setPreference("capability.policy.default.Window.mozInnerScreenY", "allAccess");
        ffp.setPreference("capability.policy.default.Window.mozInnerScreenX", "allAccess");
    }

    /**
     *
     * @param ffp
     *            the firefox profile you are using
     * @param propertiesFile
     *            the properties you want to add to the profile
     */
    private static void addPreferences(FirefoxProfile ffp, String propertiesFile) {
        Properties firefoxProfile = new Properties();

        try {
            firefoxProfile.load(new FileInputStream(propertiesFile));
        } catch (Throwable th) {
            throw new RuntimeException("Could not load firefox profile", th);
        }

        for (Object o : firefoxProfile.keySet()) {
            String key = (String) o;
            String getVal = null;
            if (key.endsWith(".type")) {
                getVal = key.substring(0, key.lastIndexOf("."));
            }

            if (getVal != null) {
                String type = firefoxProfile.getProperty(key);
                String value = firefoxProfile.getProperty(getVal + ".value");

                if (value.contains("${PROJECT_PATH}")) {
                    String projectPath = (new File("")).getAbsolutePath();
                    value = projectPath + value.replaceAll("\\$\\{PROJECT_PATH\\}", "");
                }

                if (type.equalsIgnoreCase("BOOLEAN")) {
                    ffp.setPreference(getVal, Boolean.parseBoolean(value));
                } else if (type.equalsIgnoreCase("STRING")) {
                    ffp.setPreference(getVal, value);
                } else if (type.equalsIgnoreCase("INTEGER") || type.equalsIgnoreCase("INT")) {
                    ffp.setPreference(getVal, Integer.parseInt(value));
                }
            }
        }
    }

    /**
     *
     * @param ffp
     *            the firefox profile specified
     * @param extensions
     *            extensions desired to be added
     * @throws IOException
     */
    private static void addExtensionsToFirefoxProfile(FirefoxProfile ffp, List<String> extensions)
            throws IOException {
        for (String s : extensions) {
            ffp.addExtension(new File(s));
        }
    }

    /**
     *
     * @param filePath
     *            the binary path location of the firefox app (where it's
     *            installed)
     * @return
     */
    private static FirefoxBinary getFFBinary(String filePath) {
        File[] possibleLocations = { new File(filePath != null ? filePath : ""),
                new File("C:\\Program Files\\Mozilla Firefox\\firefox.exe"),
                new File("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe"), };

        File ffbinary = null;

        for (File curr : possibleLocations) {
            if (curr.exists()) {
                ffbinary = curr;
                break;
            }
        }

        if (ffbinary == null) {
            throw new RuntimeException(
                    "Unable to find firefox binary, please ensure that firefox is installed "
                            + "on your system. If it is then please determine the path to your firefox.exe and set it as "
                            + "binaryPath=<FIREFOX PATH HERE>");
        } else {
            return new FirefoxBinary(ffbinary);
        }
    }

    /**
     * This method cleans out folders where the WebDriver temp information is
     * stored.
     *
     * @param properties
     *            client properties specified
     */
    private static void removeWebDriverTempOldFolders(ClientProperties properties) {
        String tempFolder = System.getProperty("java.io.tmpdir");

        int numberOfDaysToKeepTempFolders = properties.getNumberOfDaysToKeepTempFolders();
        if (numberOfDaysToKeepTempFolders < 0) {
            numberOfDaysToKeepTempFolders = 7;
        }

        List<String> tempFolderNameContainsList = new ArrayList<String>();
        tempFolderNameContainsList.add("anonymous");
        tempFolderNameContainsList.add("scoped_dir");
        tempFolderNameContainsList.add("webdriver-ie");

        // add parameters from config file
        String tempFolderNameContainsListFromProp = properties.getTempFolderNameContainsList();
        if (tempFolderNameContainsListFromProp != null) {
            String[] tempFolderNameContainsListFromPropSpit = tempFolderNameContainsListFromProp
                    .split(",");
            for (String name : tempFolderNameContainsListFromPropSpit) {
                tempFolderNameContainsList.add(name);
            }
        }

        removeFolders(tempFolder, tempFolderNameContainsList, numberOfDaysToKeepTempFolders);
    }

    /**
     * This method can be called to remove specific folders or set how long you
     * want to keep the temp information.
     *
     * @param folder
     *            which temp folder you want to remove
     * @param folderTemplates
     *            the templates of these temp folders
     * @param numberOfDaysToKeepTempFolders
     *            how long you want to keep the temp information
     */
    private final static void removeFolders(String folder, List<String> folderTemplates,
            int numberOfDaysToKeepTempFolders) {
        long dateToRemoveFiledAfter = (new Date()).getTime()
                - (numberOfDaysToKeepTempFolders * MILLISECONDS_IN_DAY);

        File tempFolder = new File(folder);
        if(null == tempFolder.listFiles()) {
            log.debug("Folder '" + tempFolder.getName() + "' was empty. Nothing to delete");
            return;
        }

        for (File currentFile : tempFolder.listFiles()) {
            if (currentFile.isDirectory()) {
                for (String folderTemplate : folderTemplates) {
                    if (currentFile.getName().contains(folderTemplate)
                            && (currentFile.lastModified() < dateToRemoveFiledAfter)) {
                        try {
                            currentFile.delete();
                            FileUtils.deleteDirectory(currentFile);
                            log.debug("Folder '" + currentFile.getName() + "' deleted...");
                        } catch (Exception e) {
                            log.fatal("Error deleting folder '" + currentFile.getName() + "'");
                        }
                    }
                }
            }
        }
    }
}
