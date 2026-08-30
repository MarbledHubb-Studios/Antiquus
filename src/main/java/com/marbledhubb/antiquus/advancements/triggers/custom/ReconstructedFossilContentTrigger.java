package com.marbledhubb.antiquus.advancements.triggers.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import com.marbledhubb.antiquus.advancements.triggers.custom.ReconstructedFossilContentTrigger.TriggerInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class ReconstructedFossilContentTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    @Override
    public @NonNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack content) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(content));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> content) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                i -> i.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                                ItemPredicate.CODEC.optionalFieldOf("content").forGetter(TriggerInstance::content)
                        ).apply(i, TriggerInstance::new));

        public boolean matches(ItemStack content) {
            return content().isEmpty() || content().get().test(content);
        }
    }
}
