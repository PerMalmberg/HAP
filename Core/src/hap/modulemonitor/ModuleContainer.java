package hap.modulemonitor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


public class ModuleContainer {

	public ModuleContainer(Instant startTime) {
		lastLifesign = startTime;
		myDoNotStartBefore = Instant.now().minusSeconds(1);
	}

	public ModuleContainer(Instant startTime, Process p) {
		this(startTime);
		lastLifesign = Instant.now();
		myProcess = p;
	}

	public boolean mayStart() {
		return myMayAutostart && !isActive() && Instant.now().isAfter(myDoNotStartBefore);
	}

	public boolean hasTerminated() {
		return myProcess != null && !myProcess.isAlive();
	}

	public boolean hasExpired(Instant threshold) {
		return lastLifesign.isBefore(threshold);
	}

	public boolean isActive() {
		return myProcess != null && myProcess.isAlive();
	}


	public void terminate() {
		if( myProcess != null ) {
			myProcess.destroyForcibly();
			myProcess = null;
			delayStart();
		}
	}

	public void delayStart() {
		myProcess = null;
		myDoNotStartBefore = Instant.now().plus(15, ChronoUnit.MINUTES);
	}

	public void setProcess(Process process) {
		myProcess = process;
		lastLifesign = Instant.now();
	}

	public void setIsAlive() {
		lastLifesign = Instant.now();
	}

	private Instant lastLifesign;
	private Instant myDoNotStartBefore;
	private Process myProcess = null;
	private boolean myMayAutostart = true;

	public void disableAutoStart() {
		myMayAutostart = false;
	}

	public void enableAutoStart() {
		myMayAutostart = true;
		myDoNotStartBefore = Instant.now().minusSeconds(1); // Ensure immediate start
	}
}
