package me.mape.mazescape.item;

import java.util.List;

import javax.annotation.Nullable;

import me.mape.mazescape.reference.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * The key that is needed in game to complete a stage
 */
public class MazeScapeKey extends MapeItem{
	
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

}
