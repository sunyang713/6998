// Define the REST resource service, allowing us to interact with it as a high level service
var cs6998Services = angular.module('cs6998Services', ['ngResource']);

cs6998Services.factory('Customers', ['$resource', function($resource) {
    return $resource('https://x768se4ytc.execute-api.us-east-1.amazonaws.com/prod/customers/',
        {},
        {
            "find": {
                url:'https://x768se4ytc.execute-api.us-east-1.amazonaws.com/prod/customers/:email',
                method: 'GET',
                params: {
                  email: '@email'
                },
                isArray: false
            },
            "create": {
                method: 'POST'
            },
            "update": {
                url:'https://x768se4ytc.execute-api.us-east-1.amazonaws.com/prod/customers/:email',
                method: 'PUT',
                params: {
                  email: '@email'
                }
            },
            "delete": {
                url:'https://x768se4ytc.execute-api.us-east-1.amazonaws.com/prod/customers/:email',
                method: 'DELETE',
                params: {
                  email: '@email'
                }
            },
        });
}]);

cs6998Services.factory('Addresses', ['$resource', function($resource) {
    return $resource('https://x768se4ytc.execute-api.us-east-1.amazonaws.com/prod/addresses/',
        {},
        {
            "create": {
                method: 'POST'
            },
            "find": {
                url: 'https://x768se4ytc.execute-api.us-east-1.amazonaws.com/prod/addresses/:DPBarcode',
                method: 'GET',
                params: {
                    DPBarcode: '@DPBarcode'
                },
                isArray:false
            },
            "suggestions": {
                url: 'https://us-autocomplete.api.smartystreets.com/suggest',
                method: 'GET',
                params: {
                    'prefix': '@prefix',
                    'auth-id': '875f48fe-a02c-5bd4-b3ce-2d2dd5feb71c',
                    'auth-token': 'M9L1L6PvPIFGY1RBpIC3'
                },
                isArray:false
            }
        });
}]);