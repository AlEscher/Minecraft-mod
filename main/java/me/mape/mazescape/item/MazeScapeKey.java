package me.mape.mazescape.item;

import java.util.List;

import javax.annotation.Nullable;

import me.mape.mazescape.reference.Reference;
import me.mape.mazescape.utility.MazeTest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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
	private MazeTest mazeGenerator;
	
	public MazeScapeKey() {
		super();
		this.setCreativeTab(CreativeTabs.TOOLS);
		//setUnlocalizedName(Reference.ImageSpawnerEnum.IMAGESPAWNER.getUnlocalizedName());
		setRegistryName(Reference.MazeScapeEnum.MAZESCAPE.getRegistryName());
		this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Use this key at the finish goal to enter the next stage");
	}
	
	private void showMessage(String message) {
		currentPlayer.sendMessage(new TextComponentString(message));
	}
	
	@Override
	/**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		currentPlayer = player;
		IBlockState selectedBlock = worldIn.getBlockState(pos);
		if (currentPlayer != null && worldIn.isRemote) {
			showMessage("You just used a Penis!");
			showMessage("Clicked on: " + selectedBlock.getBlock().getLocalizedName());
		}
		
		if (mazeGenerator == null) {
			mazeGenerator = new MazeTest(10, 10, 80, 40);
		}
		
		if (mazeGenerator != null) {
			
		}
		
		return EnumActionResult.PASS;
    }
	
	private void buildLabyrinth(int[][] bluePrint) {
		
	}

}
