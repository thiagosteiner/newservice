(function() {
    'use strict';

    angular
        .module('myserviceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('word-rank', {
            parent: 'entity',
            url: '/word-rank',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WordRanks'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/word-rank/word-ranks.html',
                    controller: 'WordRankController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('word-rank-detail', {
            parent: 'word-rank',
            url: '/word-rank/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WordRank'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/word-rank/word-rank-detail.html',
                    controller: 'WordRankDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'WordRank', function($stateParams, WordRank) {
                    return WordRank.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'word-rank',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('word-rank-detail.edit', {
            parent: 'word-rank-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/word-rank/word-rank-dialog.html',
                    controller: 'WordRankDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WordRank', function(WordRank) {
                            return WordRank.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('word-rank.new', {
            parent: 'word-rank',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/word-rank/word-rank-dialog.html',
                    controller: 'WordRankDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                rank: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('word-rank', null, { reload: 'word-rank' });
                }, function() {
                    $state.go('word-rank');
                });
            }]
        })
        .state('word-rank.edit', {
            parent: 'word-rank',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/word-rank/word-rank-dialog.html',
                    controller: 'WordRankDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WordRank', function(WordRank) {
                            return WordRank.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('word-rank', null, { reload: 'word-rank' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('word-rank.delete', {
            parent: 'word-rank',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/word-rank/word-rank-delete-dialog.html',
                    controller: 'WordRankDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WordRank', function(WordRank) {
                            return WordRank.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('word-rank', null, { reload: 'word-rank' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
