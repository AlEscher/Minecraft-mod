package me.mape.mazescape;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import me.mape.mazescape.init.ModItems;
import me.mape.mazescape.proxy.IProxy;
import me.mape.mazescape.reference.Reference;
import me.mape.mazescape.utility.LogHelper;

@Mod(modid = MazeScape.MODID, name = MazeScape.NAME, version = MazeScape.VERSION)
public class MazeScape
{
    public static final String MODID = Reference.MOD_ID;
    public static final String NAME = Reference.MOD_NAME;
    public static final String VERSION = Reference.MOD_VERSION;

    @Instance(Reference.MOD_ID)
    public static MazeScape instance;
    
    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	ModItems.init();
    	LogHelper.info("PreInit complete");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        LogHelper.info("Init complete");
    }
}
