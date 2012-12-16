package me.galaran.bukkitutils.__utils_project_name__.nms;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

public class ItemStackWithNBT implements ConfigurationSerializable {

    private final net.minecraft.server.ItemStack nmsStack;

    /**
     * Only wraps bukkit stack, no clonning
     */
    public ItemStackWithNBT(ItemStack bukkitStack) {
        nmsStack = CraftItemStack.createNMSItemStack(bukkitStack);
    }

    public ItemStack getBukkitStack() {
        return new CraftItemStack(nmsStack);
    }

    /**
     * @return item lore or null if none
     */
    public String getLore() {
        NBTTagCompound tag = nmsStack.tag;
        if (tag != null && tag.hasKey("display") && tag.getCompound("display").hasKey("Name")) {
            return tag.getCompound("display").getString("Name");
        }
        return null;
    }

    /**
     * @param lore item lore or null to clear
     */
    public void setLore(String lore) {
        NBTTagCompound tag = nmsStack.tag;

        if (lore == null) {
            if (tag != null && tag.hasKey("display")) {
                tag.getCompound("display").remove("Name");
            }
        } else {
            if (tag == null) {
                tag = new NBTTagCompound();
                nmsStack.tag = tag;
            }

            if (!tag.hasKey("display")) {
                tag.setCompound("display", new NBTTagCompound());
            }

            tag.getCompound("display").setString("Name", lore);
        }
    }

    private static final Method nbtloadMethod = lookupNbtLoadMethod();
    private static final Method nbtWriteMethod = lookupNbtWriteMethod();

    private static Method lookupNbtLoadMethod() {
        for (Method m : NBTBase.class.getMethods()) {
            if (Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers())) {
                if (m.getReturnType().getName().contains("NBTBase")) {
                    Class[] paramTypes = m.getParameterTypes();
                    if (paramTypes != null && paramTypes.length == 1 && paramTypes[0].getName().contains("DataInput")) {
                        return m;
                    }
                }
            }
        }
        throw new IllegalStateException("Lookup NBT Load Method returned null");
    }

    private static Method lookupNbtWriteMethod() {
        for (Method m : NBTBase.class.getMethods()) {
            if (Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers())) {
                if (m.getReturnType().equals(Void.TYPE)) {
                    Class[] paramTypes = m.getParameterTypes();
                    if (paramTypes != null && paramTypes.length == 2 && paramTypes[0].getName().contains("NBTBase") &&
                            paramTypes[1].getName().contains("DataOutput")) {
                        return m;
                    }
                }
            }
        }
        throw new IllegalStateException("Lookup NBT Write Method returned null");
    }

    /**
     * Get stack's NBT tag as Base64 string or null, if no tag
     */
    public String getTagBase64() {
        if (nmsStack.tag == null) return null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dis = new DataOutputStream(baos);
        try {
            nbtWriteMethod.invoke(null, nmsStack.tag, dis);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return DatatypeConverter.printBase64Binary(baos.toByteArray());
    }

    /**
     * Set stack's NBT tag to Base64 string. Null will set tag to null
     */
    public void setTagBase64(String tagBase64) {
        if (tagBase64 == null) {
            nmsStack.tag = null;
            return;
        }

        byte[] nbtData = DatatypeConverter.parseBase64Binary(tagBase64);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(nbtData));
        try {
            nmsStack.tag = (NBTTagCompound) nbtloadMethod.invoke(null, dis);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("type", Material.getMaterial(nmsStack.id).name());

        if (nmsStack.getData() != 0) {
            result.put("damage", nmsStack.getData());
        }

        if (nmsStack.count != 1) {
            result.put("amount", nmsStack.count);
        }

        String tagBase64 = getTagBase64();
        if (tagBase64 != null) {
            result.put("tag-base64", tagBase64);
        }

        return result;
    }

    public static ItemStackWithNBT deserialize(Map<String, Object> map) {
        Material type = Material.getMaterial((String) map.get("type"));
        short damage = 0;
        int amount = 1;

        if (map.containsKey("damage")) {
            damage = ((Number) map.get("damage")).shortValue();
        }

        if (map.containsKey("amount")) {
            amount = ((Number) map.get("amount")).intValue();
        }

        ItemStackWithNBT result = new ItemStackWithNBT(new ItemStack(type, amount, damage));
        result.setTagBase64((String) map.get("tag-base64"));

        return result;
    }
}
