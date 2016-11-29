package org.jenkinsci.plugins.xunitreportview;

import hudson.Extension;
import hudson.Functions;
import hudson.Util;
import hudson.model.Item;
import hudson.model.ListView;
import hudson.model.Run;
import hudson.model.ViewDescriptor;
import hudson.model.Descriptor.FormException;
import hudson.util.FormValidation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Show junit reports in Jenkins view
 * 
 * @author sgao (sgao@redhat.com)
 */
public class XUnitReportView extends ListView {

	@DataBoundSetter
	String reportPath = "";

	public String getReportPath() {
		return reportPath;
	}

	@DataBoundSetter
	Map<String, Map<String, JunitXML>> resultMap = null;

	public Map<String, Map<String, JunitXML>> getResultMap() {
		return JunitUnmarshal.getResultMap(reportPath);
	}

	@DataBoundSetter
	ArrayList<String> jobList = null;

	public ArrayList<String> getJobList() {
		String filePath = this.reportPath;
		return ReportFile.getJobList(filePath);
	}

	@DataBoundSetter
	ArrayList<String> buildList = null;

	public ArrayList<String> getBuildList(String jobPath) {
		return ReportFile.getBuildList(jobPath);
	}

	@DataBoundSetter
	JunitXML buildXML = null;

	public JunitXML getBuildXML(String buildPath) {
		return JunitUnmarshal.parseBuildXML(buildPath);
	}

	/**
	 * @param name
	 *            name
	 */
	@DataBoundConstructor
	public XUnitReportView(String name) {
		super(name);
	}

	@Override
	protected void submit(StaplerRequest req) throws ServletException, IOException, FormException {
		super.submit(req);
		this.reportPath = req.getParameter("reportPath");
	}

	@Extension
	public static final class DescriptorImpl extends ViewDescriptor {
		public DescriptorImpl() {
			super(XUnitReportView.class);
		}

		@Override
		public String getDisplayName() {
			return "xUnit Report View";
		}
	}
}
