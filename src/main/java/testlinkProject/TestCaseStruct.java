package testlinkProject;

public class TestCaseStruct {
	String className;
	String name;
	String status;
	String errorStackTrace;

	public TestCaseStruct(String className, String name, String status,
			String errorStackTrace) {
		super();
		this.className = className;
		this.name = name;
		this.status = status;
		this.errorStackTrace = errorStackTrace;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorStackTrace() {
		return errorStackTrace;
	}

	public void setErrorStackTrace(String errorStackTrace) {
		this.errorStackTrace = errorStackTrace;
	}
}
