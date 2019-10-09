package me.mape.mazescape;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = MazeScape.MODID, name = MazeScape.NAME, version = MazeScape.VERSION)
public class MazeScape
{
    public static final String MODID = "mazescape";
    public static final String NAME = "MazeScape";
    public static final String VERSION = "1.12.2-1.0";

    @Instance("mazescape")
    public static MazeScape instance;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
    }
}
