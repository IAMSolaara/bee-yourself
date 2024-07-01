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

/*
 * 
 * public interface Angerable {
 * String ANGER_TIME_KEY = "AngerTime";
 * String ANGRY_AT_KEY = "AngryAt";
 * 
 * int getAngerTime();
 * 
 * void setAngerTime(int angerTime);
 * 
 * @Nullable
 * UUID getAngryAt();
 * 
 * void setAngryAt(@Nullable UUID angryAt);
 * 
 * void chooseRandomAngerTime();
 * 
 * default void writeAngerToNbt(NbtCompound nbt) {
 * nbt.putInt("AngerTime", this.getAngerTime());
 * if (this.getAngryAt() != null) {
 * nbt.putUuid("AngryAt", this.getAngryAt());
 * }
 * 
 * }
 * 
 * default void readAngerFromNbt(World world, NbtCompound nbt) {
 * this.setAngerTime(nbt.getInt("AngerTime"));
 * if (world instanceof ServerWorld) {
 * if (!nbt.containsUuid("AngryAt")) {
 * this.setAngryAt((UUID)null);
 * } else {
 * UUID uUID = nbt.getUuid("AngryAt");
 * this.setAngryAt(uUID);
 * Entity entity = ((ServerWorld)world).getEntity(uUID);
 * if (entity != null) {
 * if (entity instanceof MobEntity) {
 * this.setAttacker((MobEntity)entity);
 * }
 * 
 * if (entity.getType() == EntityType.PLAYER) {
 * this.setAttacking((PlayerEntity)entity);
 * }
 * 
 * }
 * }
 * }
 * }
 * 
 * default void tickAngerLogic(ServerWorld world, boolean angerPersistent) {
 * LivingEntity livingEntity = this.getTarget();
 * UUID uUID = this.getAngryAt();
 * if ((livingEntity == null || livingEntity.isDead()) && uUID != null &&
 * world.getEntity(uUID) instanceof MobEntity) {
 * this.stopAnger();
 * } else {
 * if (livingEntity != null && !Objects.equals(uUID, livingEntity.getUuid())) {
 * this.setAngryAt(livingEntity.getUuid());
 * this.chooseRandomAngerTime();
 * }
 * 
 * if (this.getAngerTime() > 0 && (livingEntity == null ||
 * livingEntity.getType() != EntityType.PLAYER || !angerPersistent)) {
 * this.setAngerTime(this.getAngerTime() - 1);
 * if (this.getAngerTime() == 0) {
 * this.stopAnger();
 * }
 * }
 * 
 * }
 * }
 * 
 * default boolean shouldAngerAt(LivingEntity entity) {
 * if (!this.canTarget(entity)) {
 * return false;
 * } else {
 * return entity.getType() == EntityType.PLAYER &&
 * this.isUniversallyAngry(entity.world) ? true :
 * entity.getUuid().equals(this.getAngryAt());
 * }
 * }
 * 
 * default boolean isUniversallyAngry(World world) {
 * return world.getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER) &&
 * this.hasAngerTime() && this.getAngryAt() == null;
 * }
 * 
 * default boolean hasAngerTime() {
 * return this.getAngerTime() > 0;
 * }
 * 
 * default void forgive(PlayerEntity player) {
 * if (player.world.getGameRules().getBoolean(GameRules.FORGIVE_DEAD_PLAYERS)) {
 * if (player.getUuid().equals(this.getAngryAt())) {
 * this.stopAnger();
 * }
 * }
 * }
 * 
 * default void universallyAnger() {
 * this.stopAnger();
 * this.chooseRandomAngerTime();
 * }
 * 
 * default void stopAnger() {
 * this.setAttacker((LivingEntity)null);
 * this.setAngryAt((UUID)null);
 * this.setTarget((LivingEntity)null);
 * this.setAngerTime(0);
 * }
 * 
 * @Nullable
 * LivingEntity getAttacker();
 * 
 * void setAttacker(@Nullable LivingEntity attacker);
 * 
 * void setAttacking(@Nullable PlayerEntity attacking);
 * 
 * void setTarget(@Nullable LivingEntity target);
 * 
 * boolean canTarget(LivingEntity target);
 * 
 * @Nullable
 * LivingEntity getTarget();
 * }
 * 
 */
