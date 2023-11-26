package io.github.codevine327.fishingplus;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandImp implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1 && args.length != 2) {
            sender.sendMessage(Component.text("参数错误！").color(NamedTextColor.RED));
            return true;
        }

        switch (args[0]) {
            case "setchest" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(Component.text("只有游戏中的玩家可以使用该命令！").color(NamedTextColor.RED));
                    return true;
                }

                Block block = player.getTargetBlock(null, 5);
                if (!(block.getState() instanceof Chest chest)) {
                    sender.sendMessage(Component.text("找不到任何箱子！(请尝试靠近一点)").color(NamedTextColor.RED));
                    return true;
                }

                Inventory chestBlockInventory = chest.getInventory();
                List<ItemStack> itemList = new ArrayList<>();
                chestBlockInventory.forEach(item -> {
                    if (item != null) {
                        itemList.add(item);
                    }
                });
                DataUtil.setItemStackList(itemList, "treasure".equalsIgnoreCase(args[1]));
                sender.sendMessage(Component.text("宝藏设置成功，本次设置了" + itemList.size() + "个物品。"));
            }
            case "reload" -> {
                FishingPlus.getInstance().reloadConfig();
                FishingPlus.getInstance().getListener().reloadData();
                sender.sendMessage(Component.text("插件重载成功！"));
            }
            default -> sender.sendMessage(Component.text("未知的命令").color(NamedTextColor.RED));
        }

        return true;
    }
}
