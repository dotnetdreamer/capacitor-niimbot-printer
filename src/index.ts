import { registerPlugin } from '@capacitor/core';

import type { NiimbotPrinterPlugin } from './definitions';

const NiimbotPrinter = registerPlugin<NiimbotPrinterPlugin>('NiimbotPrinter', {
  web: () => import('./web').then((m) => new m.NiimbotPrinterWeb()),
});

export * from './definitions';
export { NiimbotPrinter };
