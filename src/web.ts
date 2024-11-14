import { WebPlugin } from '@capacitor/core';

import type { NiimbotPrinterPlugin } from './definitions';

export class NiimbotPrinterWeb extends WebPlugin implements NiimbotPrinterPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
