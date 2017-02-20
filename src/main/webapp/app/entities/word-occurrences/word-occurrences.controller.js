(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .controller('WordOccurrencesController', WordOccurrencesController);

    WordOccurrencesController.$inject = ['WordOccurrences'];

    function WordOccurrencesController(WordOccurrences) {
        var vm = this;

        vm.wordOccurrences = [];

        loadAll();

        function loadAll() {
            WordOccurrences.query(function(result) {
                vm.wordOccurrences = result;
                vm.searchQuery = null;
            });
        }
    }
})();
