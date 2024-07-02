package net.solaara.beeyourself;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.particle.EmotionParticle.HeartFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
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

		ParticleFactoryRegistry.getInstance().register(BeeYourself.TRANS_FLAG_HEART_PARTICLE,
				TransFlagHeartParticle.Factory::new);

		EntityModelLayerRegistry.register(MODEL_TRANSBEE_LAYER, TransBeeEntityModel::getTexturedModelData);
	}

}
