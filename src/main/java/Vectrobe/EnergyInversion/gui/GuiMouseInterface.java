package Vectrobe.EnergyInversion.gui;

public interface GuiMouseInterface
{
    public int GetID();
    public void SetID(int id);
    public boolean mouseHover(int x, int y);
    public boolean mouseClick(int x, int y, int b);
    public boolean mouseDrag(int x, int y, int b);
    public boolean mouseRelease(int x, int y, int b, long t);
}
