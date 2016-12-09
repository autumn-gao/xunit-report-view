package org.jenkinsci.plugins.xunitreportview;

import hudson.Extension;
import hudson.model.*;
import hudson.model.ViewDescriptor;
import hudson.model.Descriptor.FormException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
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
	String filterPartten = "";

	public String getFilterPartten() {
		return filterPartten;
	}

	public ArrayList<String> getJobList() {
		return ReportFile.getJobList(reportPath, filterPartten);
	}

	public ArrayList<String> getBuildList(String jobPath) {
		return ReportFile.getBuildList(jobPath);
	}

	public JunitXML getBuildXML(String buildPath) {
		return JunitUnmarshal.parseBuildXML(buildPath);
	}

	public Map<String, ArrayList<Case>> getCases(JunitXML caseXML) {
		return JunitUnmarshal.getCasesMap(caseXML);
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
