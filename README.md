# react-native-esb24-honeywell-scanner

This module is fork of react-native-honeywell-scanner with enabled EAN13 digit check transmit property.

## Getting started

`$ npm install react-native-esb24-honeywell-scanner --save`

## Usage
```javascript
import Esb24HoneywellScanner from 'react-native-esb24-honeywell-scanner';

...

useEffect(() => {
        if( Esb24HoneywellScanner.isCompatible ) {
            Esb24HoneywellScanner.startReader().then((claimed) => {
                console.log(claimed ? 'Barcode reader is claimed' : 'Barcode reader is busy');
                Esb24HoneywellScanner.onBarcodeReadSuccess(event => {
                    console.log('Received data', event.data);
                });

            });


            return(
                () => {
                    Esb24HoneywellScanner.stopReader().then(() => {
                        console.log("Freedom!!");
                        Esb24HoneywellScanner.offBarcodeReadSuccess();
                    });
                }
            )
        }
    }, []);
```