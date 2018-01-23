package Vectrobe.EnergyInversion.energy;

public enum EnergyDir{
    NONE(0),
    RECEIVE(1),
    SEND(2),
    BIDIR(3);
    public final byte value;
    private EnergyDir(int val)
    { value = (byte) val; }

    public static final EnergyDir Directions[] = {NONE,RECEIVE,SEND,BIDIR};

    public static EnergyDir[] FromBytes(byte bytes[], int size)
    {
        EnergyDir ret[] = new EnergyDir[size];
        for(int i = 0; i < size; ++i)
            ret[i] = Directions[bytes[i]];
        return ret;
    }

    public static byte[] ToBytes(EnergyDir dirs[], int size)
    {
        byte ret[] = new byte[size];
        for(int i = 0; i < size; ++i)
            ret[i] = dirs[i].value;
        return ret;
    }
}
