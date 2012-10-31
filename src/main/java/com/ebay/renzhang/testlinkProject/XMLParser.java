package com.ebay.renzhang.testlinkProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XMLParser {
	String xmlContent;
	String url;

	public XMLParser(String url) {
		this.url = url;
	}

	public void readXML() {
		URL url = null;
		try {
			url = new URL(this.url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		xmlContent = "";
		String str = null;
		try {
			while ((str = reader.readLine()) != null) {
				this.xmlContent = this.xmlContent + str;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TestSuiteStruct parseXML() {
		ArrayList<TestCaseStruct> tcList = new ArrayList<TestCaseStruct>();
		String suiteName = null;
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(this.xmlContent);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		Element root = doc.getRootElement();
		for (Iterator iter = root.elementIterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			if (element.getName().equals("suite")) {
				for (Iterator iterInner = element.elementIterator(); iterInner
						.hasNext();) {
					Element caseElement = (Element) iterInner.next();
					if (caseElement.getName().equals("case")) {
						TestCaseStruct tesecase = parseNode(caseElement);
						tcList.add(tesecase);
					} else if (caseElement.getName().equals("name")) {
						suiteName = caseElement.getText();
					}
				}
			}
		}
		String[] str = suiteName.split(" ");
		TestSuiteStruct testsuite = new TestSuiteStruct(str[6], tcList);
		return testsuite;
	}

	public TestCaseStruct parseNode(Element node) {
		String className = "";
		String name = "";
		String status = "";
		String errorStackTrace = "";

		for (Iterator iter = node.elementIterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			if (element.getName().equals("className")) {
				className = element.getText();
				String[] str = className.split(" ");
				className = str[5];
			} else if (element.getName().equals("name")) {
				name = element.getText();
			} else if (element.getName().equals("status")) {
				status = element.getText();
				if (status.equals("PASSED") || status.equals("FIXED"))
					errorStackTrace = "";
			} else if (element.getName().equals("errorStackTrace")) {
				errorStackTrace = element.getText();
			}
		}

		TestCaseStruct testcase = new TestCaseStruct(className, name, status,
				errorStackTrace);
		return testcase;
	}
}
