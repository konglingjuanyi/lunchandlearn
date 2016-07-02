/**
 * Created by DE007RA on 6/27/2016.
 */

angular.module('filters').filter('filesize', function() {
    return function(sizeInBytes) {
        var kbVal = parseInt(sizeInBytes);
        var unit = '';
        if(!isNaN(kbVal)) {
            if(kbVal < 1024) {
                sizeInBytes = sizeInBytes;
                unit = ' bytes';
            }
            else if(kbVal > 1024 & kbVal < 1048576) {
                sizeInBytes = (sizeInBytes / 1024);
                unit = ' KB';
            }
            else if(kbVal > 1048576) {
                sizeInBytes =(sizeInBytes / 1048576);
                unit = ' MB';
            }
        }
        sizeInBytes = sizeInBytes.toString();
        var index = sizeInBytes.indexOf('.');
        if((index + 3) < sizeInBytes.length) {
            sizeInBytes = sizeInBytes.substr(0, index + 3);
        }
        return sizeInBytes + unit;
    };
});
