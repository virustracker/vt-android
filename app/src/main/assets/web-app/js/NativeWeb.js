/**
 * Created by Artur on 08.11.2019.
 */

var NativeWebMethod = {
    Log: 0,
    CodeScannerStart: 10,
    CodeScannerPause: 11,
    CodeScannerContinue: 12,
    CodeScannerStop: 13,
    CodeGenerateHash: 14,
    GetProximityEventList: 15,
    AddInfectionRequest: 16,
    GetUserSettings: 17,
    SetUserSettings: 18,
    SyncData: 19
};

var NativeWeb = function () {
    this.className = NativeWeb;
    
    this.android = null;
    if (typeof Android !== "undefined") {
        this.android = Android;
    }
    
    this.ios = null;
    if (window.webkit && typeof window.webkit.messageHandlers.NativeWeb !== "undefined") {
        this.ios = window.webkit.messageHandlers.NativeWeb;
    }
    
    this.callbackEventBackButton = null;
    this.callbackProximityEventList = null;
    this.callbackUserSettings = null;
    this.callbackInfectionRequest = null;
    
    this.scanIndex = 0;
    this.scanCallbackMap = {};
    this.debugScanText = null;
};
NativeWeb.prototype = Object.create(NativeWeb.prototype);
NativeWeb.constructor = NativeWeb;

//
// From JS to Native

NativeWeb.prototype.iOSCall = function(method, parameterList) {
    if (this.ios) {
        if (parameterList == null) {
            parameterList = {};
        }
        parameterList["method"] = method;
        var parameterListString = JSON.stringify(parameterList);
        this.ios.postMessage(parameterListString);
    }
};

NativeWeb.prototype.log = function(message) {
    if (this.ios) {
        this.iOSCall(NativeWebMethod.Log, {"message": message});
    }
    else if (this.android) {
        this.android.log(message);
    }
    else {
        console.log(message);
    }
};

NativeWeb.prototype.getProximityEventList = function(from, to, callback) {
    if (callback) {
        this.callbackProximityEventList = function(data) {
            //data = '{"tokens":[{"timestamp": 1585094400000, "count": 645}, {"timestamp": 1585180800000, "count": 1215}, {"timestamp": 1585267200000, "count": 2617}, {"timestamp": 1585353600000, "count": 312}, {"timestamp": 1585440000000, "count": 3716}], "events":[{"infection_state":0,"timestamp":1585440000000,"is_confidential":false,"distance_type":1,"duration":1200000},{"infection_state":1,"timestamp":1585267200000,"is_confidential":true,"distance_type":3,"duration":200000}]}';
            var pack = JSON.parse(data);

            var eventList = [];
            var tokenList = [];
            if (pack) {
                if (pack["events"]) {
                    var events = pack["events"];
                    for (var i=0; i<events.length; i++) {
                        var item = events[i];
                        var event = new ProximityEvent(item);
                        if (event) {
                            eventList.push(event);
                        }
                    }
                }
                if (pack["tokens"]) {
                    var tokens = pack["tokens"];
                    for (var i=0; i<tokens.length; i++) {
                        var item = tokens[i];
                        var event = new TokenEvent(item);
                        if (event) {
                            tokenList.push(event);
                        }
                    }
                }
            }
            callback(eventList, tokenList);
        };
        if (this.ios) {
            this.iOSCall(NativeWebMethod.GetProximityEventList, {"from": from, "to": to});
        }
        else if (this.android) {
            this.android.getProximityEventList(from, to);
        }
        else {
            callback();
        }
    }
};

NativeWeb.prototype.setUserSettings = function(userSettings) {
    if (userSettings) {
        var settingsJson = JSON.stringify(userSettings.getDictionary());
        if (this.ios) {
            this.iOSCall(NativeWebMethod.SetUserSettings, userSettings.getDictionary());
        }
        else if (this.android) {
            this.android.setSettings(settingsJson);
        }
    }
};

NativeWeb.prototype.getUserSettings = function(callback) {
    if (callback) {
        this.callbackUserSettings = function(data) {
            var pack = JSON.parse(data);
            var settings = null;
            if (pack) {
                settings = new UserSettings(pack);
            }
            callback(settings);
        };
        if (this.ios) {
            this.iOSCall(NativeWebMethod.GetUserSettings);
        }
        else if (this.android) {
            this.android.getSettings();
        }
        else {
            callback(null);
        }
    }
};

NativeWeb.prototype.sendInfectionRequest = function(days, callback) {
    if (callback) {
        this.callbackInfectionRequest = function(success) {
            callback(success);
        };
        if (this.ios) {
            this.iOSCall(NativeWebMethod.AddInfectionRequest, {"days": days});
        }
        else if (this.android) {
            this.android.sendInfectionRequest(days);
        }
        else {
            callback([]);
        }
    }
};

NativeWeb.prototype.syncData = function() {
    if (this.ios) {
        this.iOSCall(NativeWebMethod.SyncData);
    }
    else if (this.android) {
        this.android.syncData();
    }
};