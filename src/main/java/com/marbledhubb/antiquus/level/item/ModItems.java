package com.marbledhubb.antiquus.level.item;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.level.item.custom.RockChiselItem;
import com.marbledhubb.antiquus.level.item.custom.RockHammerItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Weapon;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(Antiquus.MOD_ID);

    public static final DeferredItem<Item> FOSSILIZED_PROTOTAXITE_SPORES = ITEMS.registerSimpleItem("fossilized_prototaxite_spores");
    public static final DeferredItem<Item> FOSSILIZED_PROTOTAXITE_BUD = ITEMS.registerSimpleItem("fossilized_prototaxite_bud");
    public static final DeferredItem<Item> FOSSILIZED_COOKSONIA = ITEMS.registerSimpleItem("fossilized_cooksonia");
    public static final DeferredItem<Item> FOSSILIZED_ZOSTEROPHYLLUM = ITEMS.registerSimpleItem("fossilized_zosterophyllum");

    // TODO probably change the durability of these two. currently they have the same durability as a brush. it could be interesting if the chisel had a smaller durability -aimi
    public static final DeferredItem<Item> ROCK_HAMMER = ITEMS.registerItem("rock_hammer", RockHammerItem::new, properties -> properties.durability(64).attributes(createToolAttributes(0, 0)).component(DataComponents.WEAPON, new Weapon(2, 0)));
    public static final DeferredItem<Item> ROCK_CHISEL = ITEMS.registerItem("rock_chisel", RockChiselItem::new, properties -> properties.durability(64));

    private static ItemAttributeModifiers createToolAttributes(float attackDamageBaseline, float attackSpeedBaseline) {
        return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamageBaseline, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeedBaseline, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
