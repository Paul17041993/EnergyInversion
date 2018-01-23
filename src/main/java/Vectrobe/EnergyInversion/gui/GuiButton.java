package Vectrobe.EnergyInversion.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

public class GuiButton implements GuiMouseInterface
{
    protected static GuiSprite render;

    public int posX = 0, posY = 0, posZ = 0;

    protected int width = 0, height = 0;

    IIcon baseIIcon = null, modeIIcon = null;

    public int mb = -1;

    public GuiButton(int width, int height)
    {
        this.width = width;
        this.height = height;
        render = new GuiSprite(width,0);
    }

    int ID = 0;
    @Override
    public int GetID() { return this.ID; }
    @Override
    public void SetID(int id) { this.ID = id; }

    @Override
    public boolean mouseHover(int x, int y)
    {
        return x < (posX + width/2) && x > (posX - width/2)
                && y < (posY + height/2) && y > (posY - height/2);
    }

    @Override
    public boolean mouseClick(int x, int y, int b)
    {
        boolean hov = mouseHover(x,y);
        if(hov) mb = b;
        return hov;
    }

    @Override
    public boolean mouseDrag(int x, int y, int b) {
        return false;
    }

    @Override
    public boolean mouseRelease(int x, int y, int b, long t)
    {
        return b == mb && mouseHover(x,y);
    }

    public void Draw()
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        if(baseIIcon != null)
            render.DrawSprite(posX, posY, posZ, (float)width / (float)height, baseIIcon);
        if(modeIIcon != null)
            render.DrawSprite(posX, posY, posZ, (float)width / (float)height, modeIIcon);

    }
}
