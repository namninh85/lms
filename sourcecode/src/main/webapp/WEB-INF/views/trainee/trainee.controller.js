angular.module("lms.trainee")
	.controller('TraineeController', ['$scope', 'TraineeService', 'NgTableParams', '$loading', '$location',
    function($scope,TraineeService, NgTableParams, $loading, $location){
		var self = $scope;
		self.courseId = 0;
		self.courses = null;
		self.mycourses = null;
		
		
		//load table
		if($location.path().indexOf('browse-courses') > 0 ){
	    self.courses = new NgTableParams({
	            page: 1, // show first page
	            count: 12 // count per page
	          }, {
	        filterDelay: 300,
	        counts :[],
	        getData: function(params) {
	        	$loading.start('courses');
	          // ajax request to api
	          return TraineeService.searchPaging(params.url()).then(function(data) {
	        	  $loading.finish('courses');
	            params.total(data.data.total);
	            return data.data.listResult;
	          });
	        }
	      });
		}
	    //my courses
		if($location.path().indexOf('my-courses') > 0 ){
		    self.mycourses = new NgTableParams({
	            page: 1, // show first page
	            count: 12 // count per page
	          }, {
	        filterDelay: 300,
	        counts :[],
	        getData: function(params) {
	        	$loading.start('courses');
	          // ajax request to api
	          return TraineeService.searchMyCourses(params.url()).then(function(data) {
	        	  $loading.finish('courses');
	        	  params.total(data.data.total);
	        	  return data.data.listResult;
	          });
	        }
	      });
		};
		
		//my courses
		if($location.path().indexOf('my-history') > 0 ){
		    self.mycourses = new NgTableParams({
	            page: 1, // show first page
	            count: 12 // count per page
	          }, {
	        filterDelay: 300,
	        counts :[],
	        getData: function(params) {
	        	$loading.start('courses');
	          // ajax request to api
	          return TraineeService.searchMyHistory(params.url()).then(function(data) {
	        	  $loading.finish('courses');
	        	  params.total(data.data.total);
	        	  return data.data.listResult;
	          });
	        }
	      });
		};
		
		
		
		self.loadfile = function(path) {
			  return GLOBAL_URL + 'file/download?fileName=' + path;
		  };
	    
	    
	    //update material status
	    self.updateMaterialStatus = function(courseId, materialId, noView) {
	        alert(courseId);
	        //save course
	        //self.updateMaterialStatus(courseId, materialId, noView);
	    }
	    
		
}]);	