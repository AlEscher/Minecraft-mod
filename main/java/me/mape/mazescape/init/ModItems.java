package me.mape.mazescape.init;

import me.mape.mazescape.item.MapeItem;
import me.mape.mazescape.item.MazeGenerator;
import me.mape.mazescape.item.MazeScapeKey;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.mape.mazescape.reference.Reference;

@Mod.EventBusSubscriber(modid=Reference.MOD_ID)
public class ModItems {
	
	public static MazeScapeKey mazekey;
	public static MazeGenerator mazegen;
	
	public static void init() {
		
		mazekey = new MazeScapeKey();
		mazegen = new MazeGenerator();
		
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(mazekey, mazegen);
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		registerRender(mazekey);
		registerRender(mazegen);
	}
	
	private static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation( item.getRegistryName(), "inventory"));
	}
	
}
