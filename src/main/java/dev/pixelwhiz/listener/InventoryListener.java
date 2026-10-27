package dev.pixelwhiz.listener;

import org.powernukkitx.inventory.Inventory;
import org.powernukkitx.item.Item;
import dev.pixelwhiz.task.FlashlightTask;

public class InventoryListener implements org.powernukkitx.inventory.InventoryListener {

    public FlashlightTask task;

    public InventoryListener(FlashlightTask plugin) {
        this.task = plugin;
    }

    @Override
    public void onInventoryChanged(Inventory inventory, Item oldItem, int slot) {
        this.task.requestLightLevelUpdate();
    }
}
