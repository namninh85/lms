angular.module("lms.admin.trainer")
	.factory('TrainerService', ['$http', function($http) {
		var self = this;
		return {
			getAllTrainer : function() {
				var promise = $http.get(GLOBAL_URL + 'admin/trainer/getAll');
				promise.success(function(data, status, headers, conf){
					return data;
					console.log(data);
				});
				
				return promise;
			},
			getByIdTrainer : function(id){
				var promise = $http.get(GLOBAL_URL + 'admin/trainer/'+ id)
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			},
			getAllRolesByUserName : function(id) {
				var promise = $http.get(GLOBAL_URL + 'admin/trainer/getAllRolesByUserName/'+id);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			},
			getAllRolesNotBelongByUserName : function(id) {
				
				var promise = $http.get(GLOBAL_URL + 'admin/trainer/getAllRolesNotBelongByUserName/'+id);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			},
			saveTrainer : function(users) {
				
				var promise = $http.post(GLOBAL_URL + 'admin/trainer/save', users);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				return promise;
			},
			updateTrainer : function(users) {
				
				var promise = $http.post(GLOBAL_URL + 'admin/trainer/update', users);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				return promise;
			},
			deleteTrainer : function(id) {
				
				var promise = $http.post(GLOBAL_URL + 'admin/trainer/delete?id='+id);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				return promise;
			}
		}
	}]);