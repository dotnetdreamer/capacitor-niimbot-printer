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

* [`echo(...)`](#echo)
* [`print(...)`](#print)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### print(...)

```typescript
print(options: INiimbotPrintOptions) => Promise<any>
```

| Param         | Type                                                                  |
| ------------- | --------------------------------------------------------------------- |
| **`options`** | <code><a href="#iniimbotprintoptions">INiimbotPrintOptions</a></code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### Interfaces


#### INiimbotPrintOptions

| Prop              | Type                                                  |
| ----------------- | ----------------------------------------------------- |
| **`niimblueUri`** | <code>string</code>                                   |
| **`redirectUri`** | <code>string</code>                                   |
| **`preview`**     | <code>boolean</code>                                  |
| **`toast`**       | <code>{ enabled?: boolean; message?: string; }</code> |

</docgen-api>
