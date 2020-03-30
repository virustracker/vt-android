/**
 * Created by admin on 29/03/20.
 */

var UserSettings = function (array) {
    this.className = UserSettings;

    this.shouldShareData = false;
    this.isMarkedAsInfected = false;
    this.didFinishOnboarding = false;

    if (array) {
        this.initWithArray(array);
    }
};
UserSettings.prototype = Object.create(UserSettings.prototype);
UserSettings.constructor = UserSettings;

UserSettings.prototype.initWithArray = function(array) {
    var pe = this;
    pe.shouldShareData = array["should_share_data"];
    pe.isMarkedAsInfected = array["is_marked_as_infected"];
    pe.didFinishOnboarding = array["did_finish_onboarding"];
};

UserSettings.prototype.getDictionary = function() {
    var array = {};
    array["should_share_data"] = this.shouldShareData;
    array["is_marked_as_infected"] = this.isMarkedAsInfected;
    array["did_finish_onboarding"] = this.didFinishOnboarding;
    return array;
};
