var cs6998Controllers = angular.module('cs6998Controllers', []);

cs6998Controllers.controller('cs6998MainCtrl', function ($scope, $routeParams, Customers, Addresses, $mdDialog) {

        console.log("firstName: " + $routeParams.firstName);
        console.log("lastName: " + $routeParams.lastName);
        console.log("fbUserId: " + $routeParams.fbUserId);
        console.log("fbAccessToken: " + $routeParams.fbAccessToken);

        $scope.showCreateCustomer = function(ev) {
                $mdDialog.show({
                  controller: DialogController,
                  templateUrl: 'dialog1.tmpl.html',
                  parent: angular.element(document.body),
                  targetEvent: ev,
                  locals: {
                    mode: 'create',
                    customer: null
                  },
                  clickOutsideToClose:false,
                  fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
                });
            };

        if($routeParams.newUser === 'true') {
            var newCustomer = {};
            newCustomer.firstName = $routeParams.firstName;
            newCustomer.lastName = $routeParams.lastName;
            newCustomer.fbUserId = $routeParams.fbUserId;
            newCustomer.fbAccessToken = $routeParams.fbAccessToken;

            $mdDialog.show({
                  controller: DialogController,
                  templateUrl: 'dialog1.tmpl.html',
                  parent: angular.element(document.body),
                  locals: {
                    mode: 'create',
                    customer: newCustomer
                  },
                  clickOutsideToClose:false,
                  fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
                });
        }

        function DialogController($scope, $mdDialog, Customers, Addresses, mode, customer) {
            $scope.customer = customer;
            $scope.mode = mode;
            $scope.showAddress = false;
            //$scope.autocompleteAddresses = [{number: "21", street: "clark", city: "selden", state:"ny"}, {number: "18", street: "clark", city: "selden", state:"ny"},{number: "19", street: "clark", city: "selden", state:"ny"},{number: "20", street: "clark", city: "selden", state:"ny"}];
            $scope.autocompleteAddresses = [];

            if(customer!==null && customer.addressRef) {
                $scope.address = Addresses.find({DPBarcode: customer.addressRef});
            }

            $scope.cancel = function() {
              $mdDialog.cancel();
            };

            $scope.create = function() {
                Customers.create($scope.customer, function(result) {

                }, function(error) {
                    var message = "";
                    if(error.status === 422) {
                        message = error.data.message;
                    } else {
                        message = "Failed to create customer!";
                    }
                    $mdDialog.show(
                            $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .title('Error')
                            .textContent(message)
                            .ariaLabel(message)
                            .ok('Ok')
                        );
                });
                $mdDialog.hide();
            };

            $scope.update = function() {
                Customers.update($scope.customer, function(result) {

                }, function(error) {
                    var message = "";
                    if(error.status === 422) {
                        message = error.data.message;
                    } else {
                        message = "Failed to update customer!";
                    }
                    $mdDialog.show(
                            $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .title('Error')
                            .textContent(message)
                            .ariaLabel(message)
                            .ok('Ok')
                        );
                });
                $mdDialog.hide();
            };

            $scope.delete = function() {
                Customers.delete($scope.customer, function(result) {

                }, function(error) {
                    var message = "";
                    if(error.status === 422) {
                        message = error.data.message;
                    } else {
                        message = "Failed to delete customer!";
                    }
                    $mdDialog.show(
                            $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .title('Error')
                            .textContent(message)
                            .ariaLabel(message)
                            .ok('Ok')
                        );
                });
                $mdDialog.hide();
            };

            $scope.addUpdateAddress = function(mode) {
                $scope.address.number = $scope.address.number + " ";
                Addresses.create($scope.address, function(result) {
                    $scope.customer.addressRef = result.DPBarcode;
                    Customers.update($scope.customer);
                    $scope.showAddress = false;
                }, function(error){
                    var message = "";
                        if(error.status === 422 || error.status === 404) {
                            message = error.data.message;
                        } else {
                            message = "Failed to " + mode + " address!";
                        }
                        $mdDialog.show(
                                $mdDialog.alert()
                                .clickOutsideToClose(true)
                                .title('Error')
                                .textContent(message)
                                .ariaLabel(message)
                                .ok('Ok')
                            );
                });
            }

            /*AutoComplete Related Methods Below*/
            $scope.selectedAddressChanged = function(newAddress) {
                $scope.address = {};
                $scope.address.number = newAddress.number;
                $scope.address.street = newAddress.street;
                $scope.address.city = newAddress.city;
                $scope.address.state = newAddress.state;
            }

            $scope.updateAutoCompleteList = function() {
                Addresses.suggestions({prefix: $scope.address.number + " " + $scope.address.street}, function(result){
                    //console.log(JSON.stringify(result));
                    var newSuggestions = [];
                    for(i=0; i<result.suggestions.length; i++) {
                        streetNumber = result.suggestions[i].street_line.substring(0, result.suggestions[i].street_line.indexOf(' '));
                        streetName = result.suggestions[i].street_line.substring(result.suggestions[i].street_line.indexOf(' '), result.suggestions[i].street_line.length);
                        newSuggestions[i] = {number: streetNumber, street: streetName, city: result.suggestions[i].city, state: result.suggestions[i].state};
                        //console.log("Suggestion " + i + " " + JSON.stringify(newSuggestions[i]));
                        $scope.autocompleteAddresses = newSuggestions;
                    }
                });
            }
        }

        $scope.showCustomer = function(ev, mode) {
            var confirm = $mdDialog.prompt()
              .title('Lookup Customer')
              .textContent('Enter Customer Email')
              .placeholder('Email')
              .ariaLabel('Email')
              .targetEvent(ev)
              .ok('Lookup')
              .cancel('Cancel');

            $mdDialog.show(confirm).then(
                function(result) {
                    Customers.find({email: result}, function (result) {
                        $mdDialog.show({
                          controller: DialogController,
                          templateUrl: 'dialog1.tmpl.html',
                          parent: angular.element(document.body),
                          targetEvent: ev,
                          locals: {
                            mode: mode,
                            customer: result
                          },
                          clickOutsideToClose:false,
                          fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
                        });
                    }, function (error) {
                        var message = "";
                        if(error.status === 422 || error.status === 404) {
                            message = error.data.message;
                        } else {
                            message = "Failed to lookup customer!";
                        }
                        $mdDialog.show(
                                $mdDialog.alert()
                                .clickOutsideToClose(true)
                                .title('Error')
                                .textContent(message)
                                .ariaLabel(message)
                                .ok('Ok')
                            );
                    });
                    }
                );
        };
    }
);