angular.module("lms.quiz-review")
	.factory('QuizReviewService', ['$http', '$filter', function($http, $filter) {
		var self = this;
		
		return {
			getNextQuestion : function(courseId, topicId, quizMaterialId, traineeQuizId) {
				var promise = $http.get(GLOBAL_URL + 'trainee/next-question/' + courseId + '/' + topicId + '/' + quizMaterialId + '/' + traineeQuizId);
			
				promise.success(function(data, status, headers, conf){
				
					return data;
				});
				
				return promise;
			},
		
		getQuiz : function(courseId, topicId, quizMaterialId) {
			var promise = $http.get(GLOBAL_URL + 'quiz-review/quiz/' + courseId + '/' + topicId + '/' + quizMaterialId );
		
			promise.success(function(data, status, headers, conf){
			
				return data;
			});
			
			return promise;
		}
			
		}
		
	}]);