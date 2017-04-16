package com.bioxx.tfc2;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;

import com.bioxx.tfc2.core.RegistryItemQueue;
import com.bioxx.tfc2.core.TFCTabs;
import com.bioxx.tfc2.items.*;

public class TFCItems 
{
	public static Item StoneAxe;
	public static Item StoneShovel;
	public static Item StoneKnife;
	public static Item StoneHoe;
	public static Item StoneHammer;

	public static Item StoneAxeHead;
	public static Item StoneShovelHead;
	public static Item StoneKnifeHead;
	public static Item StoneHoeHead;
	public static Item StoneHammerHead;

	public static Item Plank;
	public static Item LooseRock;
	public static Item Firestarter;


	public static void Load()
	{
		TFC.log.info(new StringBuilder().append("[TFC2] Loading Items").toString());
		LooseRock = registerItemOnly(new ItemLooseRock().setUnlocalizedName("looserock"));
		Plank = registerItemOnly(new ItemPlank().setUnlocalizedName("plank"));

		StoneAxe = registerItem(new ItemAxe(ToolMaterial.STONE).setUnlocalizedName("stone_axe"));
		StoneShovel = registerItem(new ItemShovel(ToolMaterial.STONE).setUnlocalizedName("stone_shovel"));
		StoneKnife = registerItem(new ItemKnife(ToolMaterial.STONE).setUnlocalizedName("stone_knife"));
		StoneHoe = registerItem(new ItemHoe(ToolMaterial.STONE).setUnlocalizedName("stone_hoe"));
		StoneHammer = registerItem(new ItemHoe(ToolMaterial.STONE).setUnlocalizedName("stone_hammer"));

		StoneAxeHead = registerItem(new ItemToolHead().setUnlocalizedName("stone_axe_head"));
		StoneShovelHead = registerItem(new ItemToolHead().setUnlocalizedName("stone_shovel_head"));
		StoneKnifeHead = registerItem(new ItemToolHead().setUnlocalizedName("stone_knife_head"));
		StoneHoeHead = registerItem(new ItemToolHead().setUnlocalizedName("stone_hoe_head"));
		StoneHammerHead = registerItem(new ItemToolHead().setUnlocalizedName("stone_hammer_head"));

		Firestarter = registerItem(new ItemFirestarter().setUnlocalizedName("firestarter"));
	}

	public static void SetupCreativeTabs()
	{
		((TFCTabs) TFCTabs.TFCBuilding).setTabIconItemStack(new ItemStack(TFCBlocks.StoneBrick));
		((TFCTabs) TFCTabs.TFCDecoration).setTabIconItemStack(new ItemStack(TFCBlocks.StairsAsh));
		((TFCTabs) TFCTabs.TFCDevices).setTabIconItemStack(new ItemStack(TFCBlocks.Anvil));
		((TFCTabs) TFCTabs.TFCPottery).setTabIconItemStack(new ItemStack(Items.FLOWER_POT));
		((TFCTabs) TFCTabs.TFCMisc).setTabIconItemStack(new ItemStack(LooseRock));
		((TFCTabs) TFCTabs.TFCFoods).setTabIconItemStack(new ItemStack(Items.COOKED_CHICKEN));
		((TFCTabs) TFCTabs.TFCTools).setTabIconItemStack(new ItemStack(StoneAxe));
		((TFCTabs) TFCTabs.TFCWeapons).setTabIconItemStack(new ItemStack(Items.DIAMOND_SWORD));
		((TFCTabs) TFCTabs.TFCArmor).setTabIconItemStack(new ItemStack(Items.DIAMOND_CHESTPLATE));
		((TFCTabs) TFCTabs.TFCMaterials).setTabIconItemStack(new ItemStack(Items.REDSTONE));
		
	}
	public static void Register()
	{
		TFC.log.info(new StringBuilder().append("[TFC2] Registering Items").toString());
		RegistryItemQueue.getInstance().registerItems();

		SetupHarvestLevels();
	}

	/**
	 * Registers the item with the game registry and also registers a single ItemMeshDefinition for this item.
	 */
	private static Item registerItem(Item i)
	{
		RegistryItemQueue.getInstance().addFull(i);
		return i;
	}

	/**
	 * Registers the item with the game registry.<br>
	 * <br>
	 * Should be used for items that have multiple variants where we need to manually create a MeshDef
	 */
	private static Item registerItemOnly(Item i)
	{
		RegistryItemQueue.getInstance().addItemOnly(i);

		return i;
	}

	private static void SetupHarvestLevels()
	{
		StoneAxe.setHarvestLevel("axe", 1);
	}
}
