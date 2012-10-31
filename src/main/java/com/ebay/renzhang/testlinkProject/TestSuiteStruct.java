package com.ebay.renzhang.testlinkProject;

import java.util.ArrayList;

public class TestSuiteStruct {
	String suiteName;
	ArrayList<TestCaseStruct> list;

	public TestSuiteStruct(String name, ArrayList<TestCaseStruct> list) {
		this.suiteName = name;
		this.list = list;
	}

	public String getSuiteName() {
		return suiteName;
	}

	public void setSuiteName(String name) {
		this.suiteName = name;
	}

	public ArrayList<TestCaseStruct> getList() {
		return list;
	}

	public void setList(ArrayList<TestCaseStruct> list) {
		this.list = list;
	}
}
