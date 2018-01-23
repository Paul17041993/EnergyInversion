package Vectrobe.EnergyInversion.gui;

import Vectrobe.EnergyInversion.energyInverter.TileEntityEnergyInverter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class Gui
{
    public static final int ENERGY_INVERTER = 0;

    public static Object getClientGui( int id, EntityPlayer player, World world, int x, int y, int z )
    {
        switch(id)
        {
            case ENERGY_INVERTER:
                return new GuiEnergyInverter((TileEntityEnergyInverter) world.getTileEntity(x, y, z));

            default: return null;
        }
    }
}
