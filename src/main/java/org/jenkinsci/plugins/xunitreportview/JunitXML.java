package org.jenkinsci.plugins.xunitreportview;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result")
public class JunitXML {
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getKeepLongStdio() {
		return keepLongStdio;
	}

	public void setKeepLongStdio(String keepLongStdio) {
		this.keepLongStdio = keepLongStdio;
	}

	private String duration;
	private String keepLongStdio;

	@XmlElementWrapper(name = "suites")
	@XmlElement(name = "suite")
	private ArrayList<Suite> suites;

	public ArrayList<Suite> getSuite() {
		return suites;
	}

	public void setSuite(ArrayList<Suite> Suite) {
		this.suites = Suite;
	}
}