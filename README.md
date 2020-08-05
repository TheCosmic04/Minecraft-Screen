# MinecraftScreens

A minecraft plugin that allows to share your screen in minecraft.

## Requirements to screen share:
- nodejs
- python 2.7 (automaticly installed with nodejs)
- .NET framework (automaticly installed with nodejs)
- Visual C++ Build Tools (automaticly installed with nodejs)

## Client installation:
1. You will need to install [node js](https://nodejs.org/).
2. download the [node js client](/nodejs/client).
3. open a cmd in the folder and execute this command `npm i jimp` then `npm i robotjs` (**Note**: if there are some errors while installing `robotjs` try start a cmd as administrator and use `npm install --global windows-build-tools`).
4. now you can start the client by doing `node index.js`.

## Usage:
1. First you have to install the [plugin]() in the server by putting it in your server plugin folder.
2. Create a screen using the command `/screen create [width] <height>` (if the height is not provided the server will automaticly generate it based on the given width).
2. Once the screen was created it will give a token (if the token wasnt given you can get it with `/screen info all`), you can click on the token to copy it.
3. Once you have the token start the nodejs client using ina  cmd inside the folder `node index.js`.
4. The client will ask you the ip of the server, if the server is not on localhost type the server ip, then it will ask the port where the plugin server is opened (default: 1234) if the port was changed type the custom port, then fianlly it will ask you the screen token.
5. If everything was sucessfull the client will say the screen size in console and start shating the screen, if it wasnt sucessfull because the token is invalid or the screen is already begin used it will ask the screen token again.

## Planed features:
- [x] Support multiple screens at once.
- [ ] Config files.
- [ ] Screen interaction (mouse click and typing).
