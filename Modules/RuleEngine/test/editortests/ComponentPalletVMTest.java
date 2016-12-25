package editortests;


import hap.ruleengine.editor.viewmodel.ComponentPallet;
import hap.ruleengine.editor.viewmodel.NativeCategory;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ComponentPalletVMTest
{
	private final ComponentPallet pallet = new ComponentPallet();

	@Test
	public void LoadComponentTest()
	{
		assertTrue( pallet.loadComponents() );

		HashMap<String, NativeCategory> cat = pallet.getPallet();

		assertEquals( 4, cat.size() );
	}
}
