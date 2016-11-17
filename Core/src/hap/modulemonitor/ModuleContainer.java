package hap.modulemonitor;

import java.time.Instant;


public class ModuleContainer {

	public ModuleContainer(Instant startTime) {
		lastLifesign = startTime;
		doNotStartBefore = Instant.now().minusSeconds(1);
	}

	public ModuleContainer(Instant startTime, Process p)
	{
		this( startTime );
		lastLifesign = Instant.now();
		process = p;
	}

	public boolean mayStart()
	{
		return !isActive() && Instant.now().isAfter(doNotStartBefore);
	}

	public boolean hasTerminated() {
		return process != null && !process.isAlive();
	}

	public boolean hasExpired(Instant threshold) {
		return lastLifesign.isBefore(threshold);
	}

	public boolean isActive() {
		return process != null && process.isAlive();
	}

	public Instant lastLifesign;
	public Instant doNotStartBefore;
	public Process process = null;


}
