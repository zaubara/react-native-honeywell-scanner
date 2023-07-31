package com.ami3go.honeywellscannerinterface;

import android.os.Build;
import android.util.Log;

import com.facebook.react.bridge.*;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.honeywell.aidc.*;
import com.honeywell.aidc.AidcManager.CreatedCallback;

import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.ami3go.honeywellscannerinterface.HoneywellScannerPackage.HoneyWellTAG;

public class HoneywellScannerModule extends ReactContextBaseJavaModule implements BarcodeReader.BarcodeListener {

    // Debugging
    private static final boolean D = true;

    private final ReactApplicationContext reactContext;
    private AidcManager manager;
    private BarcodeReader reader;

    private static final String BARCODE_READ_SUCCESS = "barcodeReadSuccess";
    private static final String BARCODE_READ_FAIL = "barcodeReadFail";

    public HoneywellScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "HoneywellScanner";
    }

    /**
     * Send event to javascript
     *
     * @param eventName Name of the event
     * @param params    Additional params
     */
    private void sendEvent(String eventName, @Nullable WritableMap params) {
        if (reactContext.hasActiveReactInstance()) {
            if (D) Log.d(HoneyWellTAG, "Sending event: " + eventName);
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        }
    }

    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {
        if (D) Log.d(HoneyWellTAG, "HoneywellBarcodeReader - Barcode scan read");
        WritableMap params = Arguments.createMap();
        params.putString("data", barcodeReadEvent.getBarcodeData());
        sendEvent(BARCODE_READ_SUCCESS, params);
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {
        if (D) Log.d(HoneyWellTAG, "HoneywellBarcodeReader - Barcode scan failed");
        sendEvent(BARCODE_READ_FAIL, null);
    }

    @ReactMethod
    public void addListener(String eventName) {
    }

    @ReactMethod
    public void removeListeners(Integer count) {
    }

    /*******************************/
    /** Methods Available from JS **/
    /*******************************/

    @ReactMethod
    public void startReader(final ReadableMap options, final Promise promise) {
        AidcManager.create(reactContext, new CreatedCallback() {
            @Override
            public void onCreated(AidcManager aidcManager) {
                manager = aidcManager;
                reader = manager.createBarcodeReader();
                if (reader != null) {
                    reader.addBarcodeListener(HoneywellScannerModule.this);
                    try {
                        reader.claim();
                        boolean ean8 = options.hasKey("ean8") && options.getBoolean("ean8"); // default false
                        boolean ean13 = options.hasKey("ean13") && options.getBoolean("ean13");
                        boolean code128 = !options.hasKey("code128") || options.getBoolean("code128"); // default true
                        boolean gs1128 = options.hasKey("gs1128") && options.getBoolean("gs1128");
                        boolean code39 = options.hasKey("code39") && options.getBoolean("code39");
                        boolean interleaved25 = options.hasKey("interleaved25") && options.getBoolean("interleaved25");
                        boolean datamatrix = !options.hasKey("datamatrix") || options.getBoolean("datamatrix");
                        boolean qrcode = !options.hasKey("qrcode") || options.getBoolean("qrcode");

                        reader.setProperty(BarcodeReader.PROPERTY_EAN_8_ENABLED, ean8);
                        reader.setProperty(BarcodeReader.PROPERTY_EAN_8_CHECK_DIGIT_TRANSMIT_ENABLED, ean8);

                        reader.setProperty(BarcodeReader.PROPERTY_EAN_13_ENABLED, ean13);
                        reader.setProperty(BarcodeReader.PROPERTY_EAN_13_CHECK_DIGIT_TRANSMIT_ENABLED, ean13);
                        reader.setProperty(BarcodeReader.PROPERTY_EAN_13_TWO_CHAR_ADDENDA_ENABLED, ean13);
                        reader.setProperty(BarcodeReader.PROPERTY_EAN_13_FIVE_CHAR_ADDENDA_ENABLED, ean13);

                        reader.setProperty(BarcodeReader.PROPERTY_CODE_128_ENABLED, code128);

                        reader.setProperty(BarcodeReader.PROPERTY_GS1_128_ENABLED, gs1128);

                        reader.setProperty(BarcodeReader.PROPERTY_CODE_39_ENABLED, code39);

                        reader.setProperty(BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED, interleaved25);

                        reader.setProperty(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, datamatrix);

                        reader.setProperty(BarcodeReader.PROPERTY_QR_CODE_ENABLED, qrcode);

                        promise.resolve(true);
                    } catch (ScannerUnavailableException | UnsupportedPropertyException e) {
                        promise.resolve(false);
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @ReactMethod
    public void stopReader(Promise promise) {
        try {
            if (reader != null) {
                reader.close();
            }
            if (manager != null) {
                manager.close();
            }
        } catch (Exception e) {
            promise.reject(e);
        }
        promise.resolve(null);
    }

    private boolean isCompatible() {
        // This... is not optimal. Need to find a better way to performantly check whether device has a Honeywell scanner
        return Build.BRAND.toLowerCase().contains("honeywell");
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("BARCODE_READ_SUCCESS", BARCODE_READ_SUCCESS);
        constants.put("BARCODE_READ_FAIL", BARCODE_READ_FAIL);
        constants.put("isCompatible", isCompatible());
        return constants;
    }

}
