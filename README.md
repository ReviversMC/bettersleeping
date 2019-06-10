# BetterSleeping for Minecraft

This mod adds sleep voting, sleep recovery and no-sleep debuffs!

 

Sleep voting allows nights to be skipped when a certain percentage of the players are asleep. This can be set via a gamerule (`percentRequiredToSleep`) per save!

 

Sleep recovery gives players regeneration for 15s when they have slept through the night. This can be enabled or disabled through the config file.

 

No-sleep debuffs are applied when a player has skipped some nights consecutively without sleeping. The effects get stronger the more nights the player is awake. This can be enabled or disabled, and the nights before the effect is applied can be configured through the config file as well.

The effects given are:
* Nausea (5s first night, 10s second night, so on)
* Slowness (50s, strength increases every other night)
* Mining fatigue (50s, strength increases every other night)
* Weakness (50s, strength increases every other night)
* Blindness (25s, only after the 2nd night of debuffs)

Messages are sent when a player sleeps, for skipping nights and for debuffs. These can all be configured as well. Each message can have a placeholder that is replaced:
* The night skipped message does not take any placeholders.
* The players asleep message can take:
  * `{asleep}` which is replaced with the number of players asleep
  * `{total}` which is replaced with the total number of players
  * `{percent}` which is replaced with the percentage of players that are asleep
* The debuff message can take `{nights}` which is replaced with the number of nights skipped.

The config file also has a formatting field that is used to format the messages. It is a list and can take the names of any of the standard colours and styles. The [Minecraft wiki](https://minecraft.gamepedia.com/Formatting_codes) has a full list.

 

The config file is found in the `config` folder and is called `bettersleeping_config.json`. If this file is deleted the mod will recreate it with the default settings at the next start-up.

See on [CurseForge](https://minecraft.curseforge.com/projects/bettersleeping).