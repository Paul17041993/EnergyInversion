package Vectrobe.EnergyInversion.energyInverter;


import Vectrobe.EnergyInversion.EnergyInversion;
import Vectrobe.EnergyInversion.Objects;
import Vectrobe.EnergyInversion.client.RenderEnergyInverter;
import Vectrobe.EnergyInversion.energy.EnergyDir;
import Vectrobe.EnergyInversion.energy.EnergyType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class BlockEnergyInverter extends BlockContainer
{
    //private TileEntityEnergyInverter.InverterTier tier;

    public BlockEnergyInverter(Material material)//, TileEntityEnergyInverter.InverterTier tier)
    {
        super(material);

        //this.tier = tier;

        setHardness(2.0F);
        setBlockName("energyinversion.inverter");//_" + this.tier.name().toLowerCase());

        setCreativeTab(Objects.mainTab);
    }


    @Override
    public int damageDropped (int damageValue) { return damageValue; }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_)
    {
        try
        {
            return new TileEntityEnergyInverter();//this.tier);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess iBlockAccess,int x,int y,int z,int tileX,int tileY,int tileZ)
    {
        TileEntity tile = iBlockAccess.getTileEntity(x, y, z);
        if(! (tile instanceof TileEntityEnergyInverter) ) return;
        //((TileEntityEnergyInverter) tile).updateNeighbourTile(tileX,tileY,tileZ);
        //((TileEntityPowerInverter) tile).setDirtyHandles();

        super.onNeighborChange(iBlockAccess, x, y, z, tileX, tileY, tileZ);
    }

    @Override
    public void onNeighborBlockChange(World world,int x,int y,int z,Block block)
    {
        //TileEntity tile = world.getTileEntity(x, y, z);
        //if(! (tile instanceof TileEntityEnergyInverter) ) return;
        //((TileEntityEnergyInverter) tile).setDirtyHandles();

        super.onNeighborBlockChange(world,x,y,z,block);
    }


    @SideOnly(Side.CLIENT)
    protected static IIcon[] blockIcons;

    @SideOnly(Side.CLIENT)
    protected static IIcon iconIC2r;
    @SideOnly(Side.CLIENT)
    protected static IIcon iconIC2s;
    @SideOnly(Side.CLIENT)
    protected static IIcon iconCOFHr;
    @SideOnly(Side.CLIENT)
    protected static IIcon iconCOFHs;


    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta)
    {
        if(meta < TileEntityEnergyInverter.InverterTier.TIERS.value)
            return blockIcons[meta];

        return this.blockIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess iBlockAccess, int x, int y, int z, int side)
    {
        int meta = iBlockAccess.getBlockMetadata(x,y,z);
        if(meta < TileEntityEnergyInverter.InverterTier.TIERS.value)
            return blockIcons[meta];

        return this.blockIcon;
    }

    @SideOnly(Side.CLIENT)
    public static IIcon GetSideIcon(EnergyDir dir, EnergyType type)
    {
        switch(dir)
        {
            case RECEIVE:
                switch (type)
                {
                    case IC2: return iconIC2r;
                    case COFH: return iconCOFHr;
                    default: return null;
                }
            case SEND:
                switch (type)
                {
                    case IC2: return iconIC2s;
                    case COFH: return iconCOFHs;
                    default: return null;
                }
            default: return null;
        }
    }


    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTab, List list)
    {
        for(int i = 0; i < TileEntityEnergyInverter.InverterTier.TIERS.value; ++i)
            list.add(new ItemStack(item, 1, i));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcons = new IIcon[TileEntityEnergyInverter.InverterTier.TIERS.value];
        for(int i = 0; i < TileEntityEnergyInverter.InverterTier.TIERS.value; ++i)
            blockIcons[i] = iconRegister.registerIcon("energyinversion:inverter_" +
                    TileEntityEnergyInverter.InverterTier.values()[i].name().toLowerCase());

        iconIC2r = iconRegister.registerIcon("energyinversion:ic2_in");
        iconIC2s = iconRegister.registerIcon("energyinversion:ic2_out");
        iconCOFHr = iconRegister.registerIcon("energyinversion:cofh_in");
        iconCOFHs = iconRegister.registerIcon("energyinversion:cofh_out");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderType() { return RenderEnergyInverter.renderID; }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canRenderInPass(int pass)
    {
        RenderEnergyInverter.renderPass = pass;
        return pass == 0 || pass == 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }


    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
        int side = 0;

        int dirV = MathHelper.floor_double((double) ((entity.rotationPitch * 4F) / 360F) + 0.5D) & 3;

        switch(dirV)
        {
            case 1:
                side = 0;
                break;
            case 3:
                side = 1;
                break;

            default:

                int dirH = MathHelper.floor_double((double)((entity.rotationYaw * 4F) / 360F) + 0.5D) & 3;

                switch(dirH)
                {
                    case 0:
                        side = 3;
                        break;
                    case 1:
                        side = 4;
                        break;
                    case 2:
                        side = 2;
                        break;
                    case 3:
                        side = 5;
                        break;
                    default:
                        break;
                }

                break;
        }

        TileEntity tile = world.getTileEntity(x, y, z);

        if(! (tile instanceof TileEntityEnergyInverter) ) return;

        //((TileEntityEnergyInverter) tile).SetTier(this.tier);
        ((TileEntityEnergyInverter) tile).SetSidesFromOrientation(side);


    }


    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float what, float these, float are)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null)
            return false;

        player.openGui(EnergyInversion.instance, 0, world, x, y, z);

        return true;
    }
}
