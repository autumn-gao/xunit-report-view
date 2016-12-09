package org.jenkinsci.plugins.xunitreportview;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class JunitUnmarshal {
	private static Map<String, Map<String, JunitXML>> job_build_junitxml_map = new HashMap<String, Map<String, JunitXML>>();
	private static Map<String, JunitXML> build_junitxml_map = null;

	public static Map<String, Map<String, JunitXML>> getResultMap(String reportPath) {
		try {
			Map<File, File[]> buildmap = JunitBuild.getBuilds(reportPath);
			Iterator<Entry<File, File[]>> entries = buildmap.entrySet().iterator();
			while (entries.hasNext()) {
				build_junitxml_map = new HashMap<String, JunitXML>();
				Entry<File, File[]> entry = entries.next();
				String job = entry.getKey().getName();
				for (File build : entry.getValue()) {
					File file = new File(build + "/" + "junitResult.xml");
					JAXBContext jaxbContext = JAXBContext.newInstance(JunitXML.class);
					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
					JunitXML junitxml = (JunitXML) jaxbUnmarshaller.unmarshal(file);
					build_junitxml_map.put(build.getName(), junitxml);
				}
				job_build_junitxml_map.put(job, build_junitxml_map);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return job_build_junitxml_map;
	}

	public static JunitXML parseBuildXML(String buildPath) {
		JunitXML junitxml = null;
		try {
			File file = new File(buildPath + "/" + "junitResult.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(JunitXML.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			junitxml = (JunitXML) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return junitxml;
	}

	public static Map<String, ArrayList<Case>> getCasesMap(JunitXML caseXML) {
		ArrayList<Case> failedCases = new ArrayList<Case>();
		ArrayList<Case> passedCases = new ArrayList<Case>();
		Map<String, ArrayList<Case>> allCases = new HashMap<String, ArrayList<Case>>();
		for (Case item : caseXML.getSuite().get(0).getCase()) {
			if (!"0".equals(item.getFailedSince())) {
				failedCases.add(item);
			} else {
				passedCases.add(item);
			}
		}
		allCases.put("failed", failedCases);
		allCases.put("passed", passedCases);
		return allCases;
	}

	public static Map<String, String> getBuildInfo(String buildPath) {
		Map<String, String> buildInfo = new HashMap<String, String>();
		JunitXML buildXML = parseBuildXML(buildPath);
		int fail_case = 0;
		int pass_case = 0;
		int total_case = 0;
		String duration = "";
		duration = buildXML.getDuration();
		for (Case item : buildXML.getSuite().get(0).getCase()) {
			if (!"0".equals(item.getFailedSince())) {
				fail_case += 1;
			} else {
				pass_case += 1;
			}
			total_case += 1;
		}
		buildInfo.put("duration", duration);
		buildInfo.put("fail_case", Integer.toString(fail_case));
		buildInfo.put("pass_case", Integer.toString(pass_case));
		buildInfo.put("total_case", Integer.toString(total_case));
		return buildInfo;
	}

	public static void main(String[] args) {
		// String reportPath = "/jenkins-results/reports";
		JunitXML caseXML = JunitUnmarshal
				.parseBuildXML("/jenkins-results/reports/rhsm-multy-arch-runtest/builds/2016-11-30_02-40-33");
		System.out.println(JunitUnmarshal.getCasesMap(caseXML).get("failed").get(0).getClassName());
	}
}
