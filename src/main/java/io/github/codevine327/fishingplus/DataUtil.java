package io.github.codevine327.fishingplus;

import com.google.common.collect.ImmutableList;
import me.lucko.helper.random.RandomSelector;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DataUtil {
    public static Object pickReward(boolean isTreasure) {
        return RandomSelector.uniform(getRewardList(isTreasure)).pick();
    }

    public static List<Object> getRewardList(boolean isTreasure) {
        return ImmutableList.builder()
                .addAll(getItemStackList(isTreasure))
                .addAll(getCommandList(isTreasure))
                .build();
    }

    public static List<ItemStack> getItemStackList(boolean isTreasure) {
        ConfigurationSection itemsData = isTreasure ?
                getConfig().getConfigurationSection("treasure.chest-items") :
                getConfig().getConfigurationSection("junk.chest-items");

        return itemsData.getKeys(false).stream()
                .map(itemsData::getItemStack)
                .toList();
    }

    public static void setItemStackList(List<ItemStack> itemList, boolean isTreasure) {
        ConfigurationSection itemsData = isTreasure ?
                getConfig().getConfigurationSection("treasure.chest-items") :
                getConfig().getConfigurationSection("junk.chest-items");

        for (int i = 0; i < itemList.size(); i++) {
            itemsData.set(String.valueOf(i), itemList.get(i));
        }

        FishingPlus.getInstance().saveConfig();
    }

    public static List<List<String>> getCommandList(boolean isTreasure) {
        ConfigurationSection commandData = isTreasure ?
                getConfig().getConfigurationSection("treasure.command-items") :
                getConfig().getConfigurationSection("junk.command-items");

        return commandData.getKeys(false).stream()
                .map(commandData::getStringList)
                .toList();
    }

    private static FileConfiguration getConfig() {
        return FishingPlus.getInstance().getConfig();
    }
}
