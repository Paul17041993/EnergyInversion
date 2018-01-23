package Vectrobe.EnergyInversion.energy;

public enum EnergyType{
    NONE(0),
    IC2(1),
    COFH(2);
    public final byte value;
    private EnergyType(int val)
    { value = (byte) val; }

    public static final EnergyType Types[] = {NONE,IC2,COFH};

    public static EnergyType[] FromBytes(byte bytes[], int size)
    {
        EnergyType ret[] = new EnergyType[size];
        for(int i = 0; i < size; ++i)
            ret[i] = Types[bytes[i]];
        return ret;
    }

    public static byte[] ToBytes(EnergyType types[], int size)
    {
        byte ret[] = new byte[size];
        for(int i = 0; i < size; ++i)
            ret[i] = types[i].value;
        return ret;
    }
}
