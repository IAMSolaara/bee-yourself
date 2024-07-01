package net.solaara.beeyourself;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.solaara.beeyourself.transbee.TransBeeEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeeYourself implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("bee-yourself");

	public static final EntityType<TransBeeEntity> TRANS_BEE = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier("beeyourself", "transbee"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TransBeeEntity::new)
					.dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build());

	public static final Item TRANS_BEE_SPAWN_EGG = new SpawnEggItem(TRANS_BEE, 0xF6AAB7, 0x55CDFD,
			new FabricItemSettings().group(ItemGroup.MISC));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		FabricDefaultAttributeRegistry.register(TRANS_BEE, TransBeeEntity.create_transbee_attributes());

		Registry.register(Registry.ITEM, new Identifier("bee-yourself", "trans_bee_spawn_egg"), TRANS_BEE_SPAWN_EGG);

		LOGGER.info("BeeYourself onInitialize()");
	}
}
