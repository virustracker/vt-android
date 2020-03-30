//
// RESTClient

var RESTClient = function () {
    this.className = RESTClient;
    this.rootUrl = null;
    this.autoCancelPreviousRequest = false;
    this._currentRequest = null;
    this.dataType = 'text';
    this.isForFormData = false;
};
RESTClient.prototype = Object.create(RESTClient.prototype);
RESTClient.constructor = RESTClient;
RESTClient.prototype.sendRequest = function (method, url, parameterList, callback) {

    if (method == RESTRequestMethod.FILE) {

        var paramPairList = [];
        for (var key in parameterList) {
            if (parameterList.hasOwnProperty(key)) {
                paramPairList.push(key+"="+parameterList[key]);
            }
        }

        window.location = this.rootUrl + url + "?" + paramPairList.join("&");

        if (callback) {
            callback();
        }
        return;
    }

    var that = this;
    if (this.rootUrl != null) {
        url = this.rootUrl + url;
    }
    if (that.autoCancelPreviousRequest && that._currentRequest != null) {
        that._currentRequest.abort();
        that._currentRequest = null;
    }

    var ajaxOptions = {
        type: method,
        url: url,
        data: parameterList,
        dataType: that.dataType,
        processData: that.processData,
        success: function (response) {
            //console.log(response);
            if (that.isJSON(response)) {
                response = JSON.parse(response, true);
            } else {
                console.log("[REST] Wrong JSON format in ("+url+"): "+response);
            }
            that._currentRequest = null;
            callback(response, true);
        },
        error: function (response) {
            //console.log(response);
            var response = {'responseCode': RESTResponseCode.FailureRequest};
            that._currentRequest = null;
            callback(response, false);
        }
    };
    if (that.isForFormData) {
        ajaxOptions["contentType"] = false;
        ajaxOptions["processData"] = false;
    }
    this._currentRequest = $.ajax(ajaxOptions);
    
};

RESTClient.prototype.isJSON = function(str) {
    try {
        return (JSON.parse(str) && !!str);
    } catch (e) {
        return false;
    }
}

var RESTResponseCode = {
    Success: 200,
    Failure: 400,
    Unauthorized: 401,
    FailureRequest: 404,
};

var RESTResponseKey = {
    Code: "responseCode",
    Message: "responseMessage",
    Data: "responseData"
};

var RESTRequestMethod = {
    GET: "GET",
    POST: "POST",
    FILE: "FILE"
};

//
// RESTViewServices

var RESTViewService = function (client) {
    this.className = RESTViewService;
    this.client = client;
};
RESTViewService.prototype = Object.create(RESTViewService.prototype);
RESTViewService.constructor = RESTViewService;
RESTViewService.prototype.getViewCode = function (viewType, callback) {
    this.client.sendRequest("GET", "/views" + viewType, null, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    });
};

//
// RESTSessionServices

var RESTSessionService = function (client) {
    this.className = RESTSessionService;
    this.client = client;
};
RESTSessionService.prototype = Object.create(RESTSessionService.prototype);
RESTSessionService.constructor = RESTSessionService;
RESTSessionService.prototype.login = function (login, password, callback) {
    this.client.sendRequest("POST", "/session/login", {
        'login': login,
        'password': password
    }, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseMessage']);
    });
};
RESTSessionService.prototype.logout = function (callback) {
    this.client.sendRequest("GET", "/session/logout", null, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    });
};
RESTSessionService.prototype.setVariableList = function (keyValueList, callback) {
    this.client.sendRequest("POST", "/session/variable", {"keyValueList": JSON.stringify(keyValueList)}, function (responseObject) {
        if (callback != null) {
            callback(responseObject['responseCode']);
        }
    });
};
RESTSessionService.prototype.getVariableList = function (keyList, callback) {
    this.client.sendRequest("GET", "/session/variable", {"keyList": JSON.stringify(keyList)}, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    });
};
RESTSessionService.prototype.isUserLogged = function (callback) {
    this.client.sendRequest("GET", "/session/islogged", null, function (responseObject) {
        callback(responseObject['responseCode'] == RESTResponseCode.Success);
    });
};

//
// RESTDeviceService

