package Vectrobe.EnergyInversion.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class Network
{
    public static SimpleNetworkWrapper instance;

    public static void Init()
    {
        instance = NetworkRegistry.INSTANCE.newSimpleChannel("EnergyInversion");
        instance.registerMessage(MessageGuiEvent.Handler.class, MessageGuiEvent.class, 0, Side.SERVER);
    }
}
