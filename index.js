const ReactNative = require('react-native');
const { NativeModules, DeviceEventEmitter } = ReactNative;
const HoneywellScanner = NativeModules.HoneywellScanner || {};

/**
 * Listen for available events
 * @param  {String} eventName Name of event one of barcodeReadSuccess, barcodeReadFail
 * @param  {Function} handler Event handler
 */

var subscriptionBarcodeReadSuccess = null;
var subscriptionBarcodeReadFail = null;

HoneywellScanner.onBarcodeReadSuccess = (handler) => {
    subscriptionBarcodeReadSuccess = DeviceEventEmitter.addListener(HoneywellScanner.BARCODE_READ_SUCCESS, handler)
}

HoneywellScanner.onBarcodeReadFail = (handler) => {
    subscriptionBarcodeReadFail = DeviceEventEmitter.addListener(HoneywellScanner.BARCODE_READ_FAIL, handler)
}

/**
 * Stop listening for event
 * @param  {String} eventName Name of event one of barcodeReadSuccess, barcodeReadFail
 * @param  {Function} handler Event handler
 */
HoneywellScanner.offBarcodeReadSuccess = () => {
    subscriptionBarcodeReadSuccess.remove()
};
HoneywellScanner.offBarcodeReadFail = () => {
    subscriptionBarcodeReadFail.remove()
};

export default HoneywellScanner;
