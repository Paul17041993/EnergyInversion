package Vectrobe.EnergyInversion;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EnergyInversionTab extends CreativeTabs
{

	public EnergyInversionTab() {
		super("EnergyInversion");
	}

    @Override
    public Item getTabIconItem() {
        return new ItemStack(Objects.BlockEnergyInverter).getItem();
    }
}
