package Vectrobe.EnergyInversion.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderHandlers
{
    public static final ISimpleBlockRenderingHandler renderEnergyInverter = new RenderEnergyInverter();

    public static void register()
    {
        RenderingRegistry.registerBlockHandler(renderEnergyInverter);
    }
}
