package Vectrobe.EnergyInversion.network;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MessageGuiEvent implements IMessage
{

    public enum Event
    {
        BUTTON_PRESS(0),
        PERCENT_SET(1);
        public final byte value;
        private Event(int val)
        { value = (byte) val; }

        public static final Event Directions[] = {};
    }

    int x,y,z;
    Event event;
    int id;
    int mb;
    boolean release;
    float floatVal;

    public MessageGuiEvent() { }

    public MessageGuiEvent(TileEntity tile, Event event, int id, int mb, boolean release) {
        x = tile.xCoord; y = tile.yCoord; z = tile.zCoord;
        this.event = event;
        this.id = id;
        this.mb = mb;
        this.release = release;
    }

    public MessageGuiEvent(TileEntity tile, Event event, int id, float value) {
        x = tile.xCoord; y = tile.yCoord; z = tile.zCoord;
        this.event = event;
        this.id = id;
        floatVal = value;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        event = Event.values()[buf.readByte()];
        id = buf.readInt();
        switch (event)
        {
            case BUTTON_PRESS:
                mb = buf.readInt();
                release = buf.readBoolean();
                break;
            case PERCENT_SET:
                floatVal = buf.readFloat();
                break;
            default: break;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeByte(event.value);
        buf.writeInt(id);
        switch (event)
        {
            case BUTTON_PRESS:
                buf.writeInt(mb);
                buf.writeBoolean(release);
                break;
            case PERCENT_SET:
                buf.writeFloat(floatVal);
                break;
            default: break;
        }
    }

    public static class Handler implements IMessageHandler<MessageGuiEvent, IMessage> {

        @Override
        public IMessage onMessage(MessageGuiEvent guiEvent, MessageContext ctx) {

            //World w = Minecraft.getMinecraft().theWorld;
            World w = ctx.getServerHandler().playerEntity.worldObj;
            if(w == null) {
                FMLLog.warning("EnergyInversion: A tile GUI event received while world is null!\n");
                return null;
            }
            GuiEventInterface inter = (GuiEventInterface)w.getTileEntity(guiEvent.x,guiEvent.y,guiEvent.z);

            if(inter == null) {
                FMLLog.warning("EnergyInversion: A tile GUI event received for a tile without the interface!\n");
                return null;
            }

            switch (guiEvent.event)
            {
                case BUTTON_PRESS:
                    inter.buttonAction(guiEvent.id,guiEvent.mb,guiEvent.release);
                    break;
                case PERCENT_SET:
                    inter.buttonAction(guiEvent.id,guiEvent.floatVal);
                    break;
                default: break;
            }

            return null;
        }
    }
}