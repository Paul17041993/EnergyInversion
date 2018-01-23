package Vectrobe.EnergyInversion.gui;


import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

public class GuiSprite
{
    public GuiSprite(int size, int textureSize)
    {
        this.size = size; this.textureSize = textureSize;
    }

    public int size = 32;
    public int textureSize = 64;

    /*
    Draws a sprite
     */
    void DrawSprite(int x, int y, int z, IIcon iicon)
    {
        int sizeHalf = size/2;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV((double) (x - sizeHalf), (double) (y + sizeHalf), (double) z, (double)iicon.getMinU(), (double)iicon.getMaxV());
        t.addVertexWithUV((double) (x + sizeHalf), (double) (y + sizeHalf), (double) z, (double)iicon.getMaxU(), (double)iicon.getMaxV());
        t.addVertexWithUV((double) (x + sizeHalf), (double) (y - sizeHalf), (double) z, (double)iicon.getMaxU(), (double)iicon.getMinV());
        t.addVertexWithUV((double) (x - sizeHalf), (double) (y - sizeHalf), (double) z, (double)iicon.getMinU(), (double)iicon.getMinV());
        t.draw();
    }

    /*
    Draws a sprite
     */
    void DrawSprite(int x, int y, int z, int u, int v)
    {
        int sizeHalf = size/2;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV((double) (x - sizeHalf), (double) (y + sizeHalf), (double) z, (double) ((float)(u) / textureSize), (double) ((float)(v + size) / textureSize));
        t.addVertexWithUV((double) (x + sizeHalf), (double) (y + sizeHalf), (double) z, (double) ((float)(u + size) / textureSize), (double) ((float)(v + size) / textureSize));
        t.addVertexWithUV((double) (x + sizeHalf), (double) (y - sizeHalf), (double) z, (double) ((float)(u + size) / textureSize), (double) ((float)(v) / textureSize));
        t.addVertexWithUV((double) (x - sizeHalf), (double) (y - sizeHalf), (double) z, (double) ((float)(u) / textureSize), (double) ((float)(v) / textureSize));
        t.draw();
    }

    /*
    Draws a sprite with an aspect
     */
    void DrawSprite(int x, int y, int z, float a, IIcon iicon)
    {
        int w = (int)((float)size * a)/2;
        int h = size/2;
        int tw = (int)((float)size * a);
        int th = size;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV((double) (x - w), (double) (y + h), (double) z, (double)iicon.getMinU(), (double)iicon.getMaxV());
        t.addVertexWithUV((double) (x + w), (double) (y + h), (double) z, (double)iicon.getMaxU(), (double)iicon.getMaxV());
        t.addVertexWithUV((double) (x + w), (double) (y - h), (double) z, (double)iicon.getMaxU(), (double)iicon.getMinV());
        t.addVertexWithUV((double) (x - w), (double) (y - h), (double) z, (double)iicon.getMinU(), (double)iicon.getMinV());
        t.draw();
    }

    /*
    Draws a sprite with an aspect
     */
    void DrawSprite(int x, int y, int z, float a, int u, int v)
    {
        int w = (int)((float)size * a)/2;
        int h = size/2;
        int tw = (int)((float)size * a);
        int th = size;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV((double) (x - w), (double) (y + h), (double) z, (double) ((float)(u) / textureSize), (double) ((float)(v + th) / textureSize));
        t.addVertexWithUV((double) (x + w), (double) (y + h), (double) z, (double) ((float)(u + tw) / textureSize), (double) ((float)(v + th) / textureSize));
        t.addVertexWithUV((double) (x + w), (double) (y - h), (double) z, (double) ((float)(u + tw) / textureSize), (double) ((float)(v) / textureSize));
        t.addVertexWithUV((double) (x - w), (double) (y - h), (double) z, (double) ((float)(u) / textureSize), (double) ((float)(v) / textureSize));
        t.draw();
    }

    /*
    Draws a sprite with a rotation
     */
    void DrawSprite(int x, int y, int z, int u, int v, double r)
    {
        int sizeHalf = size/2;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        double dX,dY;

        double sizeHalfSin = (double)sizeHalf * Math.sin(r);
        double sizeHalfCos = (double)sizeHalf * Math.cos(r);

        dX = -sizeHalfCos - sizeHalfSin;
        dY = -sizeHalfSin + sizeHalfCos;
        t.addVertexWithUV((double) x + dX, (double) y + dY, (double) z, (double) ((float)(u) / textureSize), (double) ((float)(v + size) / textureSize));

        dX = sizeHalfCos - sizeHalfSin;
        dY = sizeHalfSin + sizeHalfCos;
        t.addVertexWithUV((double) x + dX, (double) y + dY, (double) z, (double) ((float)(u + size) / textureSize), (double) ((float)(v + size) / textureSize));

        dX = sizeHalfCos - -sizeHalfSin;
        dY = sizeHalfSin + -sizeHalfCos;
        t.addVertexWithUV((double) x + dX, (double) y + dY, (double) z, (double) ((float)(u + size) / textureSize), (double) ((float)(v) / textureSize));

        dX = -sizeHalfCos - -sizeHalfSin;
        dY = -sizeHalfSin + -sizeHalfCos;
        t.addVertexWithUV((double) x + dX, (double) y + dY, (double) z, (double) ((float)(u) / textureSize), (double) ((float)(v) / textureSize));

        t.draw();
    }
}
