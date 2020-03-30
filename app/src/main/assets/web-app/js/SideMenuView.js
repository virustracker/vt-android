/**
 * Created by admin on 03/07/19.
 */

class SideMenuView {

    constructor() {
        /** @var MenuView */
        this.sideMenuContentView = null;
        /** @var View */
        this.sideMenuRootView = null;
    }

    getCode() {
        return "<table width='100%' border='1'><tr valign='top'><td width='50%'>"+this.sideMenuContentView.getCode()+"</td><td>"+this.sideMenuRootView.getCode()+"</td></tr></table>";
    }
}