# capacitor-niimbot-printer

Niimbot printer plugin

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
