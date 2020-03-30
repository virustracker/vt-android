/**
 * Created by Artur on 08.11.2019.
 */

var Scanner = function () {
    this.className = Scanner;
    this.scanIndex = 0;
    this.scanCallbackMap = {};
    this.debugScanText = null;
};

Scanner.prototype.scan = function(callback) {
    if (typeof Android !== "undefined") {
        this.scanIndex++;
        this.scanCallbackMap[this.scanIndex] = callback;
        Android.scanBarcode(this.scanIndex);
    } else {
        alert("Native layer missing");
        if (this.debugScanText != null) {
            if (callback != null) {
                callback(this.debugScanText);
            }
        }
    }
};

Scanner.prototype.scanCallback = function(scanIndex, text) {
    if (typeof Android !== "undefined") {
        var callback = this.scanCallbackMap[this.scanIndex];
        if (callback != null) {
            callback(text);
        }
    } else {
        alert("Native layer missing");
    }
};