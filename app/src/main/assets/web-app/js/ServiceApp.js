/**
 * Created by admin on 03/07/19.
 */

var ServiceApp = function () {
    this.className = ServiceApp;
    this.containerId = null;
};
ServiceApp.prototype = Object.create(ServiceApp.prototype);
ServiceApp.constructor = ServiceApp;
ServiceApp.prototype.initialize = function () {

    var rootElement = document.getElementById(this.containerId);

    var appContainer = document.createElement("div");
    rootElement.appendChild(appContainer);

    var sideMenuView = new SideMenuView();
    sideMenuView.sideMenuContentView = new MenuView();
    sideMenuView.sideMenuRootView = new ContractListView();

    sideMenuView.sideMenuContentView.menuSelectionCallback = function(identifier) {
        if (identifier == "menu 1") {
            sideMenuView.sideMenuRootView = new ContractListView();
        }
        else if (identifier == "menu 2") {
            sideMenuView.sideMenuRootView = new SettingsView();
        }
        appContainer.innerHTML = sideMenuView.getCode();
    };

    appContainer.innerHTML = sideMenuView.getCode();

};