import { Plugin } from '@capacitor/core'

export interface NiimbotPrinterPlugin extends Plugin {
  /**
   * Print a document using the Niimbot printer. Internally, this will open the Niimblue web app to print the document
   * @param options The options to use when printing the document
   */
  print(options: INiimbotPrintOptions): Promise<any>;
}

export interface INiimbotPrintOptions {
    /**
     * The compiled Niimblue web app URI. This is the URL to the Niimblue web app. You can get this URL by compiling the Niimblue web app.
     * in order to compile the Niimblue web app, you need to have the Niimblue web app source code. You can get the source code from https://github.com/MultiMote/niimblue.
     */
    niimblueUri: string;
    /**
     * After closing the Niimblue web app, the user will be redirected to this URL. This is your app's URL
     * Default: 'index.html'
     */
    redirectUri?: string;
    /**
     * Should show the the print preview dialog on load or not. Setting it to true will show the print preview dialog immediately
     * Default: false
     */
    preview?: boolean;
    /**
     * Whether to show the close floating button or not. Setting it to false will hide the close button
     * Default: true
     */
    displayFab?: boolean;
    /**
     * An optional message to show as a toast when navigating back from the NiimBlue web ui to the app
     */
    toast?: {
        /**
         * Whether to show the toast or not
         * Default: true
         */
        enabled?: boolean;
        /**
         * The message to show as a toast
         * Default: 'Redirecting...'
         */
        message?: string;
    }
}