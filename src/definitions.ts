import { Plugin } from '@capacitor/core'

export interface NiimbotPrinterPlugin extends Plugin {
  echo(options: { value: string }): Promise<{ value: string }>;

  print(options: INiimbotPrintOptions): Promise<any>;
}

export interface INiimbotPrintOptions {
    niimblueUri: string;
    redirectUri?: string;
    preview?: boolean;
    toast?: {
        enabled?: boolean;
        message?: string;
    }
}