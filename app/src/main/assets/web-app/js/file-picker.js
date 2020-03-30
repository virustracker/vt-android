/**
 * Created by Artur on 22.08.2017.
 */

var FilePicker = function () {
    this.className = FilePicker;
    this._idCounter = 0;
    this.elementId = null;
    this.fileInputList = [];
};
FilePicker.prototype = Object.create(FilePicker.prototype);
FilePicker.constructor = FilePicker;

FilePicker.prototype.openFileDialog = function (callback) {

    var that = this;

    var element = document.createElement("input");
    var input = $(element);
    var inputId = "file-picker-input-"+this._idCounter;
    input.attr("id", inputId);
    input.attr("type", "file");

    element.onchange = function(e) {
        if (callback) {
            var file = that.getFileFromInput(e.target);
            var reader = new FileReader();
            reader.onloadend = function(e) {
                callback(inputId, file, e.target.result, file.name);
            };
            reader.readAsDataURL(file);
        }
    };

    input.trigger("click");

    this.fileInputList.push(input);
    this._idCounter++;

    return false;
};

FilePicker.prototype.getFileFromInput = function(input) {
    var file = null;
    var inputFiles = input.files;
    if (inputFiles != null && inputFiles.length > 0) {
        file = inputFiles[0];
    }
    return file;
};