angular.module("lms")
	.factory('TopicService', ['$http', function($http) {
		var self = this;
		
		return {
			findById : function(id){
				var promise = $http.get(GLOBAL_URL + 'topic/'+ id)
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			},
			update : function(topic) {
			
				var promise = $http.post(GLOBAL_URL + 'topic/update', topic);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				
				return promise;
			}	
		}
		
	}]);