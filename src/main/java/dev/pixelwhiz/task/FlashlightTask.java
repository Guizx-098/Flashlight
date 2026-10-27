package dev.pixelwhiz.task;

import org.powernukkitx.Player;
import org.powernukkitx.block.Block;
import org.powernukkitx.block.BlockLiquid;
import org.powernukkitx.item.Item;
import org.powernukkitx.level.Position;
import org.powernukkitx.math.BlockVector3;
import org.powernukkitx.scheduler.Task;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket;
import org.powernukkitx.utils.RuntimeBlockDefinition;

import dev.pixelwhiz.listener.InventoryListener;
import dev.pixelwhiz.utils.LightLevelCalculator;

public class FlashlightTask extends Task {

    private int lightLevel = 0;
    private Position pos = null;

    public final Player player;
    public volatile boolean requireLightLevelUpdate = true;

    public FlashlightTask(Player player) {
        this.player = player;

        InventoryListener listener = new InventoryListener(this);
        player.getInventory().addListener(listener);
        player.getOffhandInventory().addListener(listener);
    }

    @Override
    public void onRun(int currentTick) {
        if (player.isClosed() || !player.isConnected()) {
            restoreBlock();
            if (getHandler() != null) {
                getHandler().cancel();
            }
            return;
        }

        if (requireLightLevelUpdate) {
            Item main = player.getInventory().getItemInMainHand();
            Item off = player.getInventory().getItemInOffhand();

            int level = Math.max(
                LightLevelCalculator.calc(main),
                LightLevelCalculator.calc(off)
            );

            requireLightLevelUpdate = false;
            setLightLevel(level);
        }

        if (lightLevel <= 0) {
            return;
        }

        Position current = player.getPosition();
        Position next = Position.fromObject(
            current.add(0.5, 1, 0.5).floor(),
            current.getLevel()
        );

        if (pos == null || !pos.equals(next)) {
            restoreBlock();
            pos = next;
            overrideBlock();
        }
    }

    private void sendBlockLayers(Position position, Block base, Block second) {
        if (position == null || !player.isConnected()) {
            return;
        }

        Vector3i vector = Vector3i.from(
            position.getFloorX(),
            position.getFloorY(),
            position.getFloorZ()
        );

        sendBlock(position, vector, base, 0);
        sendBlock(position, vector, second, 1);
    }

    private void sendBlock(
        Position position,
        Vector3i vector,
        Block block,
        int layer
    ) {
        UpdateBlockPacket packet = new UpdateBlockPacket();
        packet.setBlockPosition(vector);
        packet.setDefinition(new RuntimeBlockDefinition(block.getRuntimeId()));
        packet.setLayer(layer);
        player.sendPacket(packet);
    }

    private void restoreBlock() {
        if (pos == null) {
            return;
        }

        Position old = pos;
        pos = null;

        Block base = old.getLevelBlockAtLayer(0);
        Block second = old.getLevelBlockAtLayer(1);

        if (base == null) {
            base = Block.get("minecraft:air");
        }
        if (second == null) {
            second = Block.get("minecraft:air");
        }

        sendBlockLayers(old, base, second);
    }

    @Override
    public void onCancel() {
        restoreBlock();
    }

    private Block getLightLevelBlock(int level) {
        int safeLevel = Math.max(0, Math.min(15, level));
        return Block.get("minecraft:light_block_" + safeLevel);
    }

    private void overrideBlock() {
        if (pos == null || lightLevel <= 0) {
            return;
        }

        Block base = pos.getLevelBlockAtLayer(0);
        Block second = getLightLevelBlock(lightLevel);

        if (base == null) {
            base = Block.get("minecraft:air");
        }

        if (base instanceof BlockLiquid) {
            Block temp = base;
            base = second;
            second = temp;
        }

        sendBlockLayers(pos, base, second);
    }

    public void requestLightLevelUpdate() {
        requireLightLevelUpdate = true;
    }

    private void setLightLevel(int level) {
        int nextLevel = Math.max(0, Math.min(15, level));

        if (lightLevel == nextLevel) {
            return;
        }

        lightLevel = nextLevel;

        if (lightLevel <= 0) {
            restoreBlock();
        } else {
            overrideBlock();
        }
    }
}
