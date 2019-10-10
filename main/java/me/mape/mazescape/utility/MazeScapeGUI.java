package me.mape.mazescape.utility;


import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.lwjgl.input.Keyboard;

import me.mape.mazescape.init.ModItems;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class MazeScapeGUI extends GuiScreen{

		    private final String title = "               Please select an image ";
		    private final String title2="                   to be processed!";
		    private GuiButton mButtonClose;
		    private GuiButton addButton;
		    private GuiLabel newImageLabel;

		    @Override
		    public void initGui() {
		        super.initGui();
		        this.buttonList.add(addButton = new GuiButton(0, this.width / 2 - 100, this.height - (this.height / 4) - 50, "Generate maze"));
		        this.buttonList.add(mButtonClose = new GuiButton(1, this.width / 2 - 100, this.height - (this.height / 4) - 20, "Close"));
		        this.labelList.add(newImageLabel = new GuiLabel(fontRenderer, 2, this.width / 2 - 103, this.height / 2 - 40, 300, 20, 0xFFFFFF));

		        newImageLabel.addLine(title);
		        newImageLabel.addLine(title2);

		    }
		   
		    @Override
		    protected void actionPerformed(GuiButton button) throws IOException {
		        if (button == mButtonClose) {
		            mc.player.closeScreen();
		        } else if (button == addButton) {
		        	mc.player.closeScreen();
		        }
		    }
		    
		    public void updateScreen()
		    {
		        super.updateScreen();
		    }

		    @Override
		    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		        this.drawDefaultBackground();
		        super.drawScreen(mouseX, mouseY, partialTicks);
		    }
		    
		    protected void mouseClicked(int x, int y, int btn) {
		        try {
					super.mouseClicked(x, y, btn);
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }

		    @Override
		    public boolean doesGuiPauseGame() {
		        return true;
		    }
	
}
