package net.solaara.beeyourself.transbee;

import dev.mayaqq.estrogen.registry.common.EstrogenEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.solaara.beeyourself.BeeYourself;
import net.solaara.beeyourself.BeeYourselfDataGenerator;
import net.solaara.beeyourself.ModUtils;

public class TransBeeEntity extends BeeEntity {

	private static final TrackedData<Integer> HAPPY_TIME = DataTracker.registerData(TransBeeEntity.class,
			TrackedDataHandlerRegistry.INTEGER);

	private int happy_time = 0;

	public TransBeeEntity(EntityType<? extends BeeEntity> entityType, World world) {
		super(entityType, world);
	}

	public static DefaultAttributeContainer.Builder create_transbee_attributes() {
		var bee_attribs = TransBeeEntity.createBeeAttributes();
		return bee_attribs;
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(HAPPY_TIME, 0);
	}

	@Override
	protected void mobTick() {
		super.mobTick();

		if (!this.world.isClient) {
			this.tick_happy_logic((ServerWorld) this.world);
		}
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

				for (var i = 0; i < 7; i += 1) {
					double d = this.random.nextGaussian() * 0.3;
					double e = this.random.nextGaussian() * 0.3;
					double f = this.random.nextGaussian() * 0.3;

					var p = BeeYourself.TRANS_FLAG_HEART_PARTICLE_1;
					switch (this.world.random.nextInt(0, 3)) {
						case 1:
							p = BeeYourself.TRANS_FLAG_HEART_PARTICLE_2;
							break;
						case 2:
							p = BeeYourself.TRANS_FLAG_HEART_PARTICLE_3;
							break;
					}

					ModUtils.spawnForcedParticles((ServerWorld) world, p,
							this.getParticleX(1.0),
							this.getRandomBodyY() + 0.5,
							this.getParticleZ(1.0), 3, d, e,
							f, 1);
				}

				this.setTarget(player);
				if (this.getAngryAt().equals(player.getUuid())) {
					this.setAngryAt(null);
				}
				player.sendMessage(
						new TranslatableText(
								"entity.bee-yourself.trans_bee.interaction.praise_message."
										+ this.random.nextInt(0, 3)),
						false);

				this.set_happy_time(100);

			}
			return ActionResult.SUCCESS;
		}
		return super.interactMob(player, hand);
	}

	public void writeCustomDataToNbt(NbtCompound nbt) {
		this.write_happiness_to_nbt(nbt);
		super.writeCustomDataToNbt(nbt);
	}

	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.read_happiness_from_nbt(this.world, nbt);
	}

	void tick_happy_logic(ServerWorld world) {

		if (this.get_happy_time() > 0) {
			this.set_happy_time(this.get_happy_time() - 1);
		}
	}

	public int get_happy_time() {
		return (Integer) this.dataTracker.get(HAPPY_TIME);
	}

	public void set_happy_time(int happy_time) {
		this.dataTracker.set(HAPPY_TIME, happy_time);
	}

	void read_happiness_from_nbt(World world, NbtCompound nbt) {
		BeeYourself.LOGGER.info("reading happy time from nbt " + nbt + " " + this.happy_time);
		this.set_happy_time(nbt.getInt("HappyTime"));
	}

	void write_happiness_to_nbt(NbtCompound nbt) {
		BeeYourself.LOGGER.info("writing happy time to nbt " + this.happy_time);
		nbt.putInt("HappyTime", this.get_happy_time());
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
