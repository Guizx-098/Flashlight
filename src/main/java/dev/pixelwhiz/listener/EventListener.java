package dev.pixelwhiz.listener;

import dev.pixelwhiz.Flashlight;
import dev.pixelwhiz.task.FlashlightTask;
import org.powernukkitx.Player;
import org.powernukkitx.Server;
import org.powernukkitx.event.EventHandler;
import org.powernukkitx.event.Listener;
import org.powernukkitx.event.player.PlayerItemHeldEvent;
import org.powernukkitx.event.player.PlayerJoinEvent;
import org.powernukkitx.event.player.PlayerQuitEvent;
import org.powernukkitx.event.player.PlayerTransferItemEvent;
import org.powernukkitx.inventory.HumanOffHandInventory;
import org.powernukkitx.inventory.Inventory;
import org.powernukkitx.item.Item;
import org.powernukkitx.scheduler.TaskHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EventListener implements Listener {

    public Flashlight plugin;
    public Map<Player, TaskHandler> tasks = new ConcurrentHashMap<>();

    private static final Set<String> ALLOWED_TORCHES = Set.of(
            "minecraft:torch",
            "minecraft:soul_torch",
            "minecraft:copper_torch",
            "minecraft:redstone_torch"
    );

    public EventListener(Flashlight plugin) {
        this.plugin = plugin;
    }

    private boolean isAllowedTorch(Item item) {
        return item != null && ALLOWED_TORCHES.contains(item.getId());
    }

    @EventHandler
    public void onTransfer(PlayerTransferItemEvent event) {
        if (event.getType() == PlayerTransferItemEvent.Type.DROP) {
            return;
        }

        Inventory source = event.getSourceInventory();
        Inventory destination = event.getDestinationInventory().orElse(null);

        Item itemEnteringOffhand = null;

        if (destination instanceof HumanOffHandInventory) {
            itemEnteringOffhand = event.getSourceItem();
        } else if (source instanceof HumanOffHandInventory) {
            itemEnteringOffhand = event.getDestinationItem().orElse(null);
        }

        if (itemEnteringOffhand != null && !isAllowedTorch(itemEnteringOffhand)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        TaskHandler handler = tasks.get(player);

        if (handler != null) {
            Runnable task = handler.getTask();
            if (task instanceof FlashlightTask) {
                ((FlashlightTask) task).requestLightLevelUpdate();
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        tasks.put(player, Server.getInstance().getScheduler().scheduleRepeatingTask(
                new FlashlightTask(player),
                Math.max(1, (int) (plugin.getConfig().getDouble("update-delay", 0.25) * 20))
        ));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        TaskHandler handler = tasks.remove(event.getPlayer());
        if (handler != null) {
            handler.cancel();
        }
    }
}
