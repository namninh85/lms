angular.module("lms")
	.factory('SpreadSheetService', ['$http', function($http) {
		var self = this;
		
		return {
			parser : function(fileId) {
				var promise = $http.get(GLOBAL_URL+ 'google/parser/file/'+fileId);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			},
			getByTitle : function(title) {
				var promise = $http.get(GLOBAL_URL+ 'google/spreadsheets?title='+title);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			},
			getByFileId : function(fileId) {
				var promise = $http.get(GLOBAL_URL+ 'google/file/'+fileId);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			},
			downloadFile : function(fileId) {
				var promise = $http.get(GLOBAL_URL+ 'google/downloadfile/'+fileId);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			}

		}
		
	}]);