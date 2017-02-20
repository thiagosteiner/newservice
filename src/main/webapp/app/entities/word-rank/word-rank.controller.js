(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('WordRankController', WordRankController);

    WordRankController.$inject = ['WordRank'];

    function WordRankController(WordRank) {
        var vm = this;

        vm.wordRanks = [];

        loadAll();

        function loadAll() {
            WordRank.query(function(result) {
                vm.wordRanks = result;
                vm.searchQuery = null;
            });
        }
    }
})();
