# Create Tools
[Forge 1.18.2] A mod to dynamically generate tools based of mechanical components from Create

Concept: Modular tools system similar to that of tetra or tinkers construct, where you can combine create componenets to form more complex hand held tools. The number of components/tool can be limited 																			
by the SU capacity (Produced by an attached part such as hand crank or furnace engine) and kinetic force delivery (Number of cogs/belts). These will probably be assembled at some kind of workbench where																			
the type of tool (Drill, harvester, etc) can be selected then modifer components (blaze burner, flywheel, etc) attached aswell  as the parts of the tool. To stage out the usefulness of the tools drill heads etc																			
could be upgradeable aswell as the casing the tool starts as, perhaps from wood -> andesite alloy -> brass -> chromatic/radiant/shadow, each of the last three acting as equal but with unique effects similar to																			
providing alternatives to netherite, below is a table detailing what different components could do (early ideas, highly subject to change)															

## Component list

<hr/>

![image](https://user-images.githubusercontent.com/79579164/187468142-81b0175b-387d-48ff-9207-b1d82da9f91d.png)

## Art
<h5>All designs are work in progress and heavily subject to change</h5>


<details>
<summary>Concept Art (Click to show))</summary>
<br>

![tools_2](https://user-images.githubusercontent.com/79579164/187978556-2ab49cd1-eec0-4599-889f-8f125708810e.png)

![gui_poster_2](https://user-images.githubusercontent.com/79579164/187978734-25927d32-43c4-4a29-8117-4fcb63b994b6.png)

![showcase](https://user-images.githubusercontent.com/79579164/187979123-e9faab76-46c7-4807-9a88-e3636b84118c.png)
</details>


<details>
<summary>Generators (Click to show)</summary>
<br>

<hr/>

### Steam Engine
[Not implemented]
Takes in both water and fuel however delivers the most SU output allowing you to support more components for your tools. The longer it runs for at once the more efficient it becomes as the temperature rises peaking at a 1.5x increase. Making this very effective for more advanced and complex tools coming at the cost of being te hardest to maintain. However, any water placed within the tank will be evapourated in the nether making it unusable.

<img src="https://user-images.githubusercontent.com/79579164/194595733-e64d5bbf-10b8-4227-991f-0b8e138795c8.png" alt="drawing" style="width:400px;"/>

<hr/>

### Windmill Generator 
[Implemented]
Outputs a variable rate of SU dependant on y-height and weather you are undeground. If you venture too far down you may not be able to use a tool powered by a windmill generator meaning that it may not be as effective drills or when fighting bosses undeground, but could instead be used for farming or clearing large areas on the surface at a lower cost. When combined with the flywheel the windmill will store the player's movement and release it over time allowing you to briefly use the tool if you need it.

<img src="https://user-images.githubusercontent.com/79579164/194595697-f0b7e1a4-af20-4bf9-9f3b-ef463ffb01d9.png" alt="drawing" style="width:400px;"/>

<hr/>

### Furnace Engine
[Implemented]
Outputs a constant rate of SU similarly to furnace engine removed from Create in 0.5.0. It requires fuel to run matches the burn time for the fuel as the furnace. Outputting a lower ammount of SU than the steam engine however it can be used anywhere making it more vercatile and cheaper. It could be useful for less complex tools with only a few modifications or when exploring the nether for a advanced weapon.

<img src="https://user-images.githubusercontent.com/79579164/194596089-e3b00984-5e44-446f-97cc-aa83879b795b.png" alt="drawing" style="width:400px;"/>

<hr/>

### Hand Crank
[Implemented] The handcrank works similarly to a wind up toy where at the cost of hunger you can charge it up to a maximum 30 seconds of run time where the crank will slowly unwind outputting a small quantity of SU, making it useful for your first few tools or for tools that are used briefly and anywhere such as using a deployer to transport tile entities or mobs.

<img src="https://media.discordapp.net/attachments/1014563886491500615/1018250356574130206/unknown.png?width=810&height=581" alt="drawing" style="width:400px;"/>

### Pressure Generator
[Coming soon] - will use fuel from the copper back tank like a potato cannon

<hr/>

</details>

<details>
<summary> Components (Click to show)</summary>
* NOTICE - not all components that are planned or even implemented are here yet  *
<br/>

<hr/>

### Blaze Burner
When used with most tools the blaze burner will automatically smelt block and mob drops consuming a small amount of fuel in the process, as well as this it can also be used as flint and steel when needed. When combined with an encased fan it will blow out hot hair smelting collected items and burning entities ahead of it. When used on a potato cannon it will light your projectiles allowing you to burn your targets from a distance (Each of these effects are modifiable in config, by default potato cannon shots will not set blocks alight to avoid accidental greifing)

<hr/>

### Flywheel
The flywheel makes your tools more efficent, when used with a furnace or steam engine fuel will last longer allowing you to keep using them for longer. When used with a windmill your kinetic energy will be stored in the flywheel and then slowly outputed allowing you to temporarily gather a small amount of power when there is no wind such as when mining. When combined with a hand crank the flywheel allows for a longer time to be stored. More designs will be availabe when <a href=https://www.curseforge.com/minecraft/mc-mods/create-extended-flywheels>Extended Flywheels</a> is present.

<hr/>

### Filters
Filters allow you to restrict the items that enter your inventory either by filtering specific items or tags/groups preventing your inventory from becoming clogged when mining or when excavating large areas

<hr/>

### Mechanical Arm
The mechanical arm allows for blocks to be carefully broken like when using silk touch (configurable to allow for spawners to be moved - disabled by default) Aswell as this when building you can pick from a number of modes to help you such as randomizing placed blocks, replacing broken blocks with the item in your offhand and rotating blocks with out having to replace them so you dont need a wrench at all times. When combined with  the item vault food can be automatically pulled and feed to the player.

<hr/>

### Spyglass
The spyglass allows you to see into the distance (spyglass overlay is configurable) which can be particuarly useful when combined with the potato cannon for increased accuracy at distance

<hr/>

### Encased Fan
The encased fan is useful for many purposes, it has three states, push, pull and off. While push is active all mobs and items in a straigt line ahead will be blown away, when combined with a blaze burner this will also burn them and when stacked with a nozzle these effects will be distributed in a sector infront of the player. When pull is active entities will be pulled towads the player and item pickup range is increased, a blaze burner will cause any blown items to be smelted and again a nozzle will distribute these effects. As you may not want these at all times the fan can be toggled off at any time reducing the SU consumption.

<hr/>

### Extendo-Grip
The Extendo-grip increases the reach distance of your tools and in the case of the potato cannon causes your shots to be launched at greater speed (this can be very useful when combined with  a spyglass to handle the extra distance)

<hr/>

### Nixie Tube
The nixie tube displays your current fuel levels for any of the generators, time remaining on a handcrank and stress output on a windmill making it much quicker to check how long left you have. (This can also be enabled as  a HUD element). Aswell as this the nixie tubes will flash when near ores when combined with  a drill, near mobs when combined with any kind of weaponry such as the potato cannon or deployer (when in attack mode) and when your are low on fuel and are about to loose power.

<hr/>

### Electron Tube
The electron tube allows for dynamic lighting if mods such as magnesium extras are present so that your offhand can be used for food, a sheild etc when exploring aswell as this they will integrate with <a href=https://www.curseforge.com/minecraft/mc-mods/create-powderlogy>Powderlogy & Illumination<a/> creating special effects for each variant of tube.

<hr/>

### Clock
The clock will display either the ingame time or real world time on the tool (and toggle-ably in the ingame HUD) for when you accidentally spend far too long playing Minecraft and need to be reminded to do something productive.

<hr/>

### Item Vault
The item vault acts as a mini-backpack on your tool expanding its storage, fuel will automatically be pulled from this storage if you are using a steam engine or furnace engine. Aswell as this the deployer/mechanical arm can pull from this inventory when placing randomized blocks for greater variation in your builds. When combined with the mechanical arm once your inventory is filled items will automatically fill the vault to act as an overflow. (If the PR is accepted these vaults will also become dyeable)

<hr/>

### Millstone
The millsonte automatically mills picked up blocks  (applies before the blaze burner if it is active) to allow for quick resource processing at the cost of higher resource consomption than doing it using a millstone block. Aswell as this when combined with a potato cannon all shots will be shredded and turned into buckshot reducing there range and damage/projectile but vastly increasing the AOE of the shot. This can be stacked with the blaze burner for greater effect.

</details>