angular.module("lms.admin.profile")
	.factory('ProfileService', ['$http', function($http) {
		var self = this;
		return {
			
			getById : function(id){				
				var promise = $http.get(GLOBAL_URL + 'admin/profile/'+ id)
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			},
			update : function(users) {
				var promise = $http.post(GLOBAL_URL + 'admin/profile/save', users);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				return promise;
			},
			changePassword : function(users) {
				var promise = $http.post(GLOBAL_URL + 'admin/profile/changepassword', users);
				promise.success(function(data, status, headers, conf){
					return data;
				}).error(function(error){
					console.log(error);
					return error;
				});
				return promise;
			},
			forgotPassword : function(users) {
				var promise = $http.post(GLOBAL_URL + 'admin/profile/forgotPassword', users);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				return promise;
			},
		}
	}]);	