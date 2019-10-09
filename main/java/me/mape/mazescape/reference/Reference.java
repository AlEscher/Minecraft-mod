package me.mape.mazescape.reference;

public class Reference {
	
	public static final String MOD_ID = "mazescape";
	public static final String MOD_NAME = "MazeScape";
	public static final String MOD_VERSION ="1.12.2-1.0";
	public static final String CLIENT_PROXY_CLASS = "me.mape.mazescape.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "me.mape.mazescape.proxy.ServerProxy";
	
	public static enum MazeScapeEnum {
		MAZESCAPE("mazescape");
		
		private String unlocalizedName;
		private String registryName;
		
		MazeScapeEnum(String registryName) {
			this.registryName = registryName;
		}
		
		public String getRegistryName() {
			return registryName;
		}
		
//		public String getUnlocalizedName() {
//			return unlocalizedName;
//		}
	}
	
}
