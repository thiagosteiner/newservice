(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('RankSnapshotDialogController', RankSnapshotDialogController);

    RankSnapshotDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RankSnapshot', 'WordRank'];

    function RankSnapshotDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RankSnapshot, WordRank) {
        var vm = this;

        vm.rankSnapshot = entity;
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
            if (vm.rankSnapshot.id !== null) {
                RankSnapshot.update(vm.rankSnapshot, onSaveSuccess, onSaveError);
            } else {
                RankSnapshot.save(vm.rankSnapshot, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myserviceApp:rankSnapshotUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
