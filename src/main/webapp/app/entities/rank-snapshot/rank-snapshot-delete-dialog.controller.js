(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('RankSnapshotDeleteController',RankSnapshotDeleteController);

    RankSnapshotDeleteController.$inject = ['$uibModalInstance', 'entity', 'RankSnapshot'];

    function RankSnapshotDeleteController($uibModalInstance, entity, RankSnapshot) {
        var vm = this;

        vm.rankSnapshot = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RankSnapshot.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
