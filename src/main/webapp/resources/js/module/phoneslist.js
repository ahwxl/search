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
        //$scope.pageTotals = rsp.totals;
  });
  
  $scope.toggle = function(){
	  $http.get('/search/search?w='+$scope.w).then(function(rsp) {
	        //self.phones = rsp.data;
	        $scope.phones = rsp.data.data;
	        $scope.pageTotals = rsp.data.totals;
	        $scope.pageNo = rsp.data.pageNo;
	        $scope.allPages = rsp.data.allPages;
	        
	        options.currentPage = rsp.data.pageNo;
 	        options.totalPages = rsp.data.allPages==0?1:rsp.data.allPages;
	        
	        $('#pagebar').bootstrapPaginator(options);
		    $("#pagebar").addClass("col-md-12 col-md-offset-2 text-center");
		    $("#pagebar>ul").addClass("pagination pagination-centered");
	  });
  };
  
  var options = {
        currentPage: 1,
        totalPages:  1,
        size:"normal",
        alignment:"left",
        itemTexts: function (type, page, current) {
                switch (type) {
                case "first":
                    return "首页";
                case "prev":
                    return "上一页";
                case "next":
                    return "下一页";
                case "last":
                    return "最后一页";
                case "page":
                    return page;
                }
            },
        onPageClicked: function(event, originalEvent, type, page){
         $http.get('/search/search?w='+$scope.w+'&pageNo='+page).then(function(rsp) {
 	        $scope.phones = rsp.data.data;
 	        $scope.pageTotals = rsp.data.totals;
 	        $scope.pageNo = rsp.data.pageNo;
 	        $scope.allPages = rsp.data.allPages;
 	        
 	        options.currentPage = page;
 	        options.totalPages = rsp.data.allPages==0?1:rsp.data.allPages;
 	        
 	        $('#pagebar').bootstrapPaginator(options);
 		    $("#pagebar").addClass("col-md-12 col-md-offset-2 text-center");
 		    $("#pagebar>ul").addClass("pagination pagination-centered");
 	      });
        }
    };

});
phonecatApp.filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    }
});
