package com.github.reviversmc.bettersleeping.events;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.text.StringSubstitutor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import com.github.reviversmc.bettersleeping.BetterSleeping;
import com.github.reviversmc.bettersleeping.config.BetterSleepingConfig.Debuffs.Debuff;
import com.github.reviversmc.bettersleeping.config.BetterSleepingConfig.Debuffs.LeveledDebuff;

public abstract class EventHandlerBase {
	protected abstract void sendPlayerMessage(PlayerEntity player, String message);
	protected abstract ServerWorld getServerWorld(ServerPlayerEntity player);
	protected abstract boolean isBedWorking(DimensionType dimension);

	public void onTick(MinecraftServer server) {
		if (!BetterSleeping.config.buffs.applySleepBuffs) {
			return;
		}

		// Apply debuffs every morning to everyone who hasn't slept in a while
		for (ServerWorld world : server.getWorlds()) {
			if (world.getTimeOfDay() % 24000 == 1) {
				List<ServerPlayerEntity> players = world.getPlayers();

				// Night skipped message
				if (BetterSleeping.config.messages.sendNightSkippedMessageToEveryone) {
					for (ServerPlayerEntity player : players) {
						sendPlayerMessage(player, BetterSleeping.config.messages.nightSkippedMessage);
					}
				}

				// Debuffs
				if (!BetterSleeping.config.debuffs.applyAwakeDebuffs) {
					return;
				}

				if (players.size() <= 1 && !BetterSleeping.config.debuffs.applyAwakeDebuffsWhenAloneOnServer) {
					return;
				}

				for (ServerPlayerEntity player : players) {
					// Only apply to survival/adventure mode
					if (player.isSpectator() || player.isCreative()) {
						return;
					}

					// Only apply in dimensions you can actually sleep in
					if (!isBedWorking(getServerWorld(player).getDimension())) {
						return;
					}

					int nightsAwake = player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST)) / 24000;
					applyDebuffs(player, nightsAwake);
				}
			}
		}
	}

	// TODO: Remove this enum and make debuffs dynamic (user-addable)
	private enum DebuffWithId {
		SLOWNESS(BetterSleeping.config.debuffs.slowness, StatusEffects.SLOWNESS),
		WEAKNESS(BetterSleeping.config.debuffs.weakness, StatusEffects.WEAKNESS),
		NAUSEA(BetterSleeping.config.debuffs.nausea, StatusEffects.NAUSEA),
		MINING_FATIGUE(BetterSleeping.config.debuffs.miningFatigue, StatusEffects.MINING_FATIGUE),
		BLINDNESS(BetterSleeping.config.debuffs.blindness, StatusEffects.BLINDNESS);

		public final Debuff config;
		public final StatusEffect id;

		DebuffWithId(Debuff debuff, StatusEffect id) {
			this.config = debuff;
			this.id = id;
		}
	}

	private void applyDebuffs(ServerPlayerEntity player, int nightsAwake) {
		boolean debuffsApplied = false;
		int nightsAwakeToPunish;

		for (DebuffWithId debuff : DebuffWithId.values()) {
			nightsAwakeToPunish = nightsAwake - debuff.config.allowedAwakeNightsBeforeActivating();

			if (nightsAwakeToPunish >= 1) {
				debuffsApplied = true;

				int duration = Math.min(
						debuff.config.maxDuration() * 20, // We want seconds here, not ticks
						Math.round(Math.round(
							debuff.config.baseDuration() * 20
							* Math.pow(
								debuff.config.durationAmplifier(),
								nightsAwakeToPunish - 1
							)
						))
				);

				int additionalEffectLevels = 0;

				if (debuff.config instanceof LeveledDebuff) {
					additionalEffectLevels = Math.min(
						// Max allowed level for this debuff
						((LeveledDebuff) debuff.config).maxLevel,
						// Desired effect level according to formula
						Math.round(Math.round(Math.pow(
							((LeveledDebuff) debuff.config).levelAmplifier,
							nightsAwakeToPunish - 1
						)))
					) - 1;
				}

				player.addStatusEffect(new StatusEffectInstance(debuff.id, duration, additionalEffectLevels));
			}
		}

		if (!debuffsApplied) {
			return;
		}

		HashMap<String, String> args = new HashMap<>();
		args.put("nightsAwake", NumberFormat.getInstance().format(nightsAwake));
		String debuffMessage = StringSubstitutor.replace(BetterSleeping.config.messages.debuffMessage, args, "{", "}");
		sendPlayerMessage(player, debuffMessage);
	}

	public void onSleep(PlayerEntity player) {
		if (!(player instanceof ServerPlayerEntity)) {
			return;
		}

		// Send asleep message to every player
		sendAsleepMessage(player.getEntityWorld());
	}

	public void onWakeUp(PlayerEntity player) {
		if (!(player instanceof ServerPlayerEntity)) {
			return;
		}

		if (player.getEntityWorld().getTimeOfDay() % 24000 != 0) {
			// Player has aborted the sleeping process, since it's not yet morning
			// Resend asleep message to every player
			sendAsleepMessage(player.getEntityWorld());
			return;
		}

		// Remove debuffs
		if (BetterSleeping.config.debuffs.applyAwakeDebuffs) {
			player.removeStatusEffect(StatusEffects.NAUSEA);
			player.removeStatusEffect(StatusEffects.SLOWNESS);
			player.removeStatusEffect(StatusEffects.WEAKNESS);
			player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
			player.removeStatusEffect(StatusEffects.BLINDNESS);
		}

		// Apply buffs
		if (BetterSleeping.config.buffs.applySleepBuffs) {
			player.addStatusEffect(new StatusEffectInstance(
					StatusEffects.REGENERATION,
					BetterSleeping.config.buffs.regenerationDuration * 20,
					BetterSleeping.config.buffs.regenerationLevel - 1));
		}

		if (!BetterSleeping.config.messages.sendNightSkippedMessageToEveryone) {
			sendPlayerMessage(player, BetterSleeping.config.messages.nightSkippedMessage);
		}
	}

	private void sendAsleepMessage(World world) {
		List<? extends PlayerEntity> players = world.getPlayers();
		int sleepingPlayerCount = (int) players.stream().filter(LivingEntity::isSleeping).count();

		if (players.size() <= 1) {
			return;
		}

		int percentageNeededToSkipNight = world.getServer().getGameRules().getInt(GameRules.PLAYERS_SLEEPING_PERCENTAGE);
		int playersNeededToSkipNight = MathHelper.ceil((players.size() * percentageNeededToSkipNight / 100.0f));
		int playersAdditionallyNeeded = playersNeededToSkipNight - sleepingPlayerCount;

		HashMap<String, String> args = new HashMap<>();
		args.put("asleepPlayers", formatNumber(sleepingPlayerCount));
		args.put("totalPlayers", formatNumber(players.size()));
		args.put("asleepPercentage", formatNumber((sleepingPlayerCount * 100) / players.size()));
		args.put("asleepPlayersRequired", formatNumber(playersNeededToSkipNight));
		args.put("asleepPercentageRequired", formatNumber(percentageNeededToSkipNight));
		args.put("asleepPlayersAdditionallyNeeded", formatNumber(playersAdditionallyNeeded));

		String sleepingMessage;

		if (playersAdditionallyNeeded > 0) {
			sleepingMessage = StringSubstitutor.replace(BetterSleeping.config.messages.notEnoughPlayersAsleepMessage, args, "{", "}");
		} else {
			sleepingMessage = StringSubstitutor.replace(BetterSleeping.config.messages.playersAsleepMessage, args, "{", "}");
		}

		players.forEach(player -> {
			if (!(player instanceof ServerPlayerEntity)) {
				return;
			}

			sendPlayerMessage(player, sleepingMessage);
		});
	}

	private String formatNumber(int number) {
		return NumberFormat.getInstance().format(number);
	}
}
