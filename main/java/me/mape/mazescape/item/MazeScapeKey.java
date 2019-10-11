package me.mape.mazescape.item;

import java.util.List;

import javax.annotation.Nullable;

import me.mape.mazescape.init.ModItems;
import me.mape.mazescape.reference.Reference;
import me.mape.mazescape.utility.MazeTest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * The key that is needed in game to complete a stage
 */
public class MazeScapeKey extends MapeItem{
	
	private EntityPlayer currentPlayer;
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
		Block selectedBlock = worldIn.getBlockState(pos).getBlock();
		if (Block.getStateId(selectedBlock.getDefaultState()) == Block.getStateId(Blocks.DIAMOND_BLOCK.getDefaultState())) {
			if (worldIn.isRemote) {
				showMessage("Congratulations, you solved the maze!\nProgressing to stage " + ++currentStage);
			}
			ModItems.mazegen.clearLabyrinth();
			ModItems.mazegen.getMazeGenerator().emptyMatrix();
		}
		
		return EnumActionResult.PASS;
    }
	
	

}
