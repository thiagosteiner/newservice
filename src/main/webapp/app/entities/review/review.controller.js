(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('ReviewController', ReviewController);

    ReviewController.$inject = ['DataUtils', 'Review'];

    function ReviewController(DataUtils, Review) {
        var vm = this;

        vm.reviews = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            Review.query(function(result) {
                vm.reviews = result;
                vm.searchQuery = null;
            });
        }
    }
})();
