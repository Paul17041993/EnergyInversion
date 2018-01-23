package Vectrobe.EnergyInversion.client;


import Vectrobe.EnergyInversion.energy.EnergyDir;
import Vectrobe.EnergyInversion.energy.EnergyType;
import Vectrobe.EnergyInversion.energyInverter.BlockEnergyInverter;
import Vectrobe.EnergyInversion.energyInverter.TileEntityEnergyInverter;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderEnergyInverter implements ISimpleBlockRenderingHandler
{
    public static int renderID;

    public static int renderPass;

    public RenderEnergyInverter()
    {
        renderID = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block block, int meta, int modelID, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, meta));
        tessellator.draw();

        if(block instanceof BlockEnergyInverter)
        {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, ((BlockEnergyInverter) block).GetSideIcon(EnergyDir.SEND, EnergyType.COFH));
            tessellator.draw();
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess iBlockAccess, int x, int y, int z, Block block, int modelID, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;
        int lightValue = block.getMixedBrightnessForBlock(iBlockAccess, x, y, z);
        tessellator.setBrightness(lightValue);
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        boolean flag = false;
        switch(renderPass) {
            case 0:
            flag = renderer.renderStandardBlock(block, x, y, z);
                break;
            case 1:

            if (block instanceof BlockEnergyInverter) {
                BlockEnergyInverter inverter = (BlockEnergyInverter) block;

                TileEntityEnergyInverter tile = (TileEntityEnergyInverter)iBlockAccess.getTileEntity(x,y,z);
                if(tile == null) return false;
                TileEntityEnergyInverter.Side[] sides = tile.GetSides();
                if(sides == null) return false;

                IIcon iicon;

                tessellator.setColorOpaque_F(1, 1, 1);

                iicon = sides[0] == null ? null : sides[0].GetIIcon();
                if (iicon != null)
                    if (renderer.renderAllFaces || block.shouldSideBeRendered(iBlockAccess, x, y - 1, z, 0)) {
                        tessellator.setBrightness(block.getMixedBrightnessForBlock(iBlockAccess, x, y - 1, z));
                        renderer.renderFaceYNeg(block, (double) x, (double) y, (double) z, iicon);
                        flag = true;
                    }

                iicon = sides[1] == null ? null : sides[1].GetIIcon();
                if (iicon != null)
                    if (renderer.renderAllFaces || block.shouldSideBeRendered(iBlockAccess, x, y + 1, z, 1)) {
                        tessellator.setBrightness(block.getMixedBrightnessForBlock(iBlockAccess, x, y + 1, z));
                        renderer.renderFaceYPos(block, (double) x, (double) y, (double) z, iicon);
                        flag = true;
                    }

                iicon = sides[2] == null ? null : sides[2].GetIIcon();
                if (iicon != null)
                    if (renderer.renderAllFaces || block.shouldSideBeRendered(iBlockAccess, x, y, z - 1, 2)) {
                        tessellator.setBrightness(block.getMixedBrightnessForBlock(iBlockAccess, x, y, z - 1));
                        renderer.renderFaceZNeg(block, (double) x, (double) y, (double) z, iicon);
                        flag = true;
                    }

                iicon = sides[3] == null ? null : sides[3].GetIIcon();
                if (iicon != null)
                    if (renderer.renderAllFaces || block.shouldSideBeRendered(iBlockAccess, x, y, z + 1, 3)) {
                        tessellator.setBrightness(block.getMixedBrightnessForBlock(iBlockAccess, x, y, z + 1));
                        renderer.renderFaceZPos(block, (double) x, (double) y, (double) z, iicon);
                        flag = true;
                    }


                iicon = sides[4] == null ? null : sides[4].GetIIcon();
                if (iicon != null)
                    if (renderer.renderAllFaces || block.shouldSideBeRendered(iBlockAccess, x - 1, y, z, 4)) {
                        tessellator.setBrightness(block.getMixedBrightnessForBlock(iBlockAccess, x - 1, y, z));
                        renderer.renderFaceXNeg(block, (double) x, (double) y, (double) z, iicon);
                        flag = true;
                    }

                iicon = sides[5] == null ? null : sides[5].GetIIcon();
                if (iicon != null)
                    if (renderer.renderAllFaces || block.shouldSideBeRendered(iBlockAccess, x + 1, y, z, 5)) {
                        tessellator.setBrightness(block.getMixedBrightnessForBlock(iBlockAccess, x + 1, y, z));
                        renderer.renderFaceXPos(block, (double) x, (double) y, (double) z, iicon);
                        flag = true;
                    }

            }
                break;
            default: break;

        }

        return flag;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelID) { return true; }

    @Override
    public int getRenderId() { return renderID; }
}
