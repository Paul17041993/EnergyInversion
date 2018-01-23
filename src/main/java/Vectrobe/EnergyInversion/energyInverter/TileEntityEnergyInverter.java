package Vectrobe.EnergyInversion.energyInverter;

import Vectrobe.EnergyInversion.energy.EnergyDir;
import Vectrobe.EnergyInversion.energy.EnergyType;
import Vectrobe.EnergyInversion.energy.TileEntityEnergyInterface;
import Vectrobe.EnergyInversion.gui.GuiEnergyInverter;
import Vectrobe.EnergyInversion.gui.GuiGauge;
import Vectrobe.EnergyInversion.network.GuiEventInterface;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.network.NetworkHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.IIcon;

public class TileEntityEnergyInverter extends TileEntityEnergyInterface implements GuiEventInterface
{
    public enum InverterTier{
        WHITE(0),
        BRONZE(1),
        SILVER(2),
        GOLD(3),
        PLATINUM(4),
        TITANIUM(5),
        TIERS(6);
        public final byte value;
        private InverterTier(int val)
        { value = (byte) val; }

        public static final InverterTier Tiers[] = {WHITE,BRONZE,SILVER,GOLD,PLATINUM,TITANIUM};

        public static InverterTier[] FromBytes(byte bytes[], int size)
        {
            InverterTier ret[] = new InverterTier[size];
            for(int i = 0; i < size; ++i)
                ret[i] = Tiers[bytes[i]];
            return ret;
        }

        public static byte[] ToBytes(InverterTier Tiers[], int size)
        {
            byte ret[] = new byte[size];
            for(int i = 0; i < size; ++i)
                ret[i] = Tiers[i].value;
            return ret;
        }
    }

    public class Side {
        EnergyDir direction;
        EnergyType type;
        float storePeak;
        float storeCurrent = 0f;

        @SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
        public IIcon GetIIcon() {
            if (direction == null || type == null) return null;
            return BlockEnergyInverter.GetSideIcon(direction, type);
        }

        @SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
        public void SetColour(GuiGauge.Colour colour)
        {
            if(type == null)
            {
                colour.SetColour(0,0,0);
                return;
            }
            switch (type)
            {
                case IC2: colour.SetColour(0,1,1); break;
                case COFH: colour.SetColour(1,0,0); break;
                default: colour.SetColour(1,1,1); break;
            }
        }

        public void SwitchDirection() {
            if (direction == null) return;
            switch (direction) {
                case RECEIVE: direction = EnergyDir.SEND; break;
                case SEND: direction = EnergyDir.NONE; break;
                case NONE: direction = EnergyDir.RECEIVE; break;
                default: break;
            }
        }

        public void SwitchDirectionBack() {
            if (direction == null) return;
            switch (direction) {
                case RECEIVE: direction = EnergyDir.NONE; break;
                case SEND: direction = EnergyDir.RECEIVE; break;
                case NONE: direction = EnergyDir.SEND; break;
                default: break;
            }
        }

        public void SwitchType()
        {
            if(type == null) return;
            switch(type)
            {
                case IC2: type = EnergyType.COFH; break;
                case COFH: type = EnergyType.IC2; break;
                default: break;
            }
        }

        public float GetPeak()
        {return storePeak;}
        public float GetCurrent()
        {return storeCurrent;}

        void writeNBT(NBTTagCompound nbt, int id)
        {
            nbt.setByte("SideDir" + id, direction.value);
            nbt.setByte("SideType" + id, type.value);
            nbt.setFloat("SideStorePeak" + id, storePeak);
            nbt.setFloat("SideStoreCurr" + id, storeCurrent);
        }
        void readNBT(NBTTagCompound nbt, int id)
        {
            direction = EnergyDir.values()[nbt.getByte("SideDir" + id)];
            type = EnergyType.values()[nbt.getByte("SideType" + id)];
            storePeak = nbt.getFloat("SideStorePeak" + id);
            storeCurrent = nbt.getFloat("SideStoreCurr" + id);
        }
    }

    Side[] sides = {new Side(),new Side(),new Side(),new Side(),new Side(),new Side()};

    public Side[] GetSides() { return sides; }

    @SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
    public IIcon GetBaseIIcon(int side) { return getBlockType().getIcon(side,getBlockMetadata()); }

    public void RefreshBlock()
    {
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
        worldObj.markTileEntityChunkModified(xCoord,yCoord,zCoord,this);
        markDirty();
    }

    public TileEntityEnergyInverter()
    {
        super();

        for(int i = 0; i < 6; ++i) {
            sides[i].direction = EnergyDir.NONE;
            sides[i].type = EnergyType.IC2;
            sides[i].storePeak = 1f;
        }
    }



    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        for(int i = 0; i < 6; ++i)
            sides[i].writeNBT(nbt, i);

    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        for(int i = 0; i < 6; ++i)
            sides[i].readNBT(nbt, i);

        if(worldObj != null)
            worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
    }


    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.func_148857_g());
        RefreshBlock();
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tagCompound);
    }

    public void buttonAction(int id, int mb, boolean release)
    {
        int subid = id & 255;

        try {
            switch (id & GuiEnergyInverter.BUTTON_GROUP) {
                case 0:
                    break;
                case GuiEnergyInverter.BUTTON_GROUP_FACE_DIR:
                    if(mb == 0)
                        sides[subid].SwitchDirection();
                    else if(mb == 1)
                        sides[subid].SwitchDirectionBack();
                    break;
                case GuiEnergyInverter.BUTTON_GROUP_FACE_TYPE:
                    if(release)
                        sides[subid].SwitchType();
                    break;
                default:
                    FMLLog.warning("EnergyInversion; A Button ID in the EnergyInverter was badly masked!\n");
                    break;
            }
        }
        catch (Exception e)
        {
            FMLLog.warning("EnergyInversion: An error occurred in the EnergyInverter GUI actionPerformed!\n" + e.getMessage());
        }

        RefreshBlock();
    }

    public void buttonAction(int id, float val)
    {
        FMLLog.info(val+"");

        int subid = id & 255;

        try {
            switch (id & GuiEnergyInverter.BUTTON_GROUP) {
                case 0:
                    break;
                case GuiEnergyInverter.BUTTON_GROUP_FACE_TYPE:
                        sides[subid].storePeak = Math.min(1f, Math.max(0f, val) );
                    break;
                default:
                    FMLLog.warning("EnergyInversion; A Slider ID in the EnergyInverter was badly masked!\n");
                    break;
            }
        }
        catch (Exception e)
        {
            FMLLog.warning("EnergyInversion: An error occurred in the EnergyInverter GUI actionPerformed! (slider)\n" + e.getMessage());
        }

        RefreshBlock();
    }


    public void SetSidesFromOrientation(int orientation)
    {
        //sides[orientation] = SideType.RECEIVE;
//
        ////we want the tier 0 one to accept from all sides
        //if(this.tier == 0)
        //    for(int i = 0; i < 6; ++i)
        //        sides[i] = SideType.RECEIVE;
//
        //sides[ForgeDirection.OPPOSITES[orientation]] = SideType.SEND;
    }
}
