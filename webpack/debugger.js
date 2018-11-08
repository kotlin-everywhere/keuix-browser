// 항상 하나의 js 파일만 생성되기 때문에 일단, test 코드가 실행될수 없도록 처리한다.
window.QUnit = {
    test: function () {
    },
    module: function() {
    }
};

var kt = require('../build/classes/test/keuix-browser-test');
kt.com.minek.kotlin.everywhere.keuix.testDebugger();