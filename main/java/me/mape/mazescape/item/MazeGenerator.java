package me.mape.mazescape.item;

import java.util.List;

import javax.annotation.Nullable;

import me.mape.mazescape.init.ModItems;
import me.mape.mazescape.reference.Reference;
import me.mape.mazescape.utility.LogHelper;
import me.mape.mazescape.utility.MazeTest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MazeGenerator extends Item{
	
	private MazeTest mazeGenerator;
	private EntityPlayer currentPlayer;
	private World currentWorld;
	private BlockPos spawnPos;
	private double[] currentStartPoint;
	private double[] mazeSpawnPoint;
	
	// values for the maze
	private int x = 20;
	private int y = 20;
	private int length = 160;
	private int minLength = 50;
	private boolean hasChangedValues = false;
	
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
	
	public void updateMazeValues(int x, int y, int length, int minLength) {
		LogHelper.info("Updating maze values");
		this.x = x;
		this.y = y;
		this.length = length;
		this.minLength = minLength;
		this.hasChangedValues = true;
		mazeGenerator = new MazeTest(x, y, length, minLength);
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
		spawnPos = pos;
		ModItems.mazekey.setCurrentStage(0);
		initMaze(player, worldIn, pos, 0, true);
		
//		for (ItemStack stack : player.inventory.mainInventory) {
//			if (stack.isEmpty()) {
//				stack = PotionUtils.addPotionToItemStack(stack, PotionType.REGISTRY.getObjectById(1));
//				break;
//			}
//		}	
		return EnumActionResult.PASS;
    }
	
	/**
	 * Generates a maze if one is not already generated, also calls buildLabyrinth
	 * @param player
	 * @param worldIn
	 * @param pos
	 * @param stage
	 * @param createdByPlayer
	 */
	public void initMaze(EntityPlayer player, World worldIn, BlockPos pos, int stage, boolean createdByPlayer) {
		
		if (pos == null) {
			showMessage("Use the MazeGenerator first");
			return;
		}
		
		if (!hasChangedValues && worldIn.isRemote)
			showMessage("Using default values, type /setupmaze to customize the maze");
		
		if (mazeGenerator == null)
			mazeGenerator = new MazeTest(x, y, length, minLength);
		
		// Client generates a new maze if the current one is empty
		if (mazeGenerator.matrixIsEmpty() && worldIn.isRemote) {
			synchronized(mazeGenerator) {
				LogHelper.info("Generating maze matrix...");
				mazeGenerator.generateMaze();
				LogHelper.info("Matrix generated");
				// set the maze spawn point only if the maze was created by the player using the MazeGen item
				if (createdByPlayer) {
					double[] spawnPoint = {pos.getX(), pos.getY(), pos.getZ()};
					mazeSpawnPoint = spawnPoint;
				}

			}
		}
		
		// The server waits until the client finishes generating the maze
		if (!worldIn.isRemote) {
			synchronized(mazeGenerator) {
				LogHelper.info("Hello");
			}
		}
		
		LogHelper.info("Building maze...");
		buildLabyrinth(mazeGenerator.getMatrix(), worldIn, pos, stage);	
		LogHelper.info("Maze built");
		
		// teleport the player to the start point of the maze
		if (currentStartPoint != null) {
				
			double px = currentStartPoint[0] + 0.5;
			double py = currentStartPoint[1] + 1;
			double pz = currentStartPoint[2] + 0.5;
			if (!worldIn.isRemote) { 
				LogHelper.info("Teleporting player to " + px + " " + py + " " + pz);
				MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
				s.getCommandManager().executeCommand( s, "/tp " + player.getName() + " " + px + " " + py + " " + pz );
				LogHelper.info("Player teleported");
				s.getCommandManager().executeCommand(s, "/gamemode survival " + player.getName());
			}
		}
		
	}
	
	public void buildLabyrinth(int[][] bluePrint, World worldIn, BlockPos pos, int stage) {
		for (int i = 0; i < bluePrint.length; i++) {
			for (int j = 0; j < bluePrint[0].length; j++) {
				
				switch(bluePrint[i][j]) {
				
				case 0:
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 6 * stage, pos.getZ() + i), Blocks.BEDROCK.getDefaultState());
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 1 + 6 * stage, pos.getZ() + i), Blocks.BEDROCK.getDefaultState());
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 2 + 6 * stage, pos.getZ() + i), Blocks.BEDROCK.getDefaultState());
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 3 + 6 * stage, pos.getZ() + i), Blocks.BEDROCK.getDefaultState());
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 4 + 6 * stage, pos.getZ() + i), Blocks.BEDROCK.getDefaultState());
					break;
				case 4:
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 1 + 6 * stage, pos.getZ() + i), Blocks.CARPET.getDefaultState());
					double[] startPoint = {pos.getX() + j, pos.getY() + 6 * stage, pos.getZ() + i};
					LogHelper.info("Found start point at: " + startPoint[0] + " " + startPoint[1] + " " + startPoint[2]);
					currentStartPoint = startPoint;
					break;
				case 3:
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 6 * stage, pos.getZ() + i), Blocks.DIAMOND_BLOCK.getDefaultState());
					break;
				case 2:
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 6 * stage, pos.getZ() + i), Blocks.GLASS.getDefaultState());
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() - 1 + 6 * stage, pos.getZ() + i), Blocks.STONE.getDefaultState());
					break;
				case 1:
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 6 * stage, pos.getZ() + i), Blocks.GLASS.getDefaultState());
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() - 1 + 6 * stage, pos.getZ() + i), Blocks.GLOWSTONE.getDefaultState());
					//worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 4 + 6 * stage, pos.getZ() + i), Blocks.BEDROCK.getDefaultState());
					break;
				default:
					worldIn.setBlockState(new BlockPos(pos.getX() + j, pos.getY() + 1 + 6 * stage, pos.getZ() + i), Blocks.STONE.getDefaultState());
					break;
					
				}
			}
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
				//FMLClientHandler.instance().getServer().getWorld(0).destroyBlock(new BlockPos(px + j, py + 1, pz + i), false);
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
		LogHelper.info("Maze cleared");
		
	}
	
	public MazeTest getMazeGenerator() {
		return mazeGenerator;
	}
	
	public BlockPos getSpawnPos() {
		return spawnPos;
	}

}
