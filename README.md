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
[Not Implemented] The handcrank works similarly to a wind up toy where at the cost of hunger you can charge it up to a maximum 30 seconds of run time where the crank will slowly unwind outputting a small quantity of SU, making it useful for your first few tools or for tools that are used briefly and anywhere such as using a deployer to transport tile entities or mobs.

<img src="https://media.discordapp.net/attachments/1014563886491500615/1018250356574130206/unknown.png?width=810&height=581" alt="drawing" style="width:400px;"/>

<hr/>

</details>
