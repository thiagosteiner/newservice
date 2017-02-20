(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('ReviewVectorController', ReviewVectorController);

    ReviewVectorController.$inject = ['DataUtils', 'ReviewVector'];

    function ReviewVectorController(DataUtils, ReviewVector) {
        var vm = this;

        vm.reviewVectors = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            ReviewVector.query(function(result) {
                vm.reviewVectors = result;
                vm.searchQuery = null;
            });
        }
    }
})();
