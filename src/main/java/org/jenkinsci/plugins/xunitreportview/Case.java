package org.jenkinsci.plugins.xunitreportview;

public class Case {
	private String duration;
	private String className;
	private String testName;
	private String skipped;
	private String stdout;
	private String failedSince;

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getSkipped() {
		return skipped;
	}

	public void setSkipped(String skipped) {
		this.skipped = skipped;
	}

	public String getStdout() {
		return stdout;
	}

	public void setStdout(String stdout) {
		this.stdout = stdout;
	}

	public String getFailedSince() {
		return failedSince;
	}

	public void setFailedSince(String failedSince) {
		this.failedSince = failedSince;
	}
}