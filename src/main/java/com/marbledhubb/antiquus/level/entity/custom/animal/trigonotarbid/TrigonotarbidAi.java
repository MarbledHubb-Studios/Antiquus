package com.marbledhubb.antiquus.level.entity.custom.animal.trigonotarbid;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;

public class TrigonotarbidAi {
    public TrigonotarbidAi() {
    }

    protected static void initMemories(Trigonotarbid body, RandomSource random) {
        //body.getBrain().setMemory();
    }

    protected static List<ActivityData<Trigonotarbid>> getActivities() {
        return List.of(initCoreActivity(), initIdleActivity());
    }

    private static ActivityData<Trigonotarbid> initCoreActivity() {
        return ActivityData.create(Activity.CORE, 0, ImmutableList.of(
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink()
        ));
    }

    private static ActivityData<Trigonotarbid> initIdleActivity() {
        return ActivityData.create(Activity.IDLE, 0, ImmutableList.of(
                new RunOne<>(ImmutableList.of(
                        Pair.of(new DoNothing(30, 60), 1),
                        Pair.of(SetEntityLookTarget.create(EntityTypes.PLAYER, 6), 2)
                )),
                //Pair.of(1, StartAttacking.create((level, body) -> canAttack(body), (level, body) -> body.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE))),
                new RunOne<>(ImmutableList.of(
                        Pair.of(new DoNothing(20, 100), 1),
                        Pair.of(RandomStroll.stroll(0.6F), 2)
                ))
                )/*,
                ImmutableSet.of(Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_ABSENT))*/
        );
    }

    public static void updateActivity(Trigonotarbid body) {
        body.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
    }
}
