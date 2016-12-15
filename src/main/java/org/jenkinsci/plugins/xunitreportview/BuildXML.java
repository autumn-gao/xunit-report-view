package org.jenkinsci.plugins.xunitreportview;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "build")
public class BuildXML {
	private String number;

	@XmlElementWrapper(name = "actions")
	@XmlElement(name = "hudson.tasks.junit.TestResultAction")
	private ArrayList<Action> actions;

	public ArrayList<Action> getAction() {
		return actions;
	}

	public void setAction(ArrayList<Action> action) {
		this.actions = action;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}

class Action {
	private String failCount;
	private String skipCount;
	private String totalCount;
	private String testData;

	@XmlElementWrapper(name = "descriptions")
	@XmlElement(name = "entry")
	private ArrayList<Entry> entries;

	public ArrayList<Entry> getEntry() {
		return entries;
	}

	public void setEntry(ArrayList<Entry> entry) {
		this.entries = entry;
	}

	public String getFailCount() {
		return failCount;
	}

	public void setFailCount(String failCount) {
		this.failCount = failCount;
	}

	public String getSkipCount() {
		return skipCount;
	}

	public void setSkipCount(String skipCount) {
		this.skipCount = skipCount;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getTestData() {
		return testData;
	}

	public void setTestData(String testData) {
		this.testData = testData;
	}

}

class Entry {
	private String string;

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}