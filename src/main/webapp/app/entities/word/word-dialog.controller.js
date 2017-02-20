(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('WordDialogController', WordDialogController);

    WordDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Word', 'WordRank'];

    function WordDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Word, WordRank) {
        var vm = this;

        vm.word = entity;
        vm.clear = clear;
        vm.save = save;
        vm.wordranks = WordRank.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.word.id !== null) {
                Word.update(vm.word, onSaveSuccess, onSaveError);
            } else {
                Word.save(vm.word, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myserviceApp:wordUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
