# react-native-honeywell-scanner-v2

This module is fork of react-native-honeywell-scanner.

## Getting started

`$ npm install react-native-honeywell-scanner-v2 --save`

## Usage

```javascript
import HoneywellScanner from 'react-native-honeywell-scanner-v2';

...

useEffect(() => {
        if( HoneywellScanner.isCompatible ) {
            HoneywellScanner.startReader({
                ean8: false,
                ean13: false,
                code128: true,
                gs1128: false,
                code39: false,
                interleaved25: false,
                datamatrix: true,
                qrcode: true,
            }).then((claimed) => {
                console.log(claimed ? 'Barcode reader is claimed' : 'Barcode reader is busy');
                HoneywellScanner.onBarcodeReadSuccess(event => {
                    console.log('Received data', event.data);
                });

            });


            return(
                () => {
                    HoneywellScanner.stopReader().then(() => {
                        console.log("Freedom!!");
                        HoneywellScanner.offBarcodeReadSuccess();
                    });
                }
            )
        }
    }, []);
```
