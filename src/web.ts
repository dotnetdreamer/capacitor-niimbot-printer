import { WebPlugin } from '@capacitor/core';

import type { INiimbotPrintOptions, NiimbotPrinterPlugin } from './definitions';

export class NiimbotPrinterWeb extends WebPlugin implements NiimbotPrinterPlugin {
  print(_: INiimbotPrintOptions): Promise<any> {
    throw new Error('Method not implemented.');
  }

  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
