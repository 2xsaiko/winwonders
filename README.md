# Window Wonders

Simple mod that makes the game window keep its size and position between shutdowns.

Does not depend on Fabric API.

## Download

Get builds from [Jenkins](https://ci.dblsaiko.net/job/winwonders/) or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/winwonders).

## Using in your development environment

Merge this snippet with your Gradle buildscript:

```groovy
repositories {
    maven { url = 'https://maven.dblsaiko.net' }
}

dependencies {
    modRuntime 'net.dblsaiko:winwonders:0.1.0-+'
}
```
