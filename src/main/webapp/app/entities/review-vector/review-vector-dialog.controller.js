(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('ReviewVectorDialogController', ReviewVectorDialogController);

    ReviewVectorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'ReviewVector'];

    function ReviewVectorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, ReviewVector) {
        var vm = this;

        vm.reviewVector = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.reviewVector.id !== null) {
                ReviewVector.update(vm.reviewVector, onSaveSuccess, onSaveError);
            } else {
                ReviewVector.save(vm.reviewVector, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myserviceApp:reviewVectorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
