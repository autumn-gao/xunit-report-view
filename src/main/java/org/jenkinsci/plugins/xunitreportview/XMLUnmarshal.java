package org.jenkinsci.plugins.xunitreportview;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class XMLUnmarshal {

	public static JunitResultXML parseJunitResultXML(String buildPath) {
		String xml_file = buildPath + "/" + "junitResult.xml";
		JunitResultXML junitResultXML = null;
		try {
			File file = new File(xml_file);
			JAXBContext jaxbContext = JAXBContext.newInstance(JunitResultXML.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			junitResultXML = (JunitResultXML) jaxbUnmarshaller.unmarshal(file);
		} catch (Exception e) {
			System.out.println("Failed to parse: " + xml_file);
		}
		return junitResultXML;
	}

	public static BuildXML parseBuildXML(String buildPath) {
		String xml_file = buildPath + "/" + "build.xml";
		BuildXML buildXML = null;
		try {
			File file = new File(xml_file);
			JAXBContext jaxbContext = JAXBContext.newInstance(BuildXML.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			buildXML = (BuildXML) jaxbUnmarshaller.unmarshal(file);
		} catch (Exception e) {
			System.out.println("Failed to parse: " + xml_file);
		}
		return buildXML;
	}

	public static Map<String, ArrayList<Case>> getCasesMap(JunitResultXML caseXML) {
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
		try {
			JunitResultXML junitResultXML = parseJunitResultXML(buildPath);
			int fail_case = 0;
			int pass_case = 0;
			int total_case = 0;
			String duration = "";
			duration = junitResultXML.getDuration();
			for (Case item : junitResultXML.getSuite().get(0).getCase()) {
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
		} catch (Exception e) {
			System.out.println("Failed to get build info: " + buildInfo);
		}
		return buildInfo;
	}

	public static Properties getRuntimeInfo(String buildPath) {
		String runtime_info_file = buildPath + "/" + "RUNTIME_INFO.txt";
		Properties runtimeProperties = new Properties();
		try {
			File runtimeFile = new File(runtime_info_file);
			FileInputStream inpf = null;
			inpf = new FileInputStream(runtimeFile);
			runtimeProperties.load(inpf);
		} catch (Exception e) {
			System.out.println("Failed to get properties file: " + runtime_info_file);
		}
		return runtimeProperties;
	}

	public static String getComment(String buildPath) {
		return parseBuildXML(buildPath).getAction().get(0).getEntry().get(0).getString();
	}

	public static void main(String[] args) {
		System.out.println(
				parseJunitResultXML("/jenkins-results/reports/rhsm-multy-arch-runtest/builds/2016-11-30_02-40-36"));
	}
}
