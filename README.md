# Niimbot printer plugin
A capacitor plugin that connects to Niimbot printers via [NiimBlue](https://github.com/MultiMote/niimblue) web ui

## How it works ?
This plugin essentially uses [NiimBlue](https://github.com/MultiMote/niimblue) to connect to NIIMBOT printers. The plugin provides an easy way to load and automate niimblue in webview via simple API.

## Having Issues ?
If you are facing issue related to NiimBlue, please go to the official repository and file it there. Issues related to the plugin itself can be created in this repsoitory

## Install

### Local version (Local Machine)
To use and install this plugin locally (instead of npm), follow:
1. `git clone https://github.com/dotnetdreamer/capacitor-niimbot-printer.git`
2. `npm i path_to_just_cloned_repo (e.g C:\Git\z_others\capacitor-niimbot-printer)`
3. Go to `this_plugin_path -> android -> gradle.properties` and uncomment `localPluginPath` property. Now change the value to your app root location (where you will use this plugin). The path must ends with '/' character
4. (Optional) This plugin has pre-compiled version of Niimlue web app inside `android/src/niimblue` folder. You can replace it with your own version of Niimblue web app by following compilation instructions [here](https://github.com/MultiMote/niimblue)

### Release version (NPM)
```bash
npm install capacitor-niimbot-printer
npx cap sync
```

> [!Important]
>  Make sure that `your_app -> android -> app -> src -> main -> assets -> public -> assets` has all the required files copied by the plugin during build time. You can look specifically for index.html and other js, fonts files. 

## API

<docgen-index>

* [`print(...)`](#print)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### print(...)

```typescript
print(options: INiimbotPrintOptions) => Promise<any>
```

Print a document using the Niimbot printer. Internally, this will open the Niimblue web app to print the document

| Param         | Type                                                                  | Description                                   |
| ------------- | --------------------------------------------------------------------- | --------------------------------------------- |
| **`options`** | <code><a href="#iniimbotprintoptions">INiimbotPrintOptions</a></code> | The options to use when printing the document |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### Interfaces


#### INiimbotPrintOptions

| Prop              | Type                                                  | Description                                                                                                                               |
| ----------------- | ----------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------- |
| **`redirectUri`** | <code>string</code>                                   | After closing the Niimblue web app, the user will be redirected to this URL. This is your app's URL Default: 'index.html'                 |
| **`preview`**     | <code>boolean</code>                                  | Should show the the print preview dialog on load or not. Setting it to true will show the print preview dialog immediately Default: false |
| **`displayFab`**  | <code>boolean</code>                                  | Whether to show the close floating button or not. Setting it to false will hide the close button Default: true                            |
| **`toast`**       | <code>{ enabled?: boolean; message?: string; }</code> | An optional message to show as a toast when navigating back from the NiimBlue web ui to the app                                           |

</docgen-api>
