(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('WordRankDialogController', WordRankDialogController);

    WordRankDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WordRank', 'RankSnapshot', 'Word'];

    function WordRankDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WordRank, RankSnapshot, Word) {
        var vm = this;

        vm.wordRank = entity;
        vm.clear = clear;
        vm.save = save;
        vm.ranksnapshots = RankSnapshot.query();
        vm.words = Word.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.wordRank.id !== null) {
                WordRank.update(vm.wordRank, onSaveSuccess, onSaveError);
            } else {
                WordRank.save(vm.wordRank, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myserviceApp:wordRankUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
