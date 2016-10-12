var cs6998App = angular.module('cs6998App', [
    'ngRoute',
    'ngAnimate',
    'ngSanitize',
    'ngMaterial',
    'cs6998Services',
    'cs6998Controllers'
]);

cs6998App.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.
        when('/home', {
                templateUrl : 'partials/home.html',
                controller : 'cs6998MainCtrl'
        }).
        otherwise({
                redirectTo : '/home'
            });
        }
]);
