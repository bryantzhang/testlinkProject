package com.ebay.renzhang.testlinkProject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ActionOnDuplicate;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseDetails;
import br.eti.kinoshita.testlinkjavaapi.constants.TestImportance;
import br.eti.kinoshita.testlinkjavaapi.model.Attachment;
import br.eti.kinoshita.testlinkjavaapi.model.Build;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;
import br.eti.kinoshita.testlinkjavaapi.model.TestSuite;

public class TestLinkModule {
	String PROJECTNAME = "PAAS_Staging_Test";
	String PLANNAME = "staging_test_plan";
	TestLinkAPI testlink;

	public TestLinkModule() {
		this.connect();
	}

	public void connect() {
		URL myTestLinkServer = null;
		try {
			myTestLinkServer = new URL(
					"http://10.249.64.238/testlink/lib/api/xmlrpc.php");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		String myDevKey = "14003fc88aa083f6ea1a11655a70c69a";
		this.testlink = new TestLinkAPI(myTestLinkServer, myDevKey);
	}

	public TestSuite getTestSuiteByName(String suiteName) {
		TestSuite[] testSuites = this.testlink.getTestSuitesForTestPlan(this
				.getTestPlanId());
		TestSuite testSuite = null;
		for (int i = 0; i < testSuites.length; i++) {
			if (testSuites[i].getName().equals(suiteName)) {
				testSuite = testSuites[i];
				break;
			}
		}

		return testSuite;
	}

	public TestSuite createTestSuite(String suiteName) {
		String suiteDetails = "Details about Test Suite " + suiteName + " .";

		return this.testlink
				.createTestSuite(this.getTestProjectId(), suiteName,
						suiteDetails, null, 0, true, ActionOnDuplicate.BLOCK);
	}

	public TestCase createTestCase(String testCaseName, int testSuiteId) {
		List<TestCaseStep> steps = new ArrayList<TestCaseStep>();
		TestCaseStep step = new TestCaseStep();
		step.setNumber(1);
		step.setExpectedResults("Expected Results!");
		step.setExecutionType(ExecutionType.MANUAL);
		step.setActions("Actions!");
		steps.add(step);

		return this.testlink.createTestCase(testCaseName, testSuiteId,
				this.getTestProjectId(), "admin", "Summary about Test Case "
						+ testCaseName + "!", steps, " ",
				TestImportance.MEDIUM, ExecutionType.MANUAL, 0, null, true,
				ActionOnDuplicate.BLOCK);
	}

	public void addTestCaseToPlan(int testCaseId) {
		this.testlink.addTestCaseToTestPlan(this.getTestProjectId(),
				this.getTestPlanId(), testCaseId, 1, null, 1, null);
	}

	public void setTestCaseExecutionResult(int testCaseId, int testPlanId,
			String status, int buildId, String buildName, String bugs) {
		if (status.equals("PASSED")||status.equals("FIXED")) {
			this.testlink.setTestCaseExecutionResult(testCaseId, null,
					testPlanId, ExecutionStatus.PASSED, buildId, buildName,
					bugs, false, "Bug ID", -1, "Platform name",
					new HashMap<String, String>(), false);
		} else if (status.equals("FAILED") || status.equals("REGRESSION")) {
			this.testlink.setTestCaseExecutionResult(testCaseId, null,
					testPlanId, ExecutionStatus.FAILED, buildId, buildName,
					bugs, false, "Bug ID", -1, "Platform name",
					new HashMap<String, String>(), false);
		} else if (status.equals("SKIPPED")) {
			this.testlink.setTestCaseExecutionResult(testCaseId, null,
					testPlanId, ExecutionStatus.NOT_RUN, buildId, buildName,
					bugs, false, "Bug ID", -1, "Platform name",
					new HashMap<String, String>(), false);
		}
	}

	public Build createBuild(String buildName, String buildNotes) {
		return this.testlink.createBuild(this.getTestPlanId(), buildName,
				buildNotes);
	}

	public int getTestProjectId() {
		TestProject[] testProjects = this.testlink.getProjects();
		TestProject testProject = null;
		for (int i = 0; i < testProjects.length; i++) {
			if (testProjects[i].getName().equals(PROJECTNAME)) {
				testProject = testProjects[i];
				break;
			}
		}

		return testProject.getId();
	}

	public int getTestPlanId() {
		return this.testlink.getTestPlanByName(PLANNAME, PROJECTNAME).getId();
	}

	public int getBuildIdByName(String buildName) {
		int buildId = -1;
		Build[] builds = this.testlink.getBuildsForTestPlan(this
				.getTestPlanId());
		for (int i = 0; i < builds.length; i++) {
			if (builds[i].getName().equals(buildName)) {
				buildId = builds[i].getId();
			}
		}

		return buildId;
	}

	public int getTestCaseIdByName(String testCaseName, String testSuiteName) {
		TestCase[] testCases = this.testlink.getTestCasesForTestSuite(this
				.getTestSuiteByName(testSuiteName).getId(), true,
				TestCaseDetails.FULL);
		int testCaseId = -1;
		for (int i = 0; i < testCases.length; i++) {
			if (testCases[i].getName().equals(testCaseName)) {
				testCaseId = testCases[i].getId();
				break;
			}
		}

		return testCaseId;
	}

	public TestCase getTestCaseByName(String testCaseName, String testSuiteName) {
		TestCase[] testCases = this.testlink.getTestCasesForTestSuite(this
				.getTestSuiteByName(testSuiteName).getId(), true,
				TestCaseDetails.FULL);
		TestCase testCase = null;
		for (int i = 0; i < testCases.length; i++) {
			if (testCases[i].getName().equals(testCaseName)) {
				testCase = testCases[i];
				break;
			}
		}

		return testCase;
	}

	public boolean isBuildExist(String buildName) {
		boolean flag = false;
		Build[] builds = this.testlink.getBuildsForTestPlan(this
				.getTestPlanId());
		if (builds != null) {
			for (int i = 0; i < builds.length; i++) {
				if (builds[i].getName().equals(buildName)) {
					flag = true;
					break;
				}
			}
		}

		return flag;
	}

	public boolean isTestSuiteExist(String suiteName) {
		boolean flag = false;
		TestSuite[] testSuites = this.testlink.getTestSuitesForTestPlan(this
				.getTestPlanId());
		if (testSuites != null) {
			for (int i = 0; i < testSuites.length; i++) {
				if (testSuites[i].getName().equals(suiteName)) {
					flag = true;
					break;
				}
			}
		}

		return flag;
	}

	public boolean isTestCaseExist(int testSuiteId, String caseName) {
		boolean flag = false;
		TestCase[] testCases = this.testlink.getTestCasesForTestSuite(
				testSuiteId, true, TestCaseDetails.FULL);
		if (testCases != null) {
			for (int i = 0; i < testCases.length; i++) {
				if (testCases[i].getName().equals(caseName)) {
					flag = true;
					break;
				}
			}
		}

		return flag;
	}

	public boolean isTestCaseAddedToTestPlan(TestCase testCase) {
		boolean flag = false;
		ExecutionStatus status = testCase.getExecutionStatus();
		System.out.println(status);
		if ((status == ExecutionStatus.BLOCKED)
				|| (status == ExecutionStatus.FAILED)
				|| (status == ExecutionStatus.PASSED)
				|| (status == ExecutionStatus.NOT_RUN))
			flag = true;

		return flag;
	}

	public void uploadAttactment(String fileName, int executionId) {
		File attachmentFile = new File(fileName);
		String fileContent = null;

		try {
			byte[] byteArray = FileUtils.readFileToByteArray(attachmentFile);
			fileContent = new String(Base64.encodeBase64(byteArray));
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(-1);
		}

		Attachment attachment = this.testlink.uploadExecutionAttachment(
				executionId, // executionId
				"Setting customer plan", // title
				"In this screen the attendant is defining the customer plan", // description
				"screenshot_customer_plan_" + System.currentTimeMillis()
						+ ".jpg", // fileName
				"image/jpeg", // fileType
				fileContent); // content

		System.out.println("Attachment uploaded");
	}
}
