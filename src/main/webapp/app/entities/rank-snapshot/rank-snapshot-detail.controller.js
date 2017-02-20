(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('RankSnapshotDetailController', RankSnapshotDetailController);

    RankSnapshotDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RankSnapshot', 'WordRank'];

    function RankSnapshotDetailController($scope, $rootScope, $stateParams, previousState, entity, RankSnapshot, WordRank) {
        var vm = this;

        vm.rankSnapshot = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myserviceApp:rankSnapshotUpdate', function(event, result) {
            vm.rankSnapshot = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
