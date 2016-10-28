'use strict';

// Define the `phonecatApp` module
var phonecatApp = angular.module('phonecatApp', []);
/*
define([ "app",], function (app) {

    app().registerFilter("trusted", ["$sce", function ($sce) {
        return function (html) {
            if (typeof html== 'string')   
                return $sce.trustAsHtml(html);  
            return html;
        }
    }])

});
*/
// Define the `PhoneListController` controller on the `phonecatApp` module
phonecatApp.controller('PhoneListController', function PhoneListController($scope,$http) {
  
  var self = this;
  self.orderProp = 'age';

  $http.get('/search/search?w=汪小磊').then(function(rsp) {
        //self.phones = rsp.data;
        $scope.phones = rsp.data.data;
  });
  
  $scope.toggle = function(){
	  $http.get('/search/search?w='+$scope.w).then(function(rsp) {
	        //self.phones = rsp.data;
	        $scope.phones = rsp.data.data;
	  });
  }
  
});
phonecatApp.filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    }
});
