package org.jenkinsci.plugins.xunitreportview;

import hudson.Extension;
import hudson.Util;
import hudson.model.*;
import hudson.model.ViewDescriptor;
import hudson.model.Descriptor.FormException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.bind.JavaScriptMethod;

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
	String filterPartten = "";

	public String getFilterPartten() {
		return filterPartten;
	}

	@DataBoundSetter
	private boolean viewLatestReport = false;

	public boolean isViewLatestReport() {
		return viewLatestReport;
	}

	public ArrayList<String> getJobList() {
		return ReportFile.getJobList(reportPath, filterPartten);
	}

	public ArrayList<String> getBuildList(String jobPath) {
		return ReportFile.getBuildList(jobPath);
	}

	public String getLatestBuild(String jobPath) {
		return ReportFile.getLatestBuild(jobPath);
	}

	@JavaScriptMethod
	public void deleteBuild(String buildPath) {
		ReportFile.deleteBuild(buildPath);
	}

	public Map<String, String> getBuildInfo(String buildPath) {
		return XMLUnmarshal.getBuildInfo(buildPath);
	}

	public Properties getRuntimeInfo(String buildPath) {
		return XMLUnmarshal.getRuntimeInfo(buildPath);
	}

	public JunitResultXML getJunitResultXML(String buildPath) {
		return XMLUnmarshal.parseJunitResultXML(buildPath);
	}

	public Map<String, ArrayList<Case>> getCases(JunitResultXML caseXML) {
		return XMLUnmarshal.getCasesMap(caseXML);
	}

	@JavaScriptMethod
	public void saveComment(String commentPath, String comment) {
		System.out.println(commentPath + ":" + comment);
		System.out.println("saveComment -- not completed yet");
	}

	public String getComment(String commentPath) {
		return XMLUnmarshal.getComment(commentPath);
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
		this.filterPartten = req.getParameter("filterPartten");
		String sViewLatestReport = Util.nullify(req.getParameter("viewLatestReport"));
		viewLatestReport = sViewLatestReport != null && "on".equals(sViewLatestReport);
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
