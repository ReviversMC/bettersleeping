<img height="70" align="right" src="./assets/icon.png">

# BetterSleeping for Minecraft
[![CurseForge downloads](https://cf.way2muchnoise.eu/bettersleeping-revived.svg)](https://www.curseforge.com/minecraft/mc-mods/bettersleeping-revived)
[![CurseForge versions](https://cf.way2muchnoise.eu/versions/bettersleeping-revived.svg)](https://www.curseforge.com/minecraft/mc-mods/bettersleeping-revived)
[![Modrinth downloads](https://img.shields.io/modrinth/dt/bettersleeping-revived?color=00AF5C&label=modrinth&style=flat&logo=modrinth)](https://modrinth.com/mod/bettersleeping-revived)
![Environment: server](https://img.shields.io/badge/environment-server-1976d2?style=flat)
[![Discord chat](https://img.shields.io/badge/chat%20on-discord-7289DA?logo=discord&logoColor=white)](https://discord.gg/6bTGYFppfz)

This mod adds sleep notifications, gives sleeping players buffs and debuffs to the ones who haven't slept in a long time!


### Sleep recovery
Sleep recovery gives players regeneration for 15s when they have slept through the night. This can be adjusted in the config file.


### No-sleep debuffs
No-sleep debuffs are applied when a player has skipped three nights consecutively without sleeping. The effects get stronger and last longer the more nights the player is awake. This can be enabled or disabled, and the nights before the effect is applied can be configured through the config file as well.

The effects given are:
- Slowness
- Weakness
- Nausea
- Mining fatigue
- Blindness


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
- The debuff message can take `{nightsAwake}` which is replaced with the number of nights skipped.

The config file also has a formatting field that is used to format the messages. It is a list and can take the names of any of the standard colours and styles. The Minecraft wiki has a [full list](https://minecraft.gamepedia.com/Formatting_codes).


### Config
The config file is found in the config folder and is called `bettersleeping.toml`. This folder should be in the same place as your mods folder (`.minecraft` for Windows). If this file is deleted the mod will recreate it with the default settings at the next start-up. If you're in singleplayer, you can also use the in-game configuration menu (via ModMenu)!
