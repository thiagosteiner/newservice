(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('RankSnapshotController', RankSnapshotController);

    RankSnapshotController.$inject = ['RankSnapshot'];

    function RankSnapshotController(RankSnapshot) {
        var vm = this;

        vm.rankSnapshots = [];

        loadAll();

        function loadAll() {
            RankSnapshot.query(function(result) {
                vm.rankSnapshots = result;
                vm.searchQuery = null;
            });
        }
    }
})();
