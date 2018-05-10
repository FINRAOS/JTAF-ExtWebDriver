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
package org.finra.jtaf.ewd.widget.element.html;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.ccil.cowan.tagsoup.Parser;
import org.finra.jtaf.ewd.widget.IElement;
import org.finra.jtaf.ewd.widget.ITable;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.element.Element;
import org.finra.jtaf.ewd.widget.element.InteractiveElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * HTML Table element
 * 
 */
public class Table extends InteractiveElement implements ITable
{

    private String xPathLocator = null;

    /**
     * 
     * @param locator
     *            XPath, ID, name, CSS Selector, class name, or tag name
     * @throws WidgetException
     */
    public Table(String locator) throws WidgetException
    {
        super(locator);
    }

    /**
     * 
     * @param locator
     *            XPath, ID, name, CSS Selector, class name, or tag name
     * @throws WidgetException
     */
    public Table(By locator) throws WidgetException
    {
        super(locator);
    }

    /**
     * 
     * @param locator
     *            XPath, ID, name, CSS Selector, class name, or tag name
     * @return String of the xpath
     * @throws WidgetException
     */
    // Since the methods are implemented based on XPath locator type, we
    // need to regenerate the locator using
    // unique attribute value
    private String generateXPathLocator() throws WidgetException
    {
        if (xPathLocator == null)
        {
            IElement elem = new Element(getByLocator());
            String key = "tablewidgetattribute";
            long value = System.currentTimeMillis();
            elem.eval("arguments[0].setAttribute('" + key + "', '" + value + "')");
            xPathLocator = "//*[@" + key + "='" + value + "']";
        }
        return xPathLocator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.ITable#getTableHeaders()
     */
    @Override
    public List<String> getTableHeaders() throws WidgetException
    {
        List<String> list = new ArrayList<String>();

        try
        {

            NodeList nodes = getNodeListUsingJavaXPath(getTableXPath(generateXPathLocator())
                    + "//thead[not(contains(@style,'display: none') or contains(@style,'visibility: hidden'))]//th");
            if (nodes.getLength() == 0)
            {
                nodes = getNodeListUsingJavaXPath(getTableXPath(generateXPathLocator()) + "//th");
            }

            if (nodes.getLength() == 0)
            {
                throw new WidgetException("Table headers do not exist", generateXPathLocator());
            }

            for (int i = 0; i < nodes.getLength(); i++)
            {
                Node n = nodes.item(i);
                if (n != null && n.getNodeName() != null && n.getNodeName() != null)
                {
                    NamedNodeMap thNodeAttributes = n.getAttributes();
                    Node thStyleAttr = thNodeAttributes.getNamedItem("style");
                    if (thStyleAttr != null)
                    {
                        String style = thStyleAttr.getTextContent();
                        style = style.toLowerCase();
                        if (!(style.contains("display: none") || style
                                .contains("visibility: hidden")))
                        {
                            list.add(n.getTextContent().trim());
                        }
                    }
                    else
                    {
                        list.add(n.getTextContent().trim());
                    }

                }
            }
        }
        catch (Exception e)
        {
            throw new WidgetException("Error fetching table headers", generateXPathLocator(), e);
        }

        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.ITable#getTableRowColumnData(int,
     * int)
     */
    @Override
    public String getTableRowColumnData(int rowNumber, int columnNumber) throws WidgetException
    {
        try
        {
            String[] data = getTableRowDataArray(rowNumber);
            return data[columnNumber - 1];
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while getting table row column data at row="
                    + rowNumber + " column=" + columnNumber, generateXPathLocator(), e);
        }
    }

