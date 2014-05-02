
angular.module('sistCoopApp').controller('SucursalCreateController', ['$scope', function($scope) {
    $scope.contacts = ["hi@email.com", "hello@email.com"];

    $scope.add = function() {
        $scope.contacts.push($scope.contact);
        $scope.contact = "";
    }
}]);