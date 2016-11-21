angular.module("lms.admin.trainee")
	.factory('TraineeService', ['$http','$window', function($http,$window) {
		var self = this;
		var uri='data:application/vnd.ms-excel;base64,',
        template='<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>',
        base64=function(s){return $window.btoa(unescape(encodeURIComponent(s)));},
        format=function(s,c){return s.replace(/{(\w+)}/g,function(m,p){return c[p];})};
		return {
			getAll : function() {
				var promise = $http.get(GLOBAL_URL + 'admin/user/getAll');
				promise.success(function(data, status, headers, conf){
					return data;
					console.log(data);
				});
				
				return promise;
			},
			getAllTranier : function() {
				var promise = $http.get(GLOBAL_URL + 'admin/user/getAllTrainer');
				promise.success(function(data, status, headers, conf){
					return data;
					console.log(data);
				});
				
				return promise;
			},
			getAllTraniee : function() {
				var promise = $http.get(GLOBAL_URL + 'admin/user/getAllTrainee');
				promise.success(function(data, status, headers, conf){
					return data;
					console.log(data);
				});
				
				return promise;
			},
			getByIdTrainee : function(id){
				var promise = $http.get(GLOBAL_URL + 'admin/user/'+ id)
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			},
			getAllRolesByUserName : function(id) {
				var promise = $http.get(GLOBAL_URL + 'admin/user/getAllRolesByUserName/'+id);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			},
			getAllRolesNotBelongByUserName : function(id) {
				
				var promise = $http.get(GLOBAL_URL + 'admin/user/getAllRolesNotBelongByUserName/'+id);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				
				return promise;
			},
			saveTrainee : function(users) {
				var promise = $http.post(GLOBAL_URL + 'admin/user/save', users);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				return promise;
			},
			changePassword : function(users) {
				var promise = $http.post(GLOBAL_URL + 'admin/user/changepwd', users);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				return promise;
			},
			updateTrainee : function(users) {
				
				var promise = $http.post(GLOBAL_URL + 'admin/user/update', users);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				return promise;
			},
			deleteTrainee : function(id) {
				
				var promise = $http.post(GLOBAL_URL + 'admin/user/delete?id='+id);
				promise.success(function(data, status, headers, conf){
					return data;
				});
				return promise;
			},
			tableToExcel:function(tableId,worksheetName){
                var table=$(tableId),
                    ctx={worksheet:worksheetName,table:table.html()},
                    href=uri+base64(format(template,ctx));
                return href;
            }
		}
	}]);