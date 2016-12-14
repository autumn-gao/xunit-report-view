package org.jenkinsci.plugins.xunitreportview;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class JunitUnmarshal {

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

	public static Properties getRuntimeInfo(String buildPath) {
		File runtimeFile = new File(buildPath + "/" + "RUNTIME_INFO.txt");
		FileInputStream inpf = null;
		try {
			inpf = new FileInputStream(runtimeFile);
		} catch (FileNotFoundException e1) {
			return null;
		}
		Properties runtimeProperties = new Properties();
		try {
			runtimeProperties.load(inpf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return runtimeProperties;
	}

	public static void main(String[] args) {
		// String reportPath = "/jenkins-results/reports";
		// JunitXML caseXML = JunitUnmarshal
		// .parseBuildXML("/jenkins-results/reports/rhsm-multy-arch-runtest/builds/2016-11-30_02-40-33");
		// System.out.println(JunitUnmarshal.getCasesMap(caseXML).get("failed").get(0).getClassName());
		System.out.println(getRuntimeInfo("/jenkins-results/reports/rhsm-multy-arch-runtest/builds/2016-11-30_02-40-36")
				.getProperty("RHEL_COMPOSE"));
	}
}
