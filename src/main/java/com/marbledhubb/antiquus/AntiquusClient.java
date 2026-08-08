package com.marbledhubb.antiquus;

import com.marbledhubb.antiquus.client.renderer.blockentity.ChiselableBlockRenderer;
import com.marbledhubb.antiquus.init.ModBlockEntityTypes;
import com.marbledhubb.antiquus.init.ModItems;
import com.marbledhubb.antiquus.init.ModNetworking;
import com.marbledhubb.antiquus.init.ModParticles;
import com.marbledhubb.antiquus.init.items.ModClientItemExtensions;
import com.marbledhubb.antiquus.init.particles.GroundFogParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.SuspendedParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jspecify.annotations.NonNull;

@Mod(value = Antiquus.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Antiquus.MOD_ID, value = Dist.CLIENT)
public class AntiquusClient {
    public AntiquusClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        container.getEventBus().addListener(ModNetworking::registerClient);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(
                new ModClientItemExtensions(),
                ModItems.ROCK_HAMMER.get(),
                ModItems.ROCK_CHISEL.get()
        );
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                ModBlockEntityTypes.CHISELABLE_BLOCK.get(),
                ChiselableBlockRenderer::new
        );
    }

    @SubscribeEvent
    static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.PROTOTAXITE_SPORE.get(), PrototaxiteSporeProvider::new);
        event.registerSpriteSet(ModParticles.GROUND_FOG.get(), GroundFogParticle.Provider::new);
    }

    private record PrototaxiteSporeProvider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(@NonNull SimpleParticleType options, @NonNull ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            SuspendedParticle particle = new SuspendedParticle(level, x, y, z,
                    random.nextGaussian() * 1.0E-8d,
                    random.nextGaussian() * 1.0E-6d - 2.0E-6d,
                    random.nextGaussian() * 1.0E-8d,
                    this.sprite.get(random));
            particle.setColor(0.8274509803921568f, 0.788235294117647f, 0.7215686274509804f);
            particle.setLifetime((int) (16d / random.nextFloat() * 0.6d + 60d));
            return particle;
        }
    }
}
