package Vectrobe.EnergyInversion;

//import ic2.api.item.Items;
import Vectrobe.EnergyInversion.client.RenderHandlers;
import Vectrobe.EnergyInversion.energyInverter.BlockEnergyInverter;
import Vectrobe.EnergyInversion.energyInverter.ItemBlockEnergyInverter;
import Vectrobe.EnergyInversion.energyInverter.TileEntityEnergyInverter;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Objects {
	
	//tabs
	public static final CreativeTabs mainTab = new EnergyInversionTab();
	
	//blocks
	public static Block BlockEnergyInverter;//[] = new Block[TileEntityEnergyInverter.InverterTier.TIERS.value];
	
	//tiles
	
	
	public static void Init(FMLPreInitializationEvent event) throws Exception
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		
		config.load();
		
		//power config
		//UniPower.LoadConfig(config);
		
		//inverter block
		Material energyBlockMat = new Material(null);

		config.save();
		
		//for(int i = 0; i < TileEntityEnergyInverter.InverterTier.TIERS.value; ++i)
            BlockEnergyInverter = new BlockEnergyInverter(energyBlockMat); //,TileEntityEnergyInverter.InverterTier.values()[i]);
		
	}
	
	public static void Load(FMLInitializationEvent event) throws Exception
	{

        //for(int i = 0; i < TileEntityEnergyInverter.InverterTier.TIERS.value; ++i)
			GameRegistry.registerBlock(BlockEnergyInverter, ItemBlockEnergyInverter.class, BlockEnergyInverter.getUnlocalizedName());
		
		GameRegistry.registerTileEntity(TileEntityEnergyInverter.class, "EnergyInversion.inverter_tile");

		
		//recipes

		//GameRegistry.addRecipe(new ShapedOreRecipe(BlockEnergyInverter[0], true,
		//		new Object[]{ "WRW", "CIC", "WRW",
		//		Character.valueOf('W'), "plankWood",
		//		Character.valueOf('C'), "ingotCopper",
		//		Character.valueOf('R'), "dustRedstone",
		//		Character.valueOf('I'), "ingotIron"}));

		//GameRegistry.addRecipe(new ShapedOreRecipe(BlockEnergyInverter[1], true,
		//		new Object[]{ "IRI", "BIB", "IRI",
		//		Character.valueOf('B'), "ingotBronze",
		//		Character.valueOf('R'), "dustRedstone",
		//		Character.valueOf('I'), "ingotIron"}));

		//GameRegistry.addRecipe(new ShapedOreRecipe(BlockEnergyInverter[2], true,
		//		new Object[]{ "IRI", "EIE", "IRI",
		//		Character.valueOf('E'), "ingotElectrum",
		//		Character.valueOf('R'), "dustRedstone",
		//		Character.valueOf('I'), "ingotIron"}));
		//
		//
		//GameRegistry.addRecipe(new ShapedOreRecipe(BlockEnergyInverter[3], true,
		//		new Object[]{ "ILI", "GRG", "ILI",
		//		Character.valueOf('I'), "ingotIron",
		//		Character.valueOf('G'), "ingotGold",
		//		Character.valueOf('L'), "ingotLead",
		//		Character.valueOf('R'), "blockRedstone"}));

		//GameRegistry.addRecipe(new ItemStack(BlockEnergyInverter[4]),
		//		"RBR", "BLB", "RBR",
		//		'B', new ItemStack(BlockEnergyInverter[3]),
		//		'R', new ItemStack(Block.getBlockFromName("blockRedstone")),
		//		'L', new ItemStack(Block.getBlockFromName("blockLapis")));

	}

}
