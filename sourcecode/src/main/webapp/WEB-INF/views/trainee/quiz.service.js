angular.module("lms.quiz")
	.factory('QuizService', ['$http', '$filter', function($http, $filter) {
		var self = this;
		
		return {
			getNextQuestion : function(courseId, topicId, quizMaterialId, traineeQuizId) {
				var promise = $http.get(GLOBAL_URL + 'trainee/next-question/' + courseId + '/' + topicId + '/' + quizMaterialId + '/' + traineeQuizId);
			
				promise.success(function(data, status, headers, conf){
				
					return data;
				});
				
				return promise;
			},
			ansQuestion : function(ans, traineeQuizDetailId, courseId, topicId, quizMaterialId, traineeQuizId) {
				
				var promise = $http.get(GLOBAL_URL + 'trainee/ans-question/' + courseId + '/' + topicId + '/' + quizMaterialId + '/' + traineeQuizId + '?ans=' + ans + "&traineeQuizDetailId=" + traineeQuizDetailId);
			
				promise.success(function(data, status, headers, conf){
				
					return data;
				});
				
				return promise;
			},
			skipQuestion : function(traineeQuizDetailId, courseId, topicId, quizMaterialId, traineeQuizId) {
				
				
				
				var promise = $http.get(GLOBAL_URL + 'trainee/skip-question/' + courseId + '/' + topicId + '/' + quizMaterialId + '/' + traineeQuizId + "?traineeQuizDetailId=" + traineeQuizDetailId);
			
				promise.success(function(data, status, headers, conf){
				
					return data;
				});
				
				return promise;
			},
			
			ansFinish : function(ans, traineeQuizDetailId, courseId, topicId, quizMaterialId, traineeQuizId) {
				
				var promise = $http.get(GLOBAL_URL + 'trainee/ans-finish/' + courseId + '/' + topicId + '/' + quizMaterialId + '/' + traineeQuizId);
			
				promise.success(function(data, status, headers, conf){
				
					return data;
				});
				
				return promise;
			},
			tableToExcel:function(tableId,worksheetName){
	            var table=$(tableId);
	            var ctx={worksheet:worksheetName,table:table.html()},
	                href=uri+base64(format(template,ctx));
	            return href;
	        }
			
				
			
		}
		
	}]);
