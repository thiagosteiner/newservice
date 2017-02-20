(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('WordRankDeleteController',WordRankDeleteController);

    WordRankDeleteController.$inject = ['$uibModalInstance', 'entity', 'WordRank'];

    function WordRankDeleteController($uibModalInstance, entity, WordRank) {
        var vm = this;

        vm.wordRank = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WordRank.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
