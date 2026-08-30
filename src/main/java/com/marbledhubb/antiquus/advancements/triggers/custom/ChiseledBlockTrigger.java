package com.marbledhubb.antiquus.advancements.triggers.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import com.marbledhubb.antiquus.advancements.triggers.custom.ChiseledBlockTrigger.TriggerInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChiseledBlockTrigger extends SimpleCriterionTrigger<TriggerInstance>  {
    @Override
    public @NonNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, boolean successful, List<ItemStack> usedTools) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(successful, usedTools));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, boolean successful, Optional<List<ItemPredicate>> usedTools) implements SimpleCriterionTrigger.SimpleInstance  {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                i -> i.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                                Codec.BOOL.optionalFieldOf("successful", true).forGetter(TriggerInstance::successful),
                                ItemPredicate.CODEC.listOf().optionalFieldOf("used_tools").forGetter(TriggerInstance::usedTools)
                        ).apply(i, TriggerInstance::new));

        public boolean matches(boolean successful, List<ItemStack> usedTools) {
            if (successful() != successful) return false;

            if (usedTools().isEmpty()) return true;

            List<ItemStack> remainingTools = new ArrayList<>(usedTools);
            for (ItemPredicate predicate : usedTools().get()) {
                boolean matched = false;

                for (ItemStack stack : remainingTools) {
                    if (predicate.test(stack)) {
                        remainingTools.remove(stack);
                        matched = true;
                        break;
                    }
                }

                if (!matched) return false;
            }

            return true;
        }
    }
}
