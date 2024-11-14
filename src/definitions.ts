export interface NiimbotPrinterPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
