angular.module("lms")
	.factory('CourseService', ['$http', function($http) {
		var self = this;
		
		return {
			getAll : function() {
				var promise = $http.get(GLOBAL_URL+'course/getAll');
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			},
			searchPaging : function(object) {
			
				var numberPage= object.page-1;
				var limit= object.count;

				var promise = $http.get(GLOBAL_URL+'course/searchPaging?page='+numberPage +'&count='+limit);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			},
			searchCourses : function() {
				var promise = $http.get(GLOBAL_URL+ 'course/getAll');				
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			},
			findById : function(id){
				var promise = $http.get(GLOBAL_URL+'course/'+ id)
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			},
			update : function(course) {
			
				var promise = $http.post(GLOBAL_URL+'course/update', course);
				promise.success(function(data, status, headers, conf){
					return data;
				});
							
				return promise;
			},
			detele : function(courseId) {
				
				var promise = $http.get(GLOBAL_URL+'course/detele/'+ courseId);
				promise.success(function(data, status, headers, conf){
					return data;
				});
							
				return promise; 
			},
			clone : function(courseClone) {
				console.log(courseClone.startDate);
				var promise = $http.post(GLOBAL_URL+'course/clone', courseClone);
				promise.success(function(data, status, headers, conf){
					return data;
				});
							
				return promise;
			},
		}
		
	}]);