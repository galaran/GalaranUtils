package me.galaran.bukkitutils.__utils_project_name__.nms;

import net.minecraft.server.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NmsUtils {

    /** Bukkit's World.dropItem() drops stack with null NBT tag, this method not */
    public static void dropStackSafe(ItemStack stack, Location loc) {
        net.minecraft.server.World nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
        net.minecraft.server.ItemStack nmsStack = CraftItemStack.createNMSItemStack(stack);

        net.minecraft.server.EntityItem entity =
                new net.minecraft.server.EntityItem(nmsWorld, loc.getX(), loc.getY(), loc.getZ(), nmsStack);
        entity.pickupDelay = 10;
        nmsWorld.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    /**
     * Gives stacks to player
     * @return all stacks fit
     */
    @SuppressWarnings("deprecation")
    public static boolean giveStacks(Player player, boolean dropNearbyIfNotFit, Iterable<ItemStack> stacks) {
        List<ItemStack> stacksCopy = new ArrayList<ItemStack>();
        for (ItemStack curStack : stacks) {
            stacksCopy.add(curStack.clone());
        }

        Inventory inv = player.getInventory();
        HashMap<Integer, ItemStack> ungiven = inv.addItem(stacksCopy.toArray(new ItemStack[stacksCopy.size()]));
        player.updateInventory();
        if (ungiven != null && !ungiven.isEmpty()) {
            if (dropNearbyIfNotFit) {
                for (ItemStack ungivenStack : ungiven.values()) {
                    dropStackSafe(ungivenStack, player.getEyeLocation());
                }
            }
            return false;
        }
        return true;
    }

    /**
     * @return item lore or null if none
     */
    public static String getLore(ItemStack stack) {
        if (stack instanceof CraftItemStack) {
            NBTTagCompound tag = ((CraftItemStack) stack).getHandle().tag;
            if (tag != null && tag.hasKey("display") && tag.getCompound("display").hasKey("Name")) {
                return tag.getCompound("display").getString("Name");
            }
        }
        return null;
    }

    /**
     * @param lore item lore or null to clear
     * @return result stack
     */
    public static ItemStack setLore(ItemStack stack, String lore) {
        net.minecraft.server.ItemStack nmsStack = CraftItemStack.createNMSItemStack(stack);
        NBTTagCompound tag = nmsStack.tag;

        if (lore == null) {
            if (tag != null && tag.hasKey("display")) {
                tag.getCompound("display").remove("Name");
            }
        } else {
            if (tag == null) {
                nmsStack.tag = new NBTTagCompound();
                tag = nmsStack.tag;
            }

            if (!tag.hasKey("display")) {
                tag.setCompound("display", new NBTTagCompound());
            }

            tag.getCompound("display").setString("Name", lore);
        }

        return new CraftItemStack(nmsStack);
    }
}
