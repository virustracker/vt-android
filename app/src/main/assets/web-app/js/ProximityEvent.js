/**
 * Created by admin on 29/03/20.
 */

var ProximityEventDistance = {
    Close: 1,
    Medium: 2,
    Far: 3
};

var ProximityEvent = function (array) {
    this.className = ProximityEvent;

    this.distanceType = null;
    this.timestamp = null;
    this.duration = null;
    this.infectionState = null;
    this.isConfidential = null;

    if (array) {
        this.initWithArray(array);
    }
};
ProximityEvent.prototype = Object.create(ProximityEvent.prototype);
ProximityEvent.constructor = ProximityEvent;

ProximityEvent.prototype.initWithArray = function(array) {
    var pe = this;
    pe.distanceType = array["distance_type"];
    pe.timestamp = array["timestamp"];
    pe.duration = array["duration"];
    pe.infectionState = array["infection_state"];
    pe.isConfidential = array["is_confidential"];
};

ProximityEvent.prototype.isHighRisk = function() {
    var isHighRisk = false;
    if (this.distanceType == ProximityEventDistance.Close && this.duration >= 15 * 60) {
        isHighRisk = true;
    }
    return isHighRisk;
};
