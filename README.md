<div align="center">
  <h1>Niimbot printer plugin</h1>
  <p><strong>A capacitor plugin that connects to Niimbot printers via NiimBlue https://github.com/MultiMote/niimblue web ui</strong></p>
</div>

## How it works ?
This plugin essentially uses [niimblue](https://github.com/MultiMote/niimblue) to connect to NIIMBOT printers. The plugin provides an easy way to load and automate niimblue in webview via simple API.

## Having Issues ?
If you are facing issue related to niimblue, please go to the official repository and file it there. Issues related to the plugin itself can be created in this repsoitory

## Install

```bash
npm install capacitor-niimbot-printer
npx cap sync
```

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

| Prop              | Type                                                  | Description                                                                                                                                                                                                                                                                                              |
| ----------------- | ----------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`niimblueUri`** | <code>string</code>                                   | The compiled Niimblue web app URI. This is the URL to the Niimblue web app. You can get this URL by compiling the Niimblue web app. in order to compile the Niimblue web app, you need to have the Niimblue web app source code. You can get the source code from https://github.com/MultiMote/niimblue. |
| **`redirectUri`** | <code>string</code>                                   | After closing the Niimblue web app, the user will be redirected to this URL. This is your app's URL. This is the app redirect URL Default: 'index.html'                                                                                                                                                  |
| **`preview`**     | <code>boolean</code>                                  | Should show the the print preview dialog on load or not. Setting it to true will show the print preview dialog immediately Default: false                                                                                                                                                                |
| **`displayFab`**  | <code>boolean</code>                                  | Whether to show the close floating button or not. Setting it to false will hide the close button Default: true                                                                                                                                                                                           |
| **`toast`**       | <code>{ enabled?: boolean; message?: string; }</code> | An optional message to show as a toast when navigating back from the niimblue to the app                                                                                                                                                                                                                 |

</docgen-api>
