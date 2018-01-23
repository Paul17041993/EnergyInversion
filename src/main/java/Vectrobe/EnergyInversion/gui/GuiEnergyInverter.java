package Vectrobe.EnergyInversion.gui;

import Vectrobe.EnergyInversion.energy.EnergyDir;
import Vectrobe.EnergyInversion.energy.EnergyType;
import Vectrobe.EnergyInversion.energyInverter.TileEntityEnergyInverter;
import Vectrobe.EnergyInversion.network.MessageGuiEvent;
import Vectrobe.EnergyInversion.network.Network;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Vector;

public class GuiEnergyInverter extends GuiScreen
{
    TileEntityEnergyInverter activeInverter;

    GuiGauge gauge;
    float testval = 0f;

    int cx,cy;

    ResourceLocation gridTexture;
    GuiSprite grid;

    GuiGauge[] sideGauges = {new GuiGauge(),new GuiGauge(),new GuiGauge(),new GuiGauge(),new GuiGauge(),new GuiGauge()};
    GuiGauge gaugeIC2inv = new GuiGauge();
    GuiGauge gaugeIC2out = new GuiGauge();
    GuiGauge gaugeCOFHinv = new GuiGauge();
    GuiGauge gaugeCOFHout = new GuiGauge();

    public static final int BUTTON_GROUP = 255 << 8;
    public static final int BUTTON_GROUP_FACE_DIR = 1 << 8;
    public static final int BUTTON_GROUP_FACE_TYPE = 2 << 8;
    public static final int BUTTON_IC2_INVERSION = 0;
    public static final int BUTTON_IC2_THROUGH = 1;
    public static final int BUTTON_COFH_INVERSION = 2;
    public static final int BUTTON_COFH_THROUGH = 3;

    TileEntityEnergyInverter.Side[] sides;

    long lastMouseEvent = 0;
    Vector<GuiMouseInterface> buttons;
    GuiMouseInterface activeButton;

    GuiButton[] sideButtons = {new GuiButton(32,32),new GuiButton(32,32),new GuiButton(32,32),new GuiButton(32,32),new GuiButton(32,32),new GuiButton(32,32)};


    public GuiEnergyInverter(TileEntityEnergyInverter inverter)
    {
        activeInverter = inverter;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }


    @Override
    public void updateScreen()
    {
        //gauge.valuePeak += .05f;
        testval += 1f;

        sides = activeInverter.GetSides();

        for(int i = 0; i < 6; ++i)
        {
            sides[i].SetColour(sideGauges[i].colour);
            sideButtons[i].baseIIcon = activeInverter.GetBaseIIcon(i);
            sideButtons[i].modeIIcon = sides[i].GetIIcon();

            sideGauges[i].valuePeak = sides[i].GetPeak();
            sideGauges[i].valueCurrent = sides[i].GetCurrent();
        }

    }

    @Override
    public void initGui()
    {
        gauge = new GuiGauge();
        gauge.posX = 32;
        gauge.posY = 32;
        gauge.colour.SetColour(1f, 0f, 1f);

        int w = Minecraft.getMinecraft().currentScreen.width;
        int h = Minecraft.getMinecraft().currentScreen.height;
        cx = w/2;
        cy = h/2;

        gridTexture = new ResourceLocation("energyinversion:textures/gui/grid.png");
        grid = new GuiSprite(96,8);


        sides = activeInverter.GetSides();

        buttons = new Vector<GuiMouseInterface>();

        for(int i = 0; i < 6; ++i)
        {
            sideGauges[i].posX = cx - (int)((2.5f - (float)i) *48f);
            sideGauges[i].posY = cy + 64;
            sideGauges[i].posZ = 0;

            sideGauges[i].valueKnob = sides[i].GetPeak();

            sides[i].SetColour(sideGauges[i].colour);

            sideGauges[i].SetID(BUTTON_GROUP_FACE_TYPE + i);
            buttons.add(sideGauges[i]);

            sideButtons[i].posX = cx - (int)((2.5f - (float)i) *48f);
            sideButtons[i].posY = cy + 64 + 32;
            sideButtons[i].posZ = 0;

            sideButtons[i].baseIIcon = activeInverter.GetBaseIIcon(i);
            sideButtons[i].modeIIcon = sides[i].GetIIcon();

            sideButtons[i].SetID(BUTTON_GROUP_FACE_DIR + i);
            buttons.add(sideButtons[i]);
        }

        gaugeIC2inv.posX = cx - 2*48;
        gaugeIC2inv.posY = cy - 48;
        gaugeIC2inv.posZ = 0;
        gaugeIC2inv.colour.SetColour(1,1,1);
        gaugeIC2inv.SetID(BUTTON_IC2_INVERSION);
        buttons.add(gaugeIC2inv);

        gaugeIC2out.posX = cx - 2*48;
        gaugeIC2out.posY = cy;
        gaugeIC2out.posZ = 0;
        gaugeIC2out.colour.SetColour(0,1,1);
        gaugeIC2out.SetID(BUTTON_IC2_THROUGH);
        buttons.add(gaugeIC2out);

        gaugeCOFHinv.posX = cx + 2*48;
        gaugeCOFHinv.posY = cy - 48;
        gaugeCOFHinv.posZ = 0;
        gaugeCOFHinv.colour.SetColour(1,1,1);
        gaugeCOFHinv.SetID(BUTTON_COFH_INVERSION);
        buttons.add(gaugeCOFHinv);

        gaugeCOFHout.posX = cx + 2*48;
        gaugeCOFHout.posY = cy;
        gaugeCOFHout.posZ = 0;
        gaugeCOFHout.colour.SetColour(1,0,0);
        gaugeCOFHout.SetID(BUTTON_COFH_THROUGH);
        buttons.add(gaugeCOFHout);


    }

