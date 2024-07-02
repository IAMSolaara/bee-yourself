package net.solaara.beeyourself.transflagheart;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.solaara.beeyourself.BeeYourself;

@Environment(EnvType.CLIENT)
public class TransFlagHeartParticle extends SpriteBillboardParticle {
	// The sprite provider which is used to determine the particle's texture
	private final SpriteProvider spriteProvider;
	private final int variant;

	TransFlagHeartParticle(ClientWorld world, double x, double y, double z, double velX, double velY, double velZ,
			SpriteProvider spriteProvider) {
		super(world, x, y, z);
		this.spriteProvider = spriteProvider; // Sets the sprite provider from above to the sprite provider in the
												// constructor parameters
		this.x = x; // The x from the constructor parameters
		this.y = y;
		this.z = z;
		this.alpha = 1.0f; // Setting the alpha to 1.0f means there will be no opacity change until the
							// alpha value is changed
		this.velocityMultiplier = 0.86F;
		this.velocityX *= 0.01F;
		this.velocityY *= 0.01F;
		this.velocityZ *= 0.01F;
		this.velocityY += 0.1;
		this.scale *= 1.5F;
		this.maxAge = 16;
		this.collidesWithWorld = false;
		this.setSpriteForAge(spriteProvider); // Required
		this.variant = world.random.nextInt(1, 4);
		var test = this.spriteProvider.getSprite(this.variant, this.variant);

		BeeYourself.LOGGER.info("variant " + this.variant);
		BeeYourself.LOGGER.info("variant " + test);
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		// The factory used in a particle's registry
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double x,
				double y, double z, double velX, double velY, double velZ) {
			return new TransFlagHeartParticle(clientWorld, x, y, z, velX, velY, velZ, this.spriteProvider);
		}
	}
}
