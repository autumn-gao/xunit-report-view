package org.jenkinsci.plugins.xunitreportview;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportFile {
	private static Map<File, File[]> buildmap = null;
	private static ArrayList<String> joblist = null;
	private static ArrayList<String> buildlist = null;

	static ArrayList<String> getJobList(String filePath) {
		joblist = new ArrayList<String>();
		File root = new File(filePath);
		File[] jobs = root.listFiles();
		for (File job : jobs) {
			joblist.add(job.getAbsolutePath());
		}
		return joblist;
	}

	static ArrayList<String> getBuildList(String job) {
		buildlist = new ArrayList<String>();
		File root = new File(job + "/" + "builds");
		File[] builds = root.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return daysPassed(name) < 30;
			}
		});
		for (File build : builds) {
			buildlist.add(build.getAbsolutePath());
		}
		return buildlist;
	}

	static Map<File, File[]> getBuilds(String filePath) {
		buildmap = new HashMap<File, File[]>();
		File root = new File(filePath);
		File[] jobs = root.listFiles();
		for (File job : jobs) {
			if (job.isDirectory()) {
				File build = new File(job.getAbsolutePath() + "/" + "builds");
				File[] buildlist = build.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return daysPassed(name) < 30;
					}
				});
				buildmap.put(job, buildlist);
			}
		}
		return buildmap;
	}

	public static int daysPassed(String date_string) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		long between_days = 0;
		try {
			Date date = sdf.parse(date_string);
			date = sdf.parse(sdf.format(date));
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			long build_time = cal.getTimeInMillis();
			long now = System.currentTimeMillis();
			between_days = (now - build_time) / (1000 * 3600 * 24);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Integer.parseInt(String.valueOf(between_days));
	}

	public static void main(String[] args) throws Exception {
		String reportPath = "/jenkins-results/reports";
		// File root = new File("/jenkins-results/reports/runtest");
		// System.out.println(getBuilds(reportPath).get(root)[1].getName());
		// System.out.println(getJobList(reportPath));
		System.out.println(getBuildList("/jenkins-results/reports/rhsm-multy-arch-runtest"));
	}
}
