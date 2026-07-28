package com.marbledhubb.antiquus.init.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.NonNull;

public class GroundFogParticle extends SingleQuadParticle {
    FacingCameraMode FLAT = (target, camera, _) -> {
        double halfAngle = Math.toRadians(45);
        target.set((float) (camera.position().y > this.y ? -Math.sin(halfAngle) : Math.sin(halfAngle)), 0, 0, (float) Math.cos(halfAngle));
    };

    public GroundFogParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        this.setAlpha(0);
        this.quadSize = this.random.nextFloat() * 0.3f + 0.4f;
        this.lifetime = random.nextInt(35, 96);
        this.hasPhysics = true;
        this.friction = 1f;
        this.gravity = 0f;
    }

    public GroundFogParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, TextureAtlasSprite sprite) {
        this(level, x, y, z, sprite);
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
    }

    @Override
    protected @NonNull Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public @NonNull FacingCameraMode getFacingCameraMode() {
        return FLAT;
    }

    @Override
    public void tick() {
        double originalX = this.x;

        super.tick();

        if (Math.abs(this.x - originalX) < 0.01 && this.age < this.lifetime * 0.8f)
            this.age = (int) (this.lifetime * 0.8f);

        float progress = (float) this.age / (float) this.lifetime;
        float alpha;
        if (progress < 0.2f) {
            alpha = progress / 0.2f;
        } else if (progress > 0.8f) {
            alpha = (1f - progress) / 0.2f;
        } else {
            alpha = 1f;
        }

        this.setAlpha(alpha);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(@NonNull SimpleParticleType options, @NonNull ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new GroundFogParticle(level, x, y, z,
                    random.nextDouble() * -0.025 - 0.05,
                    0,
                    (random.nextDouble() - 0.5) * 0.01,
                    this.sprite.get(random));
        }
    }
}
