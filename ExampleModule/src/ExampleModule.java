import hap.basemodule.BaseModule;
import hap.basemodule.IHAPModule;
import hap.basemodule.ModuleRunner;

public class ExampleModule extends BaseModule implements IHAPModule {
	public static void main(String... args)
	{
		ModuleRunner mr = new ModuleRunner( new ExampleModule(), args );
		mr.run();
	}

	@Override
	public void tick() {

	}
}
