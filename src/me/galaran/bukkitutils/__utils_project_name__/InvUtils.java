package me.galaran.bukkitutils.__utils_project_name__;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvUtils {

    public static boolean contains(Inventory inv, ItemStack stack) {
        return contains(inv, stack.getType(), stack.getDurability(), stack.getAmount());
    }

    public static boolean contains(Inventory inv, Material type, short data, int minAmount) {
        int amount = 0;
        for (ItemStack curInvStack : inv.getContents()) {
            if (curInvStack == null) continue;
            if (curInvStack.getType() == type && curInvStack.getDurability() == data) {
                amount += curInvStack.getAmount();
            }
        }

        return amount >= minAmount;
    }
}
