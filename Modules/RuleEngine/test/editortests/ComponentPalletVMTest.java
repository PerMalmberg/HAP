package editortests;


import hap.ruleengine.editor.viewmodel.ComponentPallet;
import hap.ruleengine.editor.viewmodel.NativeCategory;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ComponentPalletVMTest
{
	private final ComponentPallet pallet = new ComponentPallet();

	@Test
	public void LoadComponentTest()
	{
		assertTrue( pallet.loadComponents() );

		List<NativeCategory> cat = pallet.getCategories();

		assertEquals( 4, cat.size() );
	}
}
