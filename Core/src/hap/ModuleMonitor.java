// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package hap;

import java.io.File;

public class ModuleMonitor {
	public boolean load(File[] modules) {
		for (File m : modules) {
			System.out.println(m.getName());
		}

		return false;
	}
}
