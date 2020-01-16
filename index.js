const ReactNative = require('react-native');
const { NativeModules, DeviceEventEmitter } = ReactNative;
const Esb24HoneywellScanner = NativeModules.Esb24HoneywellScanner || {};

/**
 * Listen for available events
 * @param  {String} eventName Name of event one of barcodeReadSuccess, barcodeReadFail
 * @param  {Function} handler Event handler
 */

var subscriptionBarcodeReadSuccess = null;
var subscriptionBarcodeReadFail = null;

Esb24HoneywellScanner.onBarcodeReadSuccess = (handler) => {
    subscriptionBarcodeReadSuccess = DeviceEventEmitter.addListener(Esb24HoneywellScanner.BARCODE_READ_SUCCESS, handler)
}

Esb24HoneywellScanner.onBarcodeReadFail = (handler) => {
    subscriptionBarcodeReadFail = DeviceEventEmitter.addListener(Esb24HoneywellScanner.BARCODE_READ_FAIL, handler)
}

/**
 * Stop listening for event
 * @param  {String} eventName Name of event one of barcodeReadSuccess, barcodeReadFail
 * @param  {Function} handler Event handler
 */
Esb24HoneywellScanner.offBarcodeReadSuccess = () => {
    subscriptionBarcodeReadSuccess.remove()
};
Esb24HoneywellScanner.offBarcodeReadFail = () => {
    subscriptionBarcodeReadFail.remove()
};

export default Esb24HoneywellScanner;
