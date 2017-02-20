(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('WordRankDetailController', WordRankDetailController);

    WordRankDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WordRank', 'RankSnapshot', 'Word'];

    function WordRankDetailController($scope, $rootScope, $stateParams, previousState, entity, WordRank, RankSnapshot, Word) {
        var vm = this;

        vm.wordRank = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myserviceApp:wordRankUpdate', function(event, result) {
            vm.wordRank = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