    /**
     * Retrieves the entire HTML table data within tbody (visible data)
     * 
     * @return List of table row data. List index reflects row number. Each item
     *         represents table row data in String array
     * @throws WidgetException
     */
    @Override
    public List<String[]> getTableDataInArray() throws WidgetException
    {
        try
        {
            List<String[]> list = new ArrayList<String[]>();
            NodeList nodes = getNodeListUsingJavaXPath(getTableXPath(generateXPathLocator()) + "/tbody[1]");
            NodeList tbodyChildNodes = nodes.item(0).getChildNodes();
            for (int i = 0; i < tbodyChildNodes.getLength(); i++)
            {
                Node n = tbodyChildNodes.item(i);
                if (n != null && n.getNodeName() != null && n.getNodeName() != null
                        && n.getNodeName().equalsIgnoreCase("tr"))
                {
                    NamedNodeMap trNodeAttributes = n.getAttributes();
                    Node trStyleAttr = trNodeAttributes.getNamedItem("style");
                    boolean proceed = true;
                    if (trStyleAttr != null)
                    {
                        String style = trStyleAttr.getTextContent();
                        style = style.toLowerCase();
                        if (style.contains("display: none") || style.contains("display:none")
                                || style.contains("visibility: hidden"))
                        {
                            proceed = false;
                        }
                    }

                    if (proceed)
                    {
                        List<String> rowData = new ArrayList<String>();
                        NodeList cnl = n.getChildNodes();
                        for (int j = 0; j < cnl.getLength(); j++)
                        {
                            Node cn = cnl.item(j);

                            if (cn != null && cn.getNodeName() != null && cn.getNodeName() != null
                                    && cn.getNodeName().equalsIgnoreCase("td"))
                            {
                                NamedNodeMap tdNodeAttributes = cn.getAttributes();
                                Node tdStyleAttr = tdNodeAttributes.getNamedItem("style");
                                if (tdStyleAttr != null)
                                {
                                    String style = tdStyleAttr.getTextContent();
                                    style = style.toLowerCase();
                                    if (!(style.contains("display: none")
                                            || style.contains("display:none") || style
                                                .contains("visibility: hidden")))
                                    {
                                        rowData.add(getRecursiveTextContext(cn).trim());
                                    }
                                }
                                else
                                {
                                    rowData.add(getRecursiveTextContext(cn).trim());
                                }
                            }
                        }

                        // If rowData size == 0 that means there were no TDs
                        // inside of TR.
                        // It triggers that the row was used as headers
                        if (rowData.size() > 0)
                        {
                            list.add(rowData.toArray(new String[rowData.size()]));
                        }
                    }
                }
            }

            return list;
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while getting table data in array", generateXPathLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.ITable#getTableDataInMap()
     */
    @Override
    public List<Map<String, String>> getTableDataInMap() throws WidgetException
    {
        try
        {
            List<Map<String, String>> tableDataInMap = new ArrayList<Map<String, String>>();

            List<String> headers = getTableHeaders();
            List<String[]> tableData = getTableDataInArray();

            for (String[] rowData : tableData)
            {
                Map<String, String> rowDataMap = new HashMap<String, String>();
                for (int i = 0; i < rowData.length; i++)
                {
                    rowDataMap.put(headers.get(i), rowData[i]);
                }

                tableDataInMap.add(rowDataMap);
            }

            return tableDataInMap;
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while getting table data in map", generateXPathLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.ITable#getTableRowCount()
     */
    @Override
    public int getTableRowCount() throws WidgetException
    {
        try
        {
            return getTableDataInArray().size();
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while getting table row count", generateXPathLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.ITable#isItemExist(java.util.Map)
     */
    @Override
    public boolean isItemExist(Map<String, String> item) throws WidgetException
    {
        try
        {
            List<Map<String, String>> tableData = getTableDataInMap();
            Set<String> keys = item.keySet();
            for (Map<String, String> rowData : tableData)
            {
                boolean found = true;
                for (String key : keys)
                {
                    String actual = rowData.get(key);
                    String expect = item.get(key);

                    if (!actual.equals(expect))
                    {
                        found = false;
                    }
                }

                if (found)
                {
                    return true;
                }
            }

            return false;
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while determining whether item " + item
                    + " exists in table", generateXPathLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.ITable#getRowNumber(java.util.Map)
     */
    @Override
    public int getRowNumber(Map<String, String> item) throws WidgetException
    {
        try
        {
            List<Map<String, String>> tableData = getTableDataInMap();
            Set<String> keys = item.keySet();
            int index = 1;
            for (Map<String, String> rowData : tableData)
            {
                boolean found = true;
                for (String key : keys)
                {
                    String actual = rowData.get(key);
                    String expect = item.get(key);

                    if (!actual.equals(expect))
                    {
                        found = false;
                    }
                }

                if (found)
                {
                    return index;
                }

                index++;
            }

            return -1;
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while determining row number matching item " + item
                    + " in table", generateXPathLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.ITable#getTableColumnCount()
     */
    @Override
    public int getTableColumnCount() throws WidgetException
    {
        try
        {
            return getTableHeaders().size();
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while getting table headers", generateXPathLocator(), e);
        }
    }

    /**
     * 
     * @param xpath
     *            of the NodeList
     * @return a list of nodes found at the xpath
     * @throws Exception
     */
    private NodeList getNodeListUsingJavaXPath(String xpath) throws Exception
    {
        XPathFactory xpathFac = XPathFactory.newInstance();
        XPath theXpath = xpathFac.newXPath();

        String html = getGUIDriver().getHtmlSource();
        html = html.replaceAll(">\\s+<", "><");
        InputStream input = new ByteArrayInputStream(html.getBytes());

        XMLReader reader = new Parser();
        reader.setFeature(Parser.namespacesFeature, false);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        DOMResult result = new DOMResult();
        transformer.transform(new SAXSource(reader, new InputSource(input)), result);

        // This code gets a Node from the result.
        Node htmlNode = result.getNode();
        NodeList nodes = (NodeList) theXpath.evaluate(xpath, htmlNode, XPathConstants.NODESET);

        return nodes;
    }

    /**
     * 
     * @param rowNumber
     *            which row is desired
     * @return an array of strings with the data of that row
     * @throws Exception
     */
    private String[] getTableRowDataArray(int rowNumber) throws Exception
    {
        List<String[]> tableData = getTableDataInArray();
        return tableData.get(rowNumber - 1);
    }

    /**
     * 
     * @param n
     *            the node to start with
     * @return String the text content found
     * @throws Exception
     */
    private String getRecursiveTextContext(Node n) throws Exception
    {

        if (n == null)
        {
            return "";
        }
        else
        {
            if (n.getNodeType() != Node.TEXT_NODE)
            {
                NamedNodeMap attrs = n.getAttributes();
                Node styleAttr = attrs.getNamedItem("style");
                NodeList cnl = null;
                if (styleAttr != null)
                {
                    String style = styleAttr.getTextContent();
                    style = style.toLowerCase();
                    if (!(style.contains("display: none") || style.contains("visibility: hidden")))
                    {
                        cnl = n.getChildNodes();
                    }
                }
                else
                {
                    cnl = n.getChildNodes();
                }

                if (cnl != null && cnl.getLength() > 0)
                {
                    String textContent = "";
                    for (int i = 0; i < cnl.getLength(); i++)
                    {
                        textContent += getRecursiveTextContext(cnl.item(i));
                    }

                    return textContent;
                }
                else
                {
                    return "";
                }
            }
            else
            {
                return n.getTextContent();
            }
        }
    }

    /**
     * 
     * @param locator
     *            XPath, ID, name, CSS Selector, class name, or tag name
     * @return String of the xpath for the table
     * @throws WidgetException
     */
    private String getTableXPath(String locator) throws WidgetException
    {
        try
        {
            WebElement e = getGUIDriver().findElement(By.xpath(locator));
            if (e != null)
            {
                if (locator.startsWith("TABLE["))
                {
                    locator = "table[" + locator.substring("TABLE[".length());
                }

                return locator.replace("/TABLE[", "/table[");
            }
        }
        catch (Exception e)
        {
            // Continue
        }

        try
        {
            WebElement e = getGUIDriver().findElement(By.id(locator));
            if (e != null)
                return "//table[@id=\"" + locator + "\" or contains(@id,\"" + locator + "\")]";
        }
        catch (Exception e)
        {
            // Continue
        }

        try
        {
            WebElement e = getGUIDriver().findElement(By.name(locator));
            if (e != null)
                return "//table[@name=\"" + locator + "\" or contains(@name,\"" + locator + "\")]";
        }
        catch (Exception e)
        {
            // Continue
        }

        try
        {
            WebElement e = getGUIDriver().findElement(By.className(locator));
            if (e != null)
                return "//table[@class=\"" + locator + "\" or contains(@class,\"" + locator
                        + "\")]";
        }
        catch (Exception e)
        {
            // Continue
        }

        try
        {
            WebElement e = getGUIDriver().findElement(By.tagName(locator));
            if (e != null)
                return "//table";
        }
        catch (Exception e)
        {
            // Continue
        }

        throw new NoSuchElementException("Could not find table element at " + locator);
    }

    @Override
    public boolean contains(IElement targetWidget) throws WidgetException
    {
        // If this table is not present, throw a WidgetException.
        if (!isElementPresent())
        {
            throw new WidgetException("Cannot call contains on a widget that is not present", getByLocator());
        }

        // If the target widget is not present, then clearly the table does not contain it.
        try
        {
            if (!targetWidget.isElementPresent())
            {
                return false;
            }
        }
        catch (WidgetException e)
        {
            // See note @top of try block
            return false;
        }

        String pageInnards = getGUIDriver().getPageSource();
        String tableInnards = getInnerHTML();
        String targetInnards = targetWidget.getInnerHTML();

        if (!pageInnards.contains(tableInnards) || !pageInnards.contains(targetInnards))
        {
            System.err.println("getPageSource not compatible with getInnerHTML, see Table.java:contains");
        }

        if (!tableInnards.contains(targetInnards))
        {
            return false;
        }
        else if (pageInnards.indexOf(targetInnards) == pageInnards.lastIndexOf(targetInnards))
        {
            return true;
        }
        else if (tableInnards.split(targetInnards).length == pageInnards.split(targetInnards).length)
        {
            // There is more than one occurrence of the target's innards in the page, but they're all within the table.
            return true;
        }
        else
        {
            // This is the tricky case. There is more than one occurrence of targetInnards on the page, at least one of which is inside the table.
            // TODO test the above, get back to this.
            System.err.println("Returning false, but we can't tell for sure yet.");
            return false;
        }
    }
}
