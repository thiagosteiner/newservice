(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('WordDeleteController',WordDeleteController);

    WordDeleteController.$inject = ['$uibModalInstance', 'entity', 'Word'];

    function WordDeleteController($uibModalInstance, entity, Word) {
        var vm = this;

        vm.word = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Word.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