    @Override
    public void onGuiClosed()
    {

    }

    @SideOnly(Side.CLIENT)
    protected void buttonAction(int id, int mb, boolean release)
    {
        if(activeButton instanceof GuiGauge)
        {
            if(((GuiGauge)activeButton).valueKnob != ((GuiGauge)activeButton).valuePeak)
            {
                Network.instance.sendToServer(new MessageGuiEvent(activeInverter,
                        MessageGuiEvent.Event.PERCENT_SET, id, ((GuiGauge)activeButton).valueKnob));
                return;
            }
        }

        Network.instance.sendToServer(new MessageGuiEvent(activeInverter,
                MessageGuiEvent.Event.BUTTON_PRESS, id, mb, release));

    }

    @Override
    protected void mouseClicked(int x, int y, int b)
    {
        for(GuiMouseInterface button : buttons)
            if(button.mouseClick(x,y,b)) {
                activeButton = button;
                lastMouseEvent = Minecraft.getSystemTime();
                return;
            }
    }

    @Override
    protected void mouseMovedOrUp(int x, int y, int b)
    {
        if(activeButton == null) return;
        if(activeButton.mouseRelease(x,y,b,Minecraft.getSystemTime() - lastMouseEvent))
            buttonAction(activeButton.GetID(), b, true);
        activeButton = null;
    }

    @Override
    protected void mouseClickMove(int x, int y, int b, long t)
    {
        if(activeButton == null) return;
        if(activeButton.mouseDrag(x,y,b))
            buttonAction(activeButton.GetID(), b, false);
    }



    @Override
    public void drawScreen(int mouseX, int mouseY, float delta)
    {
        this.drawWorldBackground(0);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);


        this.mc.renderEngine.bindTexture(gridTexture);
        grid.DrawSprite(cx, cy - 24, 0, 1.5f, 0, 0);
        grid.DrawSprite(32,32,0,0,0, .25d * Math.PI);

        gauge.valuePeak = (testval + delta) / 50f;
        gauge.Draw();

        for(int i = 0; i < 6; ++i) {
            sideGauges[i].Draw();
            sideButtons[i].Draw();
        }

        gaugeIC2inv.Draw();
        gaugeIC2out.Draw();
        gaugeCOFHinv.Draw();
        gaugeCOFHout.Draw();


        //this.drawCenteredString(this.fontRendererObj, I18n.format("advMode.setCommand", new Object[0]), this.width / 2, 20, 16777215);
        //this.drawString(this.fontRendererObj, I18n.format("advMode.command", new Object[0]), this.width / 2 - 150, 37, 10526880);
        ////this.commandTextField.drawTextBox();
        //byte var4 = 75;
        //byte var5 = 0;
        //FontRenderer var10001 = this.fontRendererObj;
        //String var10002 = I18n.format("advMode.nearestPlayer", new Object[0]);
        //int var10003 = this.width / 2 - 150;
        //int var8 = var5 + 1;
        //this.drawString(var10001, var10002, var10003, var4 + var5 * this.fontRendererObj.FONT_HEIGHT, 10526880);
        //this.drawString(this.fontRendererObj, I18n.format("advMode.randomPlayer", new Object[0]), this.width / 2 - 150, var4 + var8++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
        //this.drawString(this.fontRendererObj, I18n.format("advMode.allPlayers", new Object[0]), this.width / 2 - 150, var4 + var8++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
        ////if(this.field_146486_g.getText().length() > 0) {
        ////    int var7 = var4 + var8 * this.fontRendererObj.FONT_HEIGHT + 20;
        ////    this.drawString(this.fontRendererObj, I18n.format("advMode.previousOutput", new Object[0]), this.width / 2 - 150, var7, 10526880);
        ////    this.field_146486_g.drawTextBox();
        ////}


        GL11.glDisable(GL11.GL_BLEND);

        super.drawScreen(mouseX, mouseY, delta);
    }

    //@Override
    //protected void drawGuiContainerForegroundLayer(int param1, int param2)
    //{
    //    //draw text and stuff here
    //    //the parameters for drawString are: string, x, y, color
    //    Minecraft.getMinecraft().fontRenderer.drawString("Tiny", 8, 6, 4210752);
    //    //draws "Inventory" or your regional equivalent
    //    Minecraft.getMinecraft().fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    //}

    //@Override
    //protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    //{
    //    //draw your Gui here, only thing you need to change is the path
    //    int texture = mc.renderEngine.getTexture("/gui/trap.png");
    //    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    //    this.mc.renderEngine.bindTexture(texture);
    //    int x = (width - xSize) / 2;
    //    int y = (height - ySize) / 2;
    //    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    //}

}
