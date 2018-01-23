package Vectrobe.EnergyInversion.energy;

public class EnergyAPIs
{
    public static boolean apiExists(EnergyType type)
    {
        try
        {
            switch(type)
            {
                case IC2:
                    return apiIC2();
                case COFH:
                    return apiCOFH();
                default:
                    return false;
            }
        }
        catch(NoSuchMethodError e)
        {
            return false;
        }
    }

    @cpw.mods.fml.common.Optional.Method(modid = "IC2")
    private static boolean apiIC2() { return true; }

    @cpw.mods.fml.common.Optional.Method(modid = "CoFHAPI")
    private static boolean apiCOFH() { return true; }

}
