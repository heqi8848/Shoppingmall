package cloud.simple.model;

public class AutoTestInfo {
	Integer testSwitch;
	Integer period;
	
	public AutoTestInfo()
	{
		
	}
	public AutoTestInfo(Integer testSwitch, Integer period) {
		super();
		this.testSwitch = testSwitch;
		this.period = period;
	}
	public Integer getTestSwitch() {
		return testSwitch;
	}
	public void setTestSwitch(Integer testSwitch) {
		this.testSwitch = testSwitch;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
}
