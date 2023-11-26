package io.github.codevine327.fishingplus;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.ItemStack;

import javax.xml.crypto.Data;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerListener implements Listener {
    private final FishingPlus plugin;
    private final Set<Material> fishes = Set.of(Material.COD, Material.SALMON, Material.TROPICAL_FISH, Material.PUFFERFISH);
    private final Set<Material> junks = new HashSet<>();
    private final Set<Material> treasures = new HashSet<>();

    private void loadData() {
        junks.clear();
        treasures.clear();

        plugin.getConfig().getStringList("junk.materials")
                .forEach(m -> junks.add(Material.valueOf(m)));
        plugin.getConfig().getStringList("treasure.materials")
                .forEach(m -> junks.add(Material.valueOf(m)));
    }

    public PlayerListener(FishingPlus plugin) {
        this.plugin = plugin;
        loadData();
    }

    public void reloadData() {
        loadData();
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerFishing(PlayerFishEvent event) {
        if (plugin.getConfig().getBoolean("debug-mode")) {
            FishHook hook = event.getHook();
            if (hook.getMaxWaitTime() > 20) {
                hook.setMinWaitTime(20);
                hook.setMaxWaitTime(20);
            }
        }

        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) {
            return;
        }

        if (!(event.getCaught() instanceof Item hookedItem)) {
            return;
        }

        if (fishes.contains(hookedItem.getItemStack().getType())) {
            return;
        }

        // 这里才是正式处理非鱼类物品的逻辑，需要替换为自定义物品。
        boolean isTreasure = treasures.contains(hookedItem.getItemStack().getType());
        Object picked = DataUtil.pickReward(isTreasure);
        if (picked instanceof ItemStack pickedItem) {
            ((Item) event.getCaught()).setItemStack(pickedItem);
        } else {
            ((Item) event.getCaught()).setItemStack(new ItemStack(Material.AIR));
            List<String> command = (List<String>) picked;
            command.forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("\\{player}", event.getPlayer().getName())));
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerItemMend(PlayerItemMendEvent event) {
        if (event.getItem().getType() == Material.FISHING_ROD) {
            event.setCancelled(true);
        }
    }
}
