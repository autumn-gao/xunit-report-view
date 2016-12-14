package org.jenkinsci.plugins.xunitreportview;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReportFile {
	private static Map<File, File[]> buildmap = null;
	private static ArrayList<String> joblist = null;
	private static ArrayList<String> buildlist = null;

	static ArrayList<String> getJobList(String filePath, final String filter) {
		joblist = new ArrayList<String>();
		File root = new File(filePath);
		File[] jobs = root.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (filter == null || filter == "") {
					return true;
				} else {
					Pattern pattern = Pattern.compile(filter);
					Matcher match = pattern.matcher(name);
					return match.find();
				}
			}
		});
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
				return daysPassed(name) < 30 && (!name.endsWith("-Delete"));
			}
		});
		for (File build : builds) {
			buildlist.add(build.getAbsolutePath());
		}
		// sort builds via time
		Collections.sort(buildlist, new Comparator<String>() {
			@Override
			public int compare(String f1, String f2) {
				return f2.compareTo(f1);
			}
		});
		return buildlist;
	}

	static String getLatestBuild(String job) {
		return getBuildList(job).get(0);
	}

	static String deleteBuild(String filePath) {
		File file = new File(filePath);
		File file_delete = new File(filePath + "-Delete");
		if (file.exists()) {
			file.renameTo(file_delete);
		}
		return "Sucess";
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
		// String reportPath = "/jenkins-results/reports";
		// String filterPartten = "^rhsm-*";
		// String filterPartten = "^virt-who-*";
		// File root = new File("/jenkins-results/reports/runtest");
		// System.out.println(getBuilds(reportPath).get(root)[1].getName());
		// System.out.println(getJobList(reportPath, filterPartten));
		// System.out.println(getBuildList("/jenkins-results/reports/rhsm-multy-arch-runtest"));
		// System.out.println(getLatestBuild("/jenkins-results/reports/rhsm-multy-arch-runtest"));
		System.out.println(deleteBuild("/jenkins-results/reports/rhsm-multy-arch-runtest/builds/2016-11-30_02-40-36"));

	}
}
