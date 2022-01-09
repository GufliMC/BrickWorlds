# BrickWorlds

An extension for [Minestom](https://github.com/Minestom/Minestom) to manage worlds that can be saved to the disk.

## Install

Get the latest jar file from [Github actions](https://github.com/MinestomBrick/BrickWorlds/actions) 
and place it in the extension folder of your minestom server.

Worlds are stored in the `worlds` directory at the root of your server.

## Commands

Don't worry, console can execute all commands ;)

| Command              | Permission           |
|----------------------|----------------------|
| /bw save-all         | brickworlds.save-all |
| /bw save (world)     | brickworlds.save     |
| /bw load (world)     | brickworlds.load     |
| /bw setspawn         | brickworlds.setspawn |
| /bw teleport (world) | brickworlds.teleport |

## Config

You can change the settings in the `config.json`.

Load worlds on extension startup.
```json
{
  "loadOnStartup": [
    "world"
  ]
}
```

Change the default world. This is the world players will spawn in. Must be present in `loadOnStartup`.
```json
{
  "defaultWorld": "world"
}
```

Change autosave interval in minutes. Use 0 to disable autosaving.
```json
{
  "autoSaveInterval": 10
}
```

## Credits

* The [Minestom](https://github.com/Minestom/Minestom) project

## Contributing

Check our [contributing info](CONTRIBUTING.md)
