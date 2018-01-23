package Vectrobe.EnergyInversion.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.info.Info;

import cofh.api.energy.IEnergyHandler;

@cpw.mods.fml.common.Optional.InterfaceList(value = {
        @cpw.mods.fml.common.Optional.Interface(iface = "ic2.api.energy.event.EnergyTileLoadEvent", modid = "IC2"),
        @cpw.mods.fml.common.Optional.Interface(iface = "ic2.api.energy.event.EnergyTileUnloadEvent", modid = "IC2"),
        @cpw.mods.fml.common.Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
        @cpw.mods.fml.common.Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "IC2"),
        @cpw.mods.fml.common.Optional.Interface(iface = "ic2.api.info.Info", modid = "IC2"),

        @cpw.mods.fml.common.Optional.Interface(iface = "cofh.api.energy.IEnergyHandler", modid = "CoFHAPI")
})

public class TileEntityEnergyInterface extends TileEntity implements IEnergySink, IEnergySource, IEnergyHandler
{

    protected boolean CanInterface(ForgeDirection side, EnergyType type, EnergyDir direction)
    {
        return false;
    }

    protected float GetPossibleTransfer(ForgeDirection side, EnergyType type, EnergyDir direction)
    {
        return 0f;
    }

    protected void Transfer(ForgeDirection side, EnergyType type, EnergyDir direction, float value)
    {
        return;
    }

    protected int GetTier(ForgeDirection side, EnergyType type, EnergyDir direction)
    {
        return 0;
    }


    protected final boolean Init()
    {
        if(EnergyAPIs.apiExists(EnergyType.IC2))
            loadEnergyTile();

        return true;
    }

    protected final boolean UnInit()
    {
        if(EnergyAPIs.apiExists(EnergyType.IC2))
            unloadEnergyTile();

        return true;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // IC2
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @cpw.mods.fml.common.Optional.Method(modid = "IC2")
    private void loadEnergyTile()
    {
        if (Info.isIc2Available())
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
    }

    @cpw.mods.fml.common.Optional.Method(modid = "IC2")
    private void unloadEnergyTile()
    {
        if (Info.isIc2Available())
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
    }

    // sink
    @cpw.mods.fml.common.Optional.Method(modid = "IC2")
    @Override
    public final double getDemandedEnergy()
    {
        return (double) GetPossibleTransfer(ForgeDirection.UNKNOWN, EnergyType.IC2, EnergyDir.RECEIVE);
    }

    @cpw.mods.fml.common.Optional.Method(modid = "IC2")
    @Override
    public final double injectEnergy(ForgeDirection side, double amount, double voltage)
    {
        Transfer(side, EnergyType.IC2, EnergyDir.RECEIVE, (float)amount);
        return 0;
    }

    @cpw.mods.fml.common.Optional.Method(modid = "IC2")
    @Override
    public final int getSinkTier()
    {
        return Integer.MAX_VALUE;
    }

    @cpw.mods.fml.common.Optional.Method(modid = "IC2")
    @Override
    public final boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection side)
    {
        return CanInterface(side, EnergyType.IC2, EnergyDir.RECEIVE);
    }

    // source
    @cpw.mods.fml.common.Optional.Method(modid = "IC2")
    @Override
    public final boolean emitsEnergyTo(TileEntity receiver, ForgeDirection side)
    {
        return CanInterface(side, EnergyType.IC2, EnergyDir.SEND);
    }

    @cpw.mods.fml.common.Optional.Method(modid = "IC2")
    @Override
    public final double getOfferedEnergy()
    {
        return (double) GetPossibleTransfer(ForgeDirection.UNKNOWN, EnergyType.IC2, EnergyDir.SEND);
    }

    @cpw.mods.fml.common.Optional.Method(modid = "IC2")
    @Override
    public final void drawEnergy(double amount)
    {
        Transfer(ForgeDirection.UNKNOWN, EnergyType.IC2, EnergyDir.SEND, (float)amount);
    }

    @cpw.mods.fml.common.Optional.Method(modid = "IC2")
    @Override
    public int getSourceTier()
    {
        return 0;
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // COFH
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @cpw.mods.fml.common.Optional.Method(modid = "CoFHAPI")
    @Override
    public final int receiveEnergy(ForgeDirection side, int maxEnergy, boolean simulate)
    {
        int energy = Math.min( (int)GetPossibleTransfer(side, EnergyType.COFH, EnergyDir.SEND), maxEnergy);

        if(!simulate) Transfer(side, EnergyType.COFH, EnergyDir.SEND, (float) energy);

        return energy;
    }

    @cpw.mods.fml.common.Optional.Method(modid = "CoFHAPI")
    @Override
    public final int extractEnergy(ForgeDirection side, int maxEnergy, boolean simulate)
    {
        int energy = Math.min( (int)GetPossibleTransfer(side, EnergyType.COFH, EnergyDir.SEND), maxEnergy);

        if(!simulate) Transfer(side, EnergyType.COFH, EnergyDir.SEND, (float) energy);

        return energy;
    }

    @cpw.mods.fml.common.Optional.Method(modid = "CoFHAPI")
    @Override
    public final boolean canConnectEnergy(ForgeDirection side)
    {
        return CanInterface(side, EnergyType.COFH, EnergyDir.BIDIR);
    }

    @cpw.mods.fml.common.Optional.Method(modid = "CoFHAPI")
    @Override
    public final int getEnergyStored(ForgeDirection side)
    {
        return (int) GetPossibleTransfer(side, EnergyType.COFH, EnergyDir.BIDIR);
    }

    @cpw.mods.fml.common.Optional.Method(modid = "CoFHAPI")
    @Override
    public final int getMaxEnergyStored(ForgeDirection side)
    {
        return (int) GetPossibleTransfer(side, EnergyType.COFH, EnergyDir.BIDIR);
    }

}
