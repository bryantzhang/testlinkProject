package testlinkProject;

import java.util.ArrayList;
import br.eti.kinoshita.testlinkjavaapi.model.Build;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestSuite;

public class TestLinkClient {
	String url;

	public TestLinkClient(String url) {
		this.url = url;
	}

	public void processUrl() {
		String buildUrl = url + "testReport/api/xml";
		XMLParser parser = new XMLParser(buildUrl);
		parser.readXML();
		TestSuiteStruct tsStruct = parser.parseXML();

		System.out.println(tsStruct.getSuiteName());
		ArrayList<TestCaseStruct> list = tsStruct.getList();
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i).getClassName() + "    ");
			System.out.print(list.get(i).getName() + "    ");
			System.out.print(list.get(i).getStatus() + "    ");
			System.out.println(list.get(i).getErrorStackTrace());
		}

		TestLinkModule testLink = new TestLinkModule();
		TestSuite testSuite = null;
		if (!(testLink.isTestSuiteExist(tsStruct.getSuiteName()))) {
			testSuite = testLink.createTestSuite(tsStruct.getSuiteName());
		}

		String buildName = this.getBuildName(buildUrl);
		Build build = null;
		if (!testLink.isBuildExist(buildName)) {
			build = testLink.createBuild(buildName, "Notes of build "
					+ buildName + " .");
		}

		int buildId = -1;
		if (build == null) {
			buildId = testLink.getBuildIdByName(buildName);
		} else {
			buildId = build.getId();
		}

		int testSuiteId = -1;
		if (testSuite == null) {
			testSuiteId = testLink.getTestSuiteByName(tsStruct.getSuiteName())
					.getId();
		} else {
			testSuiteId = testSuite.getId();
		}

		if (testSuiteId != -1) {
			for (int i = 0; i < list.size(); i++) {
				TestCaseStruct tcStruct = list.get(i);
				TestCase testCase = null;
				if (!testLink.isTestCaseExist(testSuiteId, tcStruct.getName())) {
					testCase = testLink.createTestCase(tcStruct.getName(),
							testSuiteId);
				} else {
					testCase = testLink.getTestCaseByName(tcStruct.getName(),
							tsStruct.getSuiteName());
				}
				if (!testLink.isTestCaseAddedToTestPlan(testCase)) {
					testLink.addTestCaseToPlan(testCase.getId());
				}
				testLink.setTestCaseExecutionResult(testCase.getId(),
						testLink.getTestPlanId(), tcStruct.getStatus(),
						buildId, buildName, tcStruct.getErrorStackTrace());
			}
		} else {
			System.out
					.println("The test suite named Parallel isn't exist or no test case belonging to the test suite is added to test plan!");
		}
	}

	public String getBuildName(String url) {
		String[] list = url.split("/");
		for (int i = 0; i < list.length; i++) {
			System.out.println(list[i]);
		}
		return "#" + list[5];
	}
}