var RESTDeviceService = function (client) {
    this.className = RESTDeviceService;
    this.client = client;
};
RESTDeviceService.prototype = Object.create(RESTDeviceService.prototype);
RESTDeviceService.constructor = RESTDeviceService;
RESTDeviceService.prototype.getInstantData = function (deviceIdList, callback) {
    this.client.sendRequest("GET", "/deviceendpoint/devicelistinstantdata/" + JSON.stringify(deviceIdList), null, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    });
};
RESTDeviceService.prototype.getInstantDataList = function (requestDataList, callback) {
    this.client.sendRequest("GET", "/deviceendpoint/devicelistinstantdatapack/" + JSON.stringify(requestDataList), null, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    });
};
RESTDeviceService.prototype.getLoggedUserDevices = function (callback) {
    this.client.sendRequest("GET", "/deviceendpoint/loggeduserdevicelist/", null, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    });
};
RESTDeviceService.prototype.setIdOfGroupToEdit = function (groupId, callback) {
    this.client.sendRequest("POST", "/deviceendpoint/getdevicegrouptoupdate/", {"groupId": groupId}, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};
RESTDeviceService.prototype.showDeviceProperties = function (callback) {
    this.client.sendRequest("GET", "/deviceendpoint/deviceproperties/", null, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};
RESTDeviceService.prototype.storeDeviceGroupProperties = function (dataList, callback) {
    this.client.sendRequest("POST", "/deviceendpoint/devicegroupproperties/", {"dataList": dataList}, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};
RESTDeviceService.prototype.createNewDevice = function (dataList, callback) {
    this.client.sendRequest("POST", "/deviceendpoint/newdevice/", {"dataList": dataList}, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};
RESTDeviceService.prototype.removeDevice = function (deviceId, callback) {
    this.client.sendRequest("POST", "/deviceendpoint/removedevice/", {"deviceId": deviceId}, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};
RESTDeviceService.prototype.getDeviceData = function (deviceId, callback) {
    this.client.sendRequest("GET", "/deviceendpoint/devicedata/", {"deviceId": deviceId}, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};
RESTDeviceService.prototype.updateDevice = function (dataList, callback) {
    this.client.sendRequest("GET", "/deviceendpoint/updatedevice/", {"dataList": dataList}, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};
RESTDeviceService.prototype.getSettingsView = function (deviceId, callback) {
    this.client.sendRequest("GET", "/deviceendpoint/devicesettingsview/", {"deviceId": deviceId}, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};

//
// RESTReportService

var RESTReportService = function (client) {
    this.className = RESTReportService;
    this.client = client
};
RESTReportService.prototype = Object.create(RESTReportService.prototype);
RESTReportService.constructor = RESTReportService;
RESTReportService.prototype.setReport = function (dataList, callback) {
    this.client.sendRequest("POST", "/reportendpoint/setreport/", {"dataList": dataList}, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};
RESTReportService.prototype.getReportList = function (callback) {
    this.client.sendRequest("GET", "/reportendpoint/reportlist/", null, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};

RESTReportService.prototype.getReportTypeList = function (reportId, callback) {
    this.client.sendRequest("GET", "/reportendpoint/reporttypelist/" + JSON.stringify(reportId), null, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};
RESTReportService.prototype.setReportScheduler = function (dataList, callback) {
    this.client.sendRequest("POST", "/reportendpoint/setreportscheduler/", {"dataList": dataList}, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};
RESTReportService.prototype.getReportScheduleList = function (callback) {
    this.client.sendRequest("GET", "/reportendpoint/reportschedulelist/", null, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};
RESTReportService.prototype.deleteReportScheduler = function (schedulerId, callback) {
    this.client.sendRequest("POST", "/reportendpoint/deletereportscheduler/", {"schedulerId": schedulerId}, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};
RESTReportService.prototype.getReportScheduleData = function (schedulerId, callback) {
    this.client.sendRequest("GET", "/reportendpoint/reportscheduledata/" + JSON.stringify(schedulerId), null, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};
RESTReportService.prototype.setUpdateReportScheduler = function (dataList, callback) {
    this.client.sendRequest("POST", "/reportendpoint/setupdatereportscheduler/", {"dataList": dataList}, function (responseObject) {
        callback(responseObject['responseCode'], responseObject['responseData']);
    })
};

//
// Subcontractor

var RESTSubcontractorEndpoint = function(client) {
    this.className = RESTSubcontractorEndpoint;
    this.client = client;
    this.path = "/subcontractor";
};
RESTSubcontractorEndpoint.prototype = Object.create(RESTSubcontractorEndpoint.prototype);
RESTSubcontractorEndpoint.constructor = RESTSubcontractorEndpoint;

RESTSubcontractorEndpoint.prototype.setSubcontractorSelectOption = function(optionValue, callback) {
    this.client.sendRequest("POST", this.path+"/subcontractorselectoption", {'value': optionValue}, function(response) {
        callback(response['responseCode'], response['responseMessage']);
    });
};
RESTSubcontractorEndpoint.prototype.setSubcontractorTourSelectOption = function(optionValue, callback) {
    this.client.sendRequest("POST", this.path+"/subcontractortourselectoption", {'value': optionValue}, function(response) {
        callback(response['responseCode'], response['responseMessage']);
    });
};
RESTSubcontractorEndpoint.prototype.updateSubcontractorTour = function(jsonDataString, callback) {
    this.client.sendRequest("POST", this.path+"/subcontractortourupdate", {"jsonString": jsonDataString}, function(response) {
        callback(response['responseCode'], response['responseMessage']);
    });
};
RESTSubcontractorEndpoint.prototype.addSubcontractorTour = function(jsonDataString, callback) {
    this.client.sendRequest("POST", this.path+"/subcontractortouradd", {"jsonString": jsonDataString}, function(response) {
        callback(response['responseCode'], response['responseMessage']);
    });
};
RESTSubcontractorEndpoint.prototype.removeSubcontractorTour = function(tourId, callback) {
    this.client.sendRequest("POST", this.path+"/subcontractortourremove", {'tourId': tourId}, function(response) {
        callback(response['responseCode'], response['responseMessage']);
    });
};