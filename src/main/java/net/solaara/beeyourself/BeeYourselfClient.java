package net.solaara.beeyourself;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.particle.EmotionParticle.HeartFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.solaara.beeyourself.transbee.TransBeeEntityModel;
import net.solaara.beeyourself.transbee.TransBeeEntityRenderer;
import net.solaara.beeyourself.transflagheart.TransFlagHeartParticle;

@Environment(EnvType.CLIENT)
public class BeeYourselfClient implements ClientModInitializer {
	public static final EntityModelLayer MODEL_TRANSBEE_LAYER = new EntityModelLayer(
			new Identifier("bee-yourself", "transbee"), "main");

	@Override
	public void onInitializeClient() {
		net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(BeeYourself.TRANS_BEE,
				(context) -> {
					return new TransBeeEntityRenderer(context);
				});

		// ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
		// .register(((atlasTexture, registry) -> {
		// BeeYourself.LOGGER.info("SPRITE!!!\n" + atlasTexture + "\n" + registry);
		// registry.register(new Identifier("bee-yourself",
		// "particle/trans_flag_heart1"));
		// registry.register(new Identifier("bee-yourself",
		// "particle/trans_flag_heart2"));
		// registry.register(new Identifier("bee-yourself",
		// "particle/trans_flag_heart3"));
		// }));

		ParticleFactoryRegistry.getInstance().register(BeeYourself.TRANS_FLAG_HEART_PARTICLE_1,
				TransFlagHeartParticle.Factory::new);

		ParticleFactoryRegistry.getInstance().register(BeeYourself.TRANS_FLAG_HEART_PARTICLE_2,
				TransFlagHeartParticle.Factory::new);

		ParticleFactoryRegistry.getInstance().register(BeeYourself.TRANS_FLAG_HEART_PARTICLE_3,
				TransFlagHeartParticle.Factory::new);

		EntityModelLayerRegistry.register(MODEL_TRANSBEE_LAYER, TransBeeEntityModel::getTexturedModelData);
	}

}
