/**
 * Created by admin on 29/03/20.
 */

var TokenEvent = function (array) {
    this.className = TokenEvent;

    this.timestamp = null;
    this.count = null;

    if (array) {
        this.initWithArray(array);
    }
};
TokenEvent.prototype = Object.create(TokenEvent.prototype);
TokenEvent.constructor = TokenEvent;

TokenEvent.prototype.initWithArray = function(array) {
    var pe = this;
    pe.timestamp = array["timestamp"];
    pe.count = array["count"];
};
