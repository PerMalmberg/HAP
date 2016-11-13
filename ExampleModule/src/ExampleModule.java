// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

import hap.basemodule.IHAPModule;
import hap.basemodule.ModuleRunner;

public class ExampleModule implements IHAPModule {

	public static void main(String... args) {
		ModuleRunner mr = new ModuleRunner(new ExampleModule());

		if (mr.initialize(args)) {
			System.exit(mr.run() ? 0 : 1);
		} else {
			System.exit(1);
		}
	}

	@Override
	public void tick() {

	}
}
