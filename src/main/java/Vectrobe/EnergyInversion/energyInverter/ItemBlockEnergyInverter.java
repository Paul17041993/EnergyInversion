package Vectrobe.EnergyInversion.energyInverter;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemBlockEnergyInverter extends ItemBlockWithMetadata
{
    public ItemBlockEnergyInverter(Block block)
    {
        super(block, block);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata (int damageValue) { return damageValue; }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return getUnlocalizedName() + "_" +
                TileEntityEnergyInverter.InverterTier.values()[itemstack.getItemDamage()].name().toLowerCase();
    }
}
