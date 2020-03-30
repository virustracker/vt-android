/**
 * Created by Artur on 08.11.2019.
 */

var NativeMobileHandler = function () {
    this.className = NativeMobileHandler;
    this.callbackEventBackButton = null;
};
NativeMobileHandler.prototype = Object.create(NativeMobileHandler.prototype);
NativeMobileHandler.constructor = NativeMobileHandler;

NativeMobileHandler.prototype.eventBackButton = function() {
    if (this.callbackEventBackButton) {
        this.callbackEventBackButton();
    }
};