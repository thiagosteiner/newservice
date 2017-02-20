(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('WordOccurrencesDialogController', WordOccurrencesDialogController);

    WordOccurrencesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WordOccurrences', 'Review'];

    function WordOccurrencesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WordOccurrences, Review) {
        var vm = this;

        vm.wordOccurrences = entity;
        vm.clear = clear;
        vm.save = save;
        vm.reviews = Review.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.wordOccurrences.id !== null) {
                WordOccurrences.update(vm.wordOccurrences, onSaveSuccess, onSaveError);
            } else {
                WordOccurrences.save(vm.wordOccurrences, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myserviceApp:wordOccurrencesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
