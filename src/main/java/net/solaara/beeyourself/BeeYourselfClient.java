package net.solaara.beeyourself;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.solaara.beeyourself.transbee.TransBeeEntityRenderer;

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
	}

}
