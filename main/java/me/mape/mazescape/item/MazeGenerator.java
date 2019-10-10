package me.mape.mazescape.item;

import java.util.List;

import javax.annotation.Nullable;

import me.mape.mazescape.reference.Reference;
import me.mape.mazescape.utility.LogHelper;
import me.mape.mazescape.utility.MazeTest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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

public class MazeGenerator extends MapeItem{
	
	private MazeTest mazeGenerator;
	private EntityPlayer currentPlayer;
	private World currentWorld;
	private double[] currentStartPoint;
	private double[] mazeSpawnPoint;
	
	public MazeGenerator() {
		super();
		this.setCreativeTab(CreativeTabs.TOOLS);
		setRegistryName(Reference.MazeGenEnum.MAZEGEN.getRegistryName());
		this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Use this item to build a stage");
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
		currentWorld = worldIn;
		LogHelper.info("Generating maze matrix...");
		if (mazeGenerator == null)
			mazeGenerator = new MazeTest(10, 10, 80, 20);
		if (mazeGenerator.matrixIsEmpty()) {
			mazeGenerator.generateMaze();
			double[] spawnPoint = {pos.getX(), pos.getY(), pos.getZ()};
			mazeSpawnPoint = spawnPoint;
		}
		LogHelper.info("Matrix generated");
		
		LogHelper.info("Building maze...");
		buildLabyrinth(mazeGenerator.getMatrix(), worldIn, pos);	
		LogHelper.info("Maze built");
		
		// teleport the player to the start point of the maze
		if (currentStartPoint != null) {
				
			double px = currentStartPoint[0] + 0.5;
			double py = currentStartPoint[1] + 1;
			double pz = currentStartPoint[2] + 0.5;
			LogHelper.info("Teleporting player to " + px + " " + py + " " + pz);
			MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
			s.getCommandManager().executeCommand( s, "/tp " + player.getName() + " " + px + " " + py + " " + pz );
			LogHelper.info("Player teleported");
		}
		
		return EnumActionResult.PASS;
    }
	
	private void buildLabyrinth(int[][] bluePrint, World worldIn, BlockPos pos) {
		for (int i = 0; i < bluePrint.length; i++) {
			for (int j = 0; j < bluePrint[0].length; j++) {
				
				switch(bluePrint[i][j]) {
				
				case 0:
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 1, pos.getZ() + i), Blocks.BEDROCK.getDefaultState());
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 2, pos.getZ() + i), Blocks.BEDROCK.getDefaultState());
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 3, pos.getZ() + i), Blocks.BEDROCK.getDefaultState());
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 4, pos.getZ() + i), Blocks.BEDROCK.getDefaultState());
					break;
				case 1:
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY(), pos.getZ() + i), Blocks.GLASS.getDefaultState());
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() - 1, pos.getZ() + i), Blocks.GLOWSTONE.getDefaultState());
					//worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 4, pos.getZ() + i), Blocks.BEDROCK.getDefaultState());
					break;
				case 4:
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 1, pos.getZ() + i), Blocks.CARPET.getDefaultState());
					double[] startPoint = {pos.getX() + j, pos.getY(), pos.getZ() + i};
					LogHelper.info("Found start point at: " + startPoint[0] + " " + startPoint[1] + " " + startPoint[2]);
					currentStartPoint = startPoint;
					break;
				case 3:
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY(), pos.getZ() + i), Blocks.DIAMOND_BLOCK.getDefaultState());
					break;
				default:
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 1, pos.getZ() + i), Blocks.STONE.getDefaultState());
					break;
					
				}
			}
			double progress = (double)i / (double)bluePrint.length;
			if (worldIn.isRemote && i > 0) {
				showMessage("Loaded " + progress * 100 + "% of maze!");
			}
		}
		if (worldIn.isRemote) {
			showMessage("Loaded 100% of maze!");
		}
	}
	
	public void clearLabyrinth() {
		clearLabyrinth(mazeGenerator.getMatrix(), currentWorld);
	}
	
	/**
	 * clear the maze after the player has solved it
	 * @param bluePrint
	 * @param worldIn
	 */
	private void clearLabyrinth(int[][] bluePrint, World worldIn) {
		
		double px = mazeSpawnPoint[0];
		double py = mazeSpawnPoint[1];
		double pz = mazeSpawnPoint[2];
		LogHelper.info("Found maze spawn point at: " + px + " " + py + " " + pz);
		
		LogHelper.info("Clearing maze...");
		for (int i = 0; i < bluePrint.length; i++) {
			for (int j = 0; j < bluePrint[0].length; j++) {
				
				// clear the walls
				worldIn.setBlockToAir(new BlockPos(px + j, py + 1, pz + i));
				worldIn.setBlockToAir(new BlockPos(px + j, py + 2, pz + i));
				worldIn.setBlockToAir(new BlockPos(px + j, py + 3, pz + i));
				worldIn.setBlockToAir(new BlockPos(px + j, py + 4, pz + i));
				
				// clear the floor
				worldIn.setBlockState(new BlockPos(px + j, py - 1, pz + i), Blocks.DIRT.getDefaultState());
				worldIn.setBlockState(new BlockPos(px + j, py, pz + i), Blocks.DIRT.getDefaultState());
				
				// clear old start point
				worldIn.setBlockToAir(new BlockPos(currentStartPoint[0], currentStartPoint[1] + 1, currentStartPoint[2]));
				
			}
		}
		LogHelper.info("Updating Render from :" + px + " " + py + " " + pz + " to " + (px + bluePrint[0].length) + " " +  (py + 4) + " " + (pz + bluePrint.length));
		worldIn.markBlockRangeForRenderUpdate(new BlockPos(px, py, pz), new BlockPos(px + bluePrint[0].length, py + 4, pz + bluePrint.length));
		LogHelper.info("Maze cleared");
		
	}

}
