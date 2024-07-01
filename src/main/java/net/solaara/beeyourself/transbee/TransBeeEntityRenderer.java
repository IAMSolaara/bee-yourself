package net.solaara.beeyourself.transbee;

import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BeeEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;

@Environment(EnvType.CLIENT)
public class TransBeeEntityRenderer extends BeeEntityRenderer {
	private static final Identifier ANGRY_TEXTURE = new Identifier("bee-yourself",
			"textures/entity/transbee/transbee_angry.png");
	private static final Identifier ANGRY_NECTAR_TEXTURE = new Identifier("bee-yourself",
			"textures/entity/transbee/transbee_angry_nectar.png");
	private static final Identifier PASSIVE_TEXTURE = new Identifier("bee-yourself",
			"textures/entity/transbee/transbee.png");
	private static final Identifier HAPPY_TEXTURE = new Identifier("bee-yourself",
			"textures/entity/transbee/transbee_happy_1.png");
	private static final Identifier NECTAR_TEXTURE = new Identifier("bee-yourself",
			"textures/entity/transbee/transbee_nectar.png");

	public TransBeeEntityRenderer(Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(BeeEntity entity) {
		if (entity instanceof TransBeeEntity) {
			var e = (TransBeeEntity) entity;
			if (e.get_happy_time() > 0) {
				return HAPPY_TEXTURE;
			}
		}
		if (entity.hasAngerTime()) {
			return entity.hasNectar() ? ANGRY_NECTAR_TEXTURE : ANGRY_TEXTURE;
		} else {
			return entity.hasNectar() ? NECTAR_TEXTURE : PASSIVE_TEXTURE;
		}
	}
}
