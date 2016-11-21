angular.module("lms.trainee")
	.factory('TraineeService', ['$http', '$filter', function($http, $filter) {
		var self = this;
		
		return {
			getAll : function() {
				var promise = $http.get(GLOBAL_URL + 'trainee/getAll');
			
				promise.success(function(data, status, headers, conf){
				
					return data;
				});
				
				return promise;
			},
			getAllTrainee : function() {
				var promise = $http.get(GLOBAL_URL + 'admin/getAll');
				
				promise.success(function(data, status, headers, conf){
					console.log(data)
					return data;
				});
				
				return promise;
			},
		
		
		
		searchPaging : function(object) {			
			var numberPage= object.page-1;
			var limit= object.count;

			var promise = $http.get(GLOBAL_URL + 'trainee/searchPaging?page='+numberPage +'&count='+limit);
			promise.success(function(data, status, headers, conf){
				return data;
			});
			
			return promise;
		},
		searchMyCourses : function(object) {
			var numberPage= object.page-1;
			var limit= object.count;

			var promise = $http.get(GLOBAL_URL + 'trainee/searchMyCourses?page='+numberPage +'&count='+limit);
			promise.success(function(data, status, headers, conf){
				return data;
			});
			
			return promise;
		},
		
		searchMyHistory : function(object) {
			var numberPage= object.page-1;
			var limit= object.count;

			var promise = $http.get(GLOBAL_URL + 'trainee/searchMyHistory?page='+numberPage +'&count='+limit);
			promise.success(function(data, status, headers, conf){
				return data;
			});
			
			return promise;
		},

		updateMaterialStatus : function(courseId, materialId, noViewed) {
			return '';
		}
		
		}
		
	}]);