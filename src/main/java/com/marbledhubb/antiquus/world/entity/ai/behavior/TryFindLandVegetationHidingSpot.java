package com.marbledhubb.antiquus.world.entity.ai.behavior;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import org.apache.commons.lang3.mutable.MutableLong;

import java.util.TreeMap;

public class TryFindLandVegetationHidingSpot {
    public static BehaviorControl<PathfinderMob> create(int range, float speedModifier) {
        MutableLong nextOkStartTime = new MutableLong(0);
        return BehaviorBuilder.create((i) -> i.group(i.absent(MemoryModuleType.ATTACK_TARGET), i.absent(MemoryModuleType.WALK_TARGET), i.registered(MemoryModuleType.LOOK_TARGET)).apply(i, (attackTarget, walkTarget, lookTarget) -> (level, body, timestamp) -> {
            if (timestamp < nextOkStartTime.longValue()) return false;

            BlockPos bodyBlockPos = body.blockPosition();

            TreeMap<Integer, Pair<BlockPos, Double>> potentialHidingSpots = new TreeMap<>();
            for(BlockPos pos : BlockPos.withinManhattan(body.blockPosition(), range, range, range)) {
                if ((pos.getX() != bodyBlockPos.getX() || pos.getZ() != bodyBlockPos.getZ()) && level.getBlockState(pos).getBlock() instanceof VegetationBlock && level.getFluidState(pos).isEmpty()) {
                    Path path = body.getNavigation().createPath(pos, 0);
                    if (path != null && path.canReach()) {
                        int rating = getHidingSpotRating(level, pos);
                        double length = getPathLength(path);
                        if (!potentialHidingSpots.containsKey(rating) || potentialHidingSpots.get(rating).getSecond() > length) {
                            potentialHidingSpots.put(rating, new Pair<>(pos, length));
                        }
                    }
                }
            }

            if (potentialHidingSpots.isEmpty()) {
                nextOkStartTime.setValue(timestamp + 40);
                return false;
            }

            BlockPos pos = potentialHidingSpots.firstEntry().getValue().getFirst();
            lookTarget.set(new BlockPosTracker(pos));
            walkTarget.set(new WalkTarget(new BlockPosTracker(pos), speedModifier, 0));

            nextOkStartTime.setValue(timestamp + 40);
            return true;
        }));
    }

    private static int getHidingSpotRating(LevelReader level, BlockPos pos) {
       int rating = level.getLightEmission(pos);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue;

                BlockPos neighborPos = pos.offset(dx, 0, dz);
                if (!(level.getBlockState(neighborPos).getBlock() instanceof VegetationBlock))
                    rating += 8;
            }
        }

        return rating;
    }

    private static double getPathLength(Path path) {
        double distance = 0;

        for (int i = 1; i < path.getNodeCount(); i++) {
            Node a = path.getNode(i - 1);
            Node b = path.getNode(i);

            distance += Math.sqrt(a.distanceToSqr(b));
        }

        return distance;
    }
}
