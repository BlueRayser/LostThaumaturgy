# Lost Thaumaturgy
Adding plenty of useful utilities from Thaumcraft 2

# How to add to your development environment
Include following code into your build.gradle:

>repositories {
>		maven {
>				name = "APengu Maven"
>				url = "https://raw.githubusercontent.com/APengu/PenguLibs/gh-pages"
>		}
>}
>
>dependencies {
>		deobfCompile "apengu:LostThaumaturgy:MCVERSION-LTVERSION:deobf"
>}

Where "MCVERSION" is Minecraft Version (for 1.12 AND 1.12.1, use "1.12.x") and "LTVERSION" is Lost Thaumaturgy Version starting from 12r.
