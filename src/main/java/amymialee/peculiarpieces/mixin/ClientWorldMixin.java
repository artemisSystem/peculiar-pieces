package amymialee.peculiarpieces.mixin;

import amymialee.peculiarpieces.PeculiarPieces;
import amymialee.peculiarpieces.component.PeculiarComponentInitializer;
import amymialee.peculiarpieces.component.WardingComponent;
import amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.block.Block;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "randomBlockDisplayTick", at = @At("HEAD"))
    public void randomBlockDisplayTick(int centerX, int centerY, int centerZ, int radius, Random random, Block block, BlockPos.Mutable pos, CallbackInfo ci) {
        if (VisibleBarriers.isVisible()) {
            World world = ((ClientWorld) ((Object) this));
            Optional<WardingComponent> component = PeculiarComponentInitializer.WARDING.maybeGet(world.getChunk(pos));
            if (component.isPresent()) {
                WardingComponent wardingComponent = component.get();
                if (wardingComponent.getWard(pos)) {
                    world.addParticle(PeculiarPieces.WARDING_AURA, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 0.0, 0.0, 0.0);
                }
            }
        }
    }
}