# JamLib

A general multipurpose library for JamCoreModding's mods.

## Features

- Annotation based registration.
- Single class configuration.
- General utilities.
- NBT (de)serialization.
- Networking.
- Keybindings.
- Scheduling.
- Development environment 'auth' (use your username and skin in dev environments), as well as other utilities for development environments.

## Maven

JamLib is available at [Jamalam's maven](https://maven.jamalam.tech):

```groovy
repositories {
    maven {
        url = "https://maven.jamalam.tech/releases"
    }
}

dependencies {
    modImplementation("io.github.jamalam360:jamlib:{VERSION}")
}
```

## System Properties

- `jamlib.dev.disable-eula-auto-agree`: Disable the EULA auto-agree in development environments.
- `jamlib.dev.disable-offline-mode`: Disable using offline mode by default in development environments.
- `jamlib.dev.session.uuid`: Set your username in development environments.
- `jamlib.dev.session.skin`: Set your UUID in development environments.

## Links

- [Curseforge](https://www.curseforge.com/minecraft/mc-mods/jamlib)
- [Modrinth](https://modrinth.com/mod/jamlib)
- [Discord](https://discord.jamalam.tech)
