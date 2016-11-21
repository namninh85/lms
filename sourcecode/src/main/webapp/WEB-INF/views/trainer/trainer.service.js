angular.module("lms.trainer", [])
angular.module("lms.trainer")
	.factory('TrainerService', ['$http', '$filter', function($http, $filter) {
		var self = this;
		
		return {
			getAll : function() {
				var promise = $http.get(GLOBAL_URL + 'trainer/getAll');
			
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			}
		
		}
		
	}]);