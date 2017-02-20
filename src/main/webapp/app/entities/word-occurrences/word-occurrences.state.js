(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('word-occurrences', {
            parent: 'entity',
            url: '/word-occurrences',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WordOccurrences'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/word-occurrences/word-occurrences.html',
                    controller: 'WordOccurrencesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('word-occurrences-detail', {
            parent: 'word-occurrences',
            url: '/word-occurrences/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WordOccurrences'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/word-occurrences/word-occurrences-detail.html',
                    controller: 'WordOccurrencesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'WordOccurrences', function($stateParams, WordOccurrences) {
                    return WordOccurrences.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'word-occurrences',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('word-occurrences-detail.edit', {
            parent: 'word-occurrences-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/word-occurrences/word-occurrences-dialog.html',
                    controller: 'WordOccurrencesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WordOccurrences', function(WordOccurrences) {
                            return WordOccurrences.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('word-occurrences.new', {
            parent: 'word-occurrences',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/word-occurrences/word-occurrences-dialog.html',
                    controller: 'WordOccurrencesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                amountoccurrences: null,
                                word: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('word-occurrences', null, { reload: 'word-occurrences' });
                }, function() {
                    $state.go('word-occurrences');
                });
            }]
        })
        .state('word-occurrences.edit', {
            parent: 'word-occurrences',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/word-occurrences/word-occurrences-dialog.html',
                    controller: 'WordOccurrencesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WordOccurrences', function(WordOccurrences) {
                            return WordOccurrences.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('word-occurrences', null, { reload: 'word-occurrences' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('word-occurrences.delete', {
            parent: 'word-occurrences',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/word-occurrences/word-occurrences-delete-dialog.html',
                    controller: 'WordOccurrencesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WordOccurrences', function(WordOccurrences) {
                            return WordOccurrences.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('word-occurrences', null, { reload: 'word-occurrences' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
