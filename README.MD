# LostThaumaturgy
This mod adds a lot of content from Thaumcraft 2 and adds plenty of neat and useful magical tools that you may need to use while playing.

# Translations
You can translate Lost Thaumaturgy to your language by contributing to this repository. Langs are located at src/main/resources/assets/lostthaumaturgy/lang

# How to add to your development environment
Include following code into your build.gradle:

>repositories {
>       maven {
>               name = "APengu Maven"
>               url = "https://raw.githubusercontent.com/APengu/PenguLibs/gh-pages"
>       }
>}
>
>dependencies {
>       deobfCompile "apengu:LostThaumaturgy:MCVERSION-LTVERSION:deobf"
>}

Where "MCVERSION" is Minecraft Version (for 1.12 AND 1.12.1, use "1.12.x") and "LTVERSION" is Lost Thaumaturgy Version starting from 12r.
