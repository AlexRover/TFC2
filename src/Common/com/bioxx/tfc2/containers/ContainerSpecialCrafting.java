package com.bioxx.tfc2.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.bioxx.tfc2.api.crafting.CraftingManagerTFC;
import com.bioxx.tfc2.api.crafting.CraftingManagerTFC.RecipeType;
import com.bioxx.tfc2.containers.slots.SlotSpecialCraftingOutput;
import com.bioxx.tfc2.core.PlayerInfo;
import com.bioxx.tfc2.core.PlayerInventory;
import com.bioxx.tfc2.core.PlayerManagerTFC;

public class ContainerSpecialCrafting extends ContainerTFC
{
	/** The crafting matrix inventory (5x5).
	 *  Used for knapping and leather working */
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 9, 9);

	private SlotSpecialCraftingOutput outputSlot;
	private boolean decreasedStack;

	/** The crafting result, size 1. */
	public IInventory craftResult = new InventoryCraftResult();
	private World worldObj;
	private InventoryPlayer invPlayer;
	private boolean isConstructing;
	public ContainerSpecialCrafting(InventoryPlayer inventoryplayer, ItemStack is, World world, int x, int y, int z)
	{
		invPlayer = inventoryplayer;
		this.worldObj = world; // Must be set before inventorySlotContents to prevent NPE
		decreasedStack = false;
		isConstructing = true;
		bagsSlotNum = inventoryplayer.currentItem;
		for (int j1 = 0; j1 < 81; j1++)
		{
			if(is != null)
				craftMatrix.setInventorySlotContents(j1, is.copy());
		}

		outputSlot = new SlotSpecialCraftingOutput(this, inventoryplayer.player, craftMatrix, craftResult, 0, 128, 44);
		addSlotToContainer(outputSlot);

		PlayerInventory.buildInventoryLayout(this, inventoryplayer, 8, 108, true, true);

		this.onCraftMatrixChanged(this.craftMatrix);
		isConstructing = false;
	}

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		if (!this.worldObj.isRemote)
		{
			ItemStack is = this.craftResult.getStackInSlot(0);
			if (is != ItemStack.EMPTY)
				player.entityDropItem(is, 0);
		}
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	@Override
	public void onCraftMatrixChanged(IInventory ii)
	{
		ItemStack result = CraftingManagerTFC.getInstance().findMatchingRecipe(RecipeType.KNAPPING, this.craftMatrix, worldObj);

		// Handle decreasing the stack of the held item used to open the interface.
		if (!decreasedStack && !isConstructing)
		{
			PlayerInfo pi = PlayerManagerTFC.getInstance().getPlayerInfoFromPlayer(invPlayer.player);

			// A valid clay recipe has been formed.
			/*if (pi.specialCraftingType.getItem() == TFCItems.flatClay)
			{
				if (result != null)
				{
					setDecreasedStack(true); // Mark container so it won't decrease again.
					if (!this.worldObj.isRemote && invPlayer.getCurrentItem().getMaxStackSize() >= 5) // Server only to prevent it removing multiple times.
						invPlayer.decrStackSize(invPlayer.currentItem, 5);
					else // Clientside or if the player doesn't have enough clay, return before the output slot is set.
					{
						setDecreasedStack(false);
						return;
					}
				}
			}
			// A piece of rock or leather has been removed.
			else*/ if (hasPieceBeenRemoved(pi))
			{
				setDecreasedStack(true); // Mark container so it won't decrease again.
				if (!this.worldObj.isRemote) // Server only to prevent it removing multiple times.
				{
					invPlayer.decrStackSize(invPlayer.currentItem, 1);
				}
			}
		}

		// The crafting output is only set if the input was consumed
		if (decreasedStack)
		{
			this.craftResult.setInventorySlotContents(0, result);

			// Trigger Achievements
			if (result != ItemStack.EMPTY && invPlayer.player != null)
			{
				Item item = result.getItem();

			}
		}
	}

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift clicking.
	 * @return null if successful, the original item stack otherwise
	 */
	@Override
	public ItemStack transferStackInSlotTFC(EntityPlayer player, int slotNum)
	{
		Slot slot = (Slot)this.inventorySlots.get(slotNum);
		if (slot == null)  return ItemStack.EMPTY;
		ItemStack origStack = slot.getStack();

		if (slot instanceof SlotSpecialCraftingOutput && slot.getHasStack())
		{
			ItemStack slotStack = slot.getStack();
			InventoryPlayer ip = player.inventory;

			if (slotNum < 1 && !ip.addItemStackToInventory(slotStack))
				return ItemStack.EMPTY;

			if (slotStack.getMaxStackSize() <= 0)
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();

			slot.onTake(player, slotStack);
		}

		return origStack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	public boolean hasPieceBeenRemoved(PlayerInfo pi)
	{
		// Knapping interface is a boolean array where the value is true if that button has been pushed.
		for (int i = 0; i < this.craftMatrix.getSizeInventory(); i++)
		{
			if (this.craftMatrix.getStackInSlot(i) == ItemStack.EMPTY)
				return true;
		}

		// Reset the decreasedStack flag if no pieces have been removed.
		setDecreasedStack(false);
		return false;
	}

	public void setDecreasedStack(Boolean b)
	{
		this.decreasedStack = b;
	}

}
