(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('WordOccurrencesDeleteController',WordOccurrencesDeleteController);

    WordOccurrencesDeleteController.$inject = ['$uibModalInstance', 'entity', 'WordOccurrences'];

    function WordOccurrencesDeleteController($uibModalInstance, entity, WordOccurrences) {
        var vm = this;

        vm.wordOccurrences = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WordOccurrences.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
