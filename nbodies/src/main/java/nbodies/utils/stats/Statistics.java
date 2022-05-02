package nbodies.utils.stats;

public class Statistics {

	private double tmpMin = 0;
	private double tmpMax = 0;
	private double tmpAvg = 0;
	private int nElements = 0;

	public void add(double newValue) {
		if (nElements == 0) {
			tmpMin = newValue;
			tmpMax = newValue;
			tmpAvg = newValue;
		} else {
			tmpMin = Math.min(tmpMin, newValue);
			tmpMax = Math.max(tmpMax, newValue);
			tmpAvg = (tmpAvg * nElements + newValue) / (nElements + 1);
		}
		nElements++;
	}

	public double getMin() {
		return tmpMin;
	}

	public double getMax() {
		return tmpMax;
	}

	public double getAvg() {
		return tmpAvg;
	}
}
