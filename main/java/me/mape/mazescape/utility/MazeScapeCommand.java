package me.mape.mazescape.utility;


import org.lwjgl.input.Mouse;

import me.mape.mazescape.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MazeScapeCommand extends CommandBase{

		private final String COMMAND_NAME = "setupmaze";
	    private final String COMMAND_HELP = "/setupmaze <x> <y> <length> <minLength>";

	    public String getCommandName() {
	        return COMMAND_NAME;
	    }

	    public String getCommandUsage(ICommandSender sender) {
	        return COMMAND_HELP;
	    }

	    @Override
	    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
	    	if (args != null && args.length == 4) {
	    		int x = Integer.parseInt(args[0]);
	    		int y = Integer.parseInt(args[1]);
	    		int length = Integer.parseInt(args[2]);
	    		int minLength = Integer.parseInt(args[3]);
	    		ModItems.mazegen.updateMazeValues(x, y, length, minLength);
	    	} else {
	    		LogHelper.warn("None / not enough arguments passed");
	    		sender.sendMessage(new TextComponentString("Usage: /setupmaze <x> <y> <length> <minLength>"));
	    	}
	        //Minecraft.getMinecraft().displayGuiScreen(new MazeScapeGUI());
	    }

		@Override
		public String getName() {
			return COMMAND_NAME;
		}

		@Override
		public String getUsage(ICommandSender sender) {
			return COMMAND_HELP;
		}
		
}
