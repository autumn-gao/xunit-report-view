package org.jenkinsci.plugins.xunitreportview;

import java.io.File;
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

	public static void main(String[] args) {
		String reportPath = "/jenkins-results/reports";
		System.out.println(JunitUnmarshal.getResultMap(reportPath).get("rhsm-multy-arch-runtest").get("2016-11-04_02-40-34").getSuite().get(0)
				.getCase().get(1).getStdout());
	}
}
