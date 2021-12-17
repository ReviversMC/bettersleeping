# BetterSleeping for Minecraft

This mod adds sleep notifications, gives sleeping players buffs and debuffs to the ones who haven't slept in a long time!


### Sleep recovery
Sleep recovery gives players regeneration for 20s when they have slept through the night. This can be enabled or disabled through the config file.


### No-sleep debuffs
No-sleep debuffs are applied when a player has skipped three nights consecutively without sleeping. The effects get stronger and last longer the more nights the player is awake. This can be enabled or disabled, and the nights before the effect is applied can be configured through the config file as well.

The effects given are:
- Nausea (5s first night, 10s second night, so on)
- Slowness (50s, strength increases every other night)
- Mining fatigue (50s, strength increases every other night)
- Weakness (50s, strength increases every other night)
- Blindness (25s, only after the 2nd night of debuffs)


### Messages
Messages are sent when a player sleeps, for skipping nights and for debuffs. These can all be configured as well. Each message can have placeholders that are replaced:
- The night skipped message does not take any placeholders.
- The players asleep message can take:
  - `{asleepPlayers}` which is replaced with the number of players asleep
  - `{totalPlayers}` which is replaced with the total number of players
  - `{asleepPercentage}` which is replaced with the percentage of players that are asleep
  - `{asleepPlayersRequired}` which is replaced with the number of players required to skip the night
  - `{asleepPercentageRequired}` which is replaced with the percentage of players required to skip the night
  - `{asleepPlayersAdditionallyNeeded}` which is replaced with the number of players additionally needed to sleep to skip the night
- The debuff message can take `{skippedNights}` which is replaced with the number of nights skipped.

The config file also has a formatting field that is used to format the messages. It is a list and can take the names of any of the standard colours and styles. The Minecraft wiki has a [full list](https://minecraft.gamepedia.com/Formatting_codes).


### Config
The config file is found in the config folder and is called `bettersleeping_config.json`. This folder should be in the same place as your mods folder (`.minecraft` for Windows). If this file is deleted the mod will recreate it with the default settings at the next start-up.
