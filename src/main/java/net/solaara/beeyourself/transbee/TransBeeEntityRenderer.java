package net.solaara.beeyourself.transbee;

import net.minecraft.client.render.entity.BeeEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.Identifier;

public class TransBeeEntityRenderer extends BeeEntityRenderer {

	public TransBeeEntityRenderer(Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(BeeEntity entity) {
		return new Identifier("bee-yourself", "textures/entity/transbee/transbee.png");
	}
}
