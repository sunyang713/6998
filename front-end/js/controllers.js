var cs6998Controllers = angular.module('cs6998Controllers', []);

cs6998Controllers.controller('cs6998MainCtrl', function ($scope, Customers, Addresses, $mdDialog) {

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

        function DialogController($scope, $mdDialog, Customers, Addresses, mode, customer) {
            $scope.customer = customer;
            $scope.mode = mode;

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