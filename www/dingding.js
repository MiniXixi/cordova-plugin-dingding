var exec = require('cordova/exec');

exports.isInstalled = function (arg0, success, error) {
    exec(success, error, 'Dingding', 'isInstalled', [arg0]);
};

exports.login = function (arg0, success, error) {
    exec(success, error, 'Dingding', 'login', [arg0]);
};

exports.share = function (arg0, success, error) {
    exec(success, error, 'Dingding', 'share', [arg0]);
};

