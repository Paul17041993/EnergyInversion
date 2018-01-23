package Vectrobe.EnergyInversion.gui;

import Vectrobe.EnergyInversion.EnergyInversion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiGauge implements GuiMouseInterface
{
    public GuiGauge()
    {
        Init();
    }

    public static boolean initialised = false;
    public static void Init()
    {
        if(initialised) return;

        mc = Minecraft.getMinecraft();

        render = new GuiSprite(32,64);

        texture = new ResourceLocation("energyinversion:textures/gui/gauge.png");
    }
    protected static Minecraft mc = null;

    protected static GuiSprite render;
    protected static ResourceLocation texture;

    public int posX = 0, posY = 0, posZ = 0;
    public float valuePeak = 0f;
    public float valueCurrent = 0f;
    public float valueKnob = 0f;
    public class Colour
    {
        public void SetColour(float r, float g, float b)
        { this.r = r; this.b = b; this.g = g; }
        public float r = 1f, g = 1f, b = 1f;
    }
    public Colour colour = new Colour();

    int csx = -1, csy= -1, csb = -1;
    long cst = -1;
    float csk = 0f;

    int ID = 0;
    @Override
    public int GetID() { return this.ID; }
    @Override
    public void SetID(int id) { this.ID = id; }

    @Override
    public boolean mouseHover(int x, int y) {
        return ((x-posX) * (x-posX)) + ((y-posY) * (y-posY)) < 16;
    }

    @Override
    public boolean mouseClick(int x, int y, int b)
    {
        boolean hov = mouseHover(x,y);
        if(hov)
        {
            csx = x; csy = y; csb = b;
            cst = Minecraft.getSystemTime();
            csk = valueKnob;
        }
        else
        {
            csx = csy = csb = -1;
            cst = -1;
        }
        return hov;
    }

    @Override
    public boolean mouseDrag(int x, int y, int b)
    {
        if(cst != -1 && csb == b &&  Minecraft.getSystemTime() - cst > 250)
        {
            int diff = (x-csx) + (csy-y);
            diff *= csb == 0? 1 : -1;
            valueKnob = csk + ((float)diff / 100f);
            valueKnob = Math.min(1f, Math.max(0f, valueKnob) );
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseRelease(int x, int y, int b, long t)
    {
        return t < 250;
    }

    public void Draw()
    {
        this.mc.renderEngine.bindTexture(texture);

        // value background
        GL11.glColor4f(.25f,.25f,.25f,1f);
        render.DrawSprite(posX, posY, posZ, render.size, 0);

        // value peak
        GL11.glColor4f(colour.r * .75f, colour.g * .75f, colour.b * .75f, 1f);
        render.DrawSprite(posX, posY, posZ + 1, render.size, 0, (valuePeak - 1f) * Math.PI);

        // value current
        GL11.glColor4f(colour.r,colour.g,colour.b,1f);
        render.DrawSprite(posX, posY, posZ + 2, render.size, 0, (valueCurrent - 1f) * Math.PI);

        // gauge
        GL11.glColor4f(1f,1f,1f,1f);
        render.DrawSprite(posX, posY, posZ + 3, 0, 0);

        // knob
        GL11.glColor4f(1f,1f,1f,1f);
        render.DrawSprite(posX, posY, posZ + 4, 0, render.size);

        // knob top
        GL11.glColor4f(colour.r,colour.g,colour.b,1f);
        render.DrawSprite(posX, posY, posZ + 5, render.size, render.size, (valueKnob - .5f) * Math.PI);
    }
}
