package com.insane.thermalcasting;

import cofh.api.modhelpers.ThermalExpansionHelper;
import tconstruct.TConstruct;
import tconstruct.library.crafting.CastingRecipe;
import tconstruct.library.crafting.FluidType;
import tconstruct.library.crafting.LiquidCasting;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid=ThermalCasting.MODID, name="Thermal Casting", version="0.0.1", dependencies="required-after:ThermalExpansion;required-after:TConstruct")
public class ThermalCasting {
	
	public static final String MODID = "ThermalCasting";
	
	
	@Mod.Instance(MODID)
	public static ThermalCasting instance;
	
	@SidedProxy(clientSide="com.insane.thermalcasting.client.ClientProxy", serverSide="com.insane.CommonProxy")
	public static CommonProxy proxy;
	
	public static Block blockCastingFrame;
	public static int energyCost;
	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		blockCastingFrame = new BlockCastingFrame();
		GameRegistry.registerBlock(blockCastingFrame, "castingFrame");
		
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		
		energyCost = config.get("General", "Energy cost to make block", 2500).getInt();
		
		if (config.hasChanged())
			config.save();
	}

	
	@Mod.EventHandler
	public static void init(FMLInitializationEvent event)
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(blockCastingFrame, new Object[] {"xxx", "x x", "xxx", 'x', "rodStone"}));
		
		LiquidCasting casting = TConstruct.getBasinCasting();
		//Get fluids
		for (FluidType type :  FluidType.fluidTypes.values())
		{
			FluidStack fluid = new FluidStack(type.fluid, TConstruct.blockLiquidValue);
			//Recipes
			CastingRecipe rec = casting.getCastingRecipe(fluid, null);
			
			if (rec != null)
				ThermalExpansionHelper.addTransposerFill(energyCost, new ItemStack(blockCastingFrame), rec.getResult(), fluid, false);
		}
	}
	
	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		
	}
}
