package net.solaara.beeyourself.transbee;

import java.util.Objects;

import dev.mayaqq.estrogen.registry.common.EstrogenEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.solaara.beeyourself.BeeYourself;
import net.solaara.beeyourself.ModUtils;

public class TransBeeEntity extends BeeEntity {

	public TransBeeEntity(EntityType<? extends BeeEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.targetSelector.add(10, new FollowGirlPowerGoal(this));
	}

	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (player.hasStatusEffect(EstrogenEffects.ESTROGEN_EFFECT)) {
			if (!world.isClient) {
				world.playSoundFromEntity(
						null, // Player - if non-null, will play sound for every nearby player *except* the
								// specified player
						this, // The position of where the sound will come from
						SoundEvents.ENTITY_CAT_PURREOW, // The sound that will play, in this case, the sound the anvil
						// plays when it lands.
						SoundCategory.NEUTRAL, // This determines which of the volume sliders affect this sound
						1f, // Volume multiplier, 1 is normal, 0.5 is half volume, etc
						1f // Pitch multiplier, 1 is normal, 0.5 is half pitch, etc
				);

				double d = this.random.nextGaussian() * 0.2;
				double e = this.random.nextGaussian() * 0.2;
				double f = this.random.nextGaussian() * 0.2;
				ModUtils.spawnForcedParticles((ServerWorld) world, ParticleTypes.HEART, this.getParticleX(1.0),
						this.getRandomBodyY() + 0.5,
						this.getParticleZ(1.0), 7, d, e,
						f, 1);

				this.setTarget(player);
				player.sendMessage(
						new TranslatableText(
								"entity.bee-yourself.trans_bee.interaction.praise_message."
										+ this.random.nextInt(0, 3)),
						false);

			}
			return ActionResult.SUCCESS;
		}
		return super.interactMob(player, hand);
	}

	class FollowGirlPowerGoal extends ActiveTargetGoal<PlayerEntity> {

		public FollowGirlPowerGoal(TransBeeEntity bee) {
			super(bee, PlayerEntity.class, 100, true, true,
					(e) -> e instanceof PlayerEntity && e.hasStatusEffect(EstrogenEffects.ESTROGEN_EFFECT));
		}

		@Override
		public boolean shouldContinue() {
			BeeYourself.LOGGER.info("shouldContinue()\n" + this.mob);
			if (this.mob.getTarget() != null) {
				return super.shouldContinue();
			} else {
				this.target = null;
				return false;
			}
		}

	}
}
