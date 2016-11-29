package org.jenkinsci.plugins.xunitreportview;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Suite {
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	private String file;
	private String name;
	private String duration;

	@XmlElementWrapper(name = "cases")
	@XmlElement(name = "case")
	private ArrayList<Case> cases;

	public ArrayList<Case> getCase() {
		return cases;
	}

	public void setCase(ArrayList<Case> Case) {
		this.cases = Case;
	}

}