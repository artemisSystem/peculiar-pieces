package amymialee.peculiarpieces.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class WardingComponent implements AutoSyncedComponent {
    private final Chunk chunk;
    private final Set<BlockPos> set = new HashSet<>();

    public WardingComponent(Chunk chunk) {
        this.chunk = chunk;
    }

    public boolean getWard(WorldView world, BlockPos pos) {
        BlockState block = world.getBlockState(pos);
        return !block.isAir() && !(block.isOf(Blocks.PISTON) || block.isOf(Blocks.STICKY_PISTON) || block.isOf(Blocks.MOVING_PISTON) || block.isOf(Blocks.PISTON_HEAD)) && getWard(pos);
    }

    public boolean getWard(BlockPos pos) {
        return set.contains(pos);
    }

    public void setWard(BlockPos pos, boolean warded) {
        if (warded) {
            this.set.add(pos);
        } else {
            this.set.remove(pos);
        }
        this.chunk.setNeedsSaving(true);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        NbtList list = tag.getList("positions", NbtElement.COMPOUND_TYPE);
        for (NbtElement element : list) {
            if (element instanceof NbtCompound compound) {
                BlockPos pos = NbtHelper.toBlockPos(compound);
                set.add(pos);
            }
        }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        NbtList list = new NbtList();
        for (BlockPos pos : set) {
            list.add(NbtHelper.fromBlockPos(pos));
        }
        tag.put("positions", list);
    }
}