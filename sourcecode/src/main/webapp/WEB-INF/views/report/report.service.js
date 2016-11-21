angular.module("lms.report")
	.factory('ReportService', ['$http', '$filter','$window', function($http, $filter,$window) {
		var self = this;
		var uri='data:application/vnd.ms-excel;base64,',
        template='<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>',
        base64=function(s){return $window.btoa(unescape(encodeURIComponent(s)));},
        format=function(s,c){return s.replace(/{(\w+)}/g,function(m,p){return c[p];})};
		return {	
		searchPaging : function(object, status) {			
			
			var numberPage= object.page-1;
			var limit= object.count;
			
			var promise = $http.get(GLOBAL_URL + 'report/courses?status=' + status + '&page='+numberPage +'&count='+limit);
			
			promise.success(function(data, status, headers, conf){
				return data;
			});
			
			return promise;
		},
		
		searchTrainees : function(object) {			
			var numberPage= object.page-1;
			var limit= object.count;
			var courseId = object.courseId;
			var promise = $http.get(GLOBAL_URL + 'report/trainees?page='+numberPage +'&count='+limit + '&courseId=' + courseId);
			promise.success(function(data, status, headers, conf){
				return data;
			});
			
			return promise;
		},
		
		getAllQuiz : function(object) {			
			var courseId = object.courseId;
			var promise = $http.get(GLOBAL_URL + 'report/getAllQuiz?courseId=' + courseId);
			promise.success(function(data, status, headers, conf){
				return data;
			});
			
			return promise;
		},
		
		getQuizSummary : function(courseId, materialId) {		
			
			var promise = $http.get(GLOBAL_URL + 'report/getQuizSummary?courseId=' + courseId + '&materialId=' + materialId);
			promise.success(function(data, status, headers, conf){
				return data;
			});
			
			return promise;
		},
		
		traineeDetailData : function(courseId, traineeId) {			
			
			
			var promise = $http.get(GLOBAL_URL + 'report/traineeDetailData?courseId='+courseId +'&traineeId='+traineeId);
			promise.success(function(data, status, headers, conf){
				return data;
			});
			
			return promise;
		},
		saveAssessment : function(bean) {			
			
			var promise = $http.post(GLOBAL_URL + 'report/saveAssessment', bean);
			promise.success(function(data, status, headers, conf){
				return data;
			});
			
			return promise;
		},
		
		addComment : function(bean) {			
			
			var promise = $http.post(GLOBAL_URL + 'report/addComment', bean);
			promise.success(function(data, status, headers, conf){
				return data;
			});
			
			return promise;
		},
		
		getComments : function(courseId, traineeId) {			
			
			var promise = $http.get(GLOBAL_URL + 'report/getComments?courseId='+courseId +'&traineeId='+traineeId);
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