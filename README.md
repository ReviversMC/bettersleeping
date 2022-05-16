<img height="70" align="right" src="./bettersleeping-1.17/src/main/resources/assets/bettersleeping/icon.png">

# BetterSleeping for Minecraft
[![CurseForge downloads](https://cf.way2muchnoise.eu/bettersleeping-revived.svg)](https://www.curseforge.com/minecraft/mc-mods/bettersleeping-revived)
[![CurseForge versions](https://cf.way2muchnoise.eu/versions/bettersleeping-revived.svg)](https://www.curseforge.com/minecraft/mc-mods/bettersleeping-revived)
[![Modrinth downloads](https://img.shields.io/badge/dynamic/json?color=5da545&label=modrinth&prefix=downloads%20&query=downloads&url=https://api.modrinth.com/api/v1/mod/WRzU60Pt&style=flat&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMSAxMSIgd2lkdGg9IjE0LjY2NyIgaGVpZ2h0PSIxNC42NjciICB4bWxuczp2PSJodHRwczovL3ZlY3RhLmlvL25hbm8iPjxkZWZzPjxjbGlwUGF0aCBpZD0iQSI+PHBhdGggZD0iTTAgMGgxMXYxMUgweiIvPjwvY2xpcFBhdGg+PC9kZWZzPjxnIGNsaXAtcGF0aD0idXJsKCNBKSI+PHBhdGggZD0iTTEuMzA5IDcuODU3YTQuNjQgNC42NCAwIDAgMS0uNDYxLTEuMDYzSDBDLjU5MSA5LjIwNiAyLjc5NiAxMSA1LjQyMiAxMWMxLjk4MSAwIDMuNzIyLTEuMDIgNC43MTEtMi41NTZoMGwtLjc1LS4zNDVjLS44NTQgMS4yNjEtMi4zMSAyLjA5Mi0zLjk2MSAyLjA5MmE0Ljc4IDQuNzggMCAwIDEtMy4wMDUtMS4wNTVsMS44MDktMS40NzQuOTg0Ljg0NyAxLjkwNS0xLjAwM0w4LjE3NCA1LjgybC0uMzg0LS43ODYtMS4xMTYuNjM1LS41MTYuNjk0LS42MjYuMjM2LS44NzMtLjM4N2gwbC0uMjEzLS45MS4zNTUtLjU2Ljc4Ny0uMzcuODQ1LS45NTktLjcwMi0uNTEtMS44NzQuNzEzLTEuMzYyIDEuNjUxLjY0NSAxLjA5OC0xLjgzMSAxLjQ5MnptOS42MTQtMS40NEE1LjQ0IDUuNDQgMCAwIDAgMTEgNS41QzExIDIuNDY0IDguNTAxIDAgNS40MjIgMCAyLjc5NiAwIC41OTEgMS43OTQgMCA0LjIwNmguODQ4QzEuNDE5IDIuMjQ1IDMuMjUyLjgwOSA1LjQyMi44MDljMi42MjYgMCA0Ljc1OCAyLjEwMiA0Ljc1OCA0LjY5MSAwIC4xOS0uMDEyLjM3Ni0uMDM0LjU2bC43NzcuMzU3aDB6IiBmaWxsLXJ1bGU9ImV2ZW5vZGQiIGZpbGw9IiM1ZGE0MjYiLz48L2c+PC9zdmc+)](https://modrinth.com/mod/bettersleeping-revived)
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
