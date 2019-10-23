package me.mape.mazescape.item;

import java.util.List;

import javax.annotation.Nullable;

import me.mape.mazescape.init.ModItems;
import me.mape.mazescape.reference.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BuildAWall extends Item{
	
	public BuildAWall() {
		super();
		this.setCreativeTab(CreativeTabs.TOOLS);
		setRegistryName(Reference.BuildAWallEnum.BUILDAWALL.getRegistryName());
		this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("This item lets you spawn a breakable wall");
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		worldIn.setBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()), Blocks.STONE.getDefaultState());
		worldIn.setBlockState(new BlockPos(pos.getX(), pos.getY() + 2, pos.getZ()), Blocks.STONE.getDefaultState());
		worldIn.setBlockState(new BlockPos(pos.getX(), pos.getY() + 3, pos.getZ()), Blocks.STONE.getDefaultState());
		
		IInventory inv = player.inventory;
		ItemStack itemstack = player.getHeldItem(hand);
		
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i).getItem() == itemstack.getItem()) {
				//inv.decrStackSize(i, 1);
				inv.getStackInSlot(i).shrink(1);
			}
		}
		
		return EnumActionResult.PASS;
	}

}
