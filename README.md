# MinecraftScreens

A 1.15.2 Minecraft plugin that allows you to share your screen in Minecraft. (this plugin was inspired by Fundy's video [I made Minecraft in minecraft](https://youtu.be/BNwQf6nuvMc))

## Requirements to screen share:
- nodejs
- python 2.7 (automatically installed with nodejs)
- .NET framework (automatically installed with nodejs)
- Visual C++ Build Tools (automatically installed with nodejs)

## Client installation:
1. You will need to install [node js](https://nodejs.org/).
2. download the [node js client](/nodejs).
3. open a cmd in the folder and execute this command `npm i jimp robotjs` (**Note**: if there are some errors while installing `robotjs` try to start a cmd as administrator and use `npm install --global windows-build-tools`).
4. now you can start the client by doing `node client.js`.

## Usage:
1. First, you have to install the [plugin](https://github.com/TheCosmic04/Minecraft-Screen/releases/tag/1.0) in the server by putting it in your server plugin folder.
2. Create a screen using the command `/screen create [width] <height>` (if the height is not provided the server will automatically generate it based on the given width).
2. Once the screen was created it will give a token (if the token wasn't given you can get it with `/screen info all`), you can click on the token to copy it.
3. Once you have the token start the nodejs client using in a cmd inside the folder `node client.js`.
4. The client will ask you the IP of the server, if the server is not on localhost type the server IP, then it will ask the port where the plugin server is opened (default: 1234) if the port was changed type the custom port, then finally it will ask you the screen token.
5. If everything was successful the client will say the screen size in the console and start sharing the screen, if it wasn't successful because the token is invalid or the screen is already begin used it will ask the screen token again.

## Command usage:
 - /screen create [width] <height> - Create a screen with the given width and height (**Note**: if the height wasnt given the plugin will generate it based on the width).
 - /screen delete [token] - will delete the screen with the given token.
 - /screen info [token|all] - will give info such as token, position, resolution, ... about a specific screen or all screens.
 - /screen setpixel [token] [pixel-x] [pixel-y] [block] - will change the specified pixel of the screen with the given block. 
 - /screen fill [token] [block] - will fill the scpecified screen with the given block.

## Edit and build:
Dependencies: spigot 1.15.2

Intellij:
1. create a new java project.
2. download [spigot build tools](https://hub.spigotmc.org/jenkins/job/BuildTools/) and use the command `java -jar BuildTools.jar --rev 1.15.2` to generate the library.
3. go in `File > Project Structure > Modules > Dependencies` and press the `+` button on the left, select `jars`, and add the jar file generated with the build tools.
4. download the [plugin src](/java).
5. put all the files in the src folder inside your project.
6. edit the code
7. after editing the code to build it go in `File > Project Structure > Artifacts` press the `+` button at the top, select `JAR > from module with dependencies`, then to build go to `Build > Build Artifacts > Build`.

## Planed features:
- [x] Support multiple screens at once.
- [ ] Config files.
- [ ] Config for the nodejs client.
- [ ] Screen persist after restarts.
- [ ] Screen interaction (mouse click and typing).
- [ ] Optimize immage rendering using workers.
