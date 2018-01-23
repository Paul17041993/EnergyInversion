package Vectrobe.EnergyInversion;


import Vectrobe.EnergyInversion.client.RenderHandlers;
import Vectrobe.EnergyInversion.network.Network;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid="EnergyInversion", name="EnergyInversion", version="0.0.2", acceptableRemoteVersions="0.0.2")
public class EnergyInversion {
	
	@Instance(value = "EnergyInversion")
	public static EnergyInversion instance;
	
	@SidedProxy(clientSide="Vectrobe.EnergyInversion.client.ClientProxy", serverSide="Vectrobe.EnergyInversion.CommonProxy")
	public static CommonProxy proxy;
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) throws Exception
	{
        Network.Init();
		Objects.Init(event);
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) throws Exception
	{
		proxy.registerRenderers();
        RenderHandlers.register();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		Objects.Load(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) throws Exception
	{
	}
}
