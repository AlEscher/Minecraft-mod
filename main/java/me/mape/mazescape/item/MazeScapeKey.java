package me.mape.mazescape.item;

import java.util.List;

import javax.annotation.Nullable;

import me.mape.mazescape.init.ModItems;
import me.mape.mazescape.reference.Reference;
import me.mape.mazescape.utility.LogHelper;
import me.mape.mazescape.utility.MazeTest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.concurrent.Lock;

/**
 * The key that is needed in game to complete a stage
 */
public class MazeScapeKey extends Item{
	
	private EntityPlayer currentPlayer;
	/**
	 * current stage (0 based)
	 */
	private int currentStage = 0;
	
	public MazeScapeKey() {
		super();
		this.setCreativeTab(CreativeTabs.TOOLS);
		setRegistryName(Reference.MazeScapeEnum.MAZESCAPE.getRegistryName());
		this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Use this key at the finish goal to enter the next stage");
	}
	
	/**
	 * Displays a message for the player
	 * @param message
	 */
	private void showMessage(String message) {
		if (currentPlayer != null)
			currentPlayer.sendMessage(new TextComponentString(message));
	}
	
	@Override
	/**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		currentPlayer = player;
		if (worldIn.getBlockState(pos) == Blocks.DIAMOND_BLOCK.getDefaultState()) {
			if (worldIn.isRemote) {
				showMessage("Congratulations, you solved the maze!\nProgressing to stage " + (++currentStage + 1));
			}
			//ModItems.mazegen.clearLabyrinth();
			if (worldIn.isRemote) { 
				LogHelper.info("Resetting maze");
				ModItems.mazegen.getMazeGenerator().emptyMatrix();
				showMessage("Progressing to stage " + currentStage);
			}
			LogHelper.info("Generating stage " + currentStage);
			ModItems.mazegen.initMaze(player, worldIn, ModItems.mazegen.getSpawnPos(), currentStage, false);
		} else if (worldIn.getBlockState(pos) == Blocks.GLASS.getDefaultState()) {
			
			if (!worldIn.isRemote) {
				MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
				s.getCommandManager().executeCommand( s, "/give " + player.getName() + " potion 1 0 {Potion:\"minecraft:strong_swiftness\"}");
			}
			
		}
		return EnumActionResult.PASS;
    }
	
	public void setCurrentStage(int newStage) {
		this.currentStage = newStage;
	}
	
	

}
