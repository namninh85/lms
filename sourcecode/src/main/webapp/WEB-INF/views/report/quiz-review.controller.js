angular.module("lms.quiz-review")
	.controller('QuizReviewController', ['$scope', 'QuizReviewService','$timeout', '$window', '$loading', '$interval','$location','$routeParams',
    function($scope, QuizReviewService, $timeout, $window, $loading, $interval,  $location,$routeParams){
		
		var self = $scope;
		self.finish = 0;
		self.rightArray = null;
		self.selection = [];
		
		var courseId = document.getElementById("courseId").value;;
		var topicId = document.getElementById("topicId").value;;
		var quizMaterialId = document.getElementById("quizMaterialId").value;;
		self.objAns = {text: ''};
		self.singleAns = '';
		var quiz = null;
		var traineeQuizDetailId = 0;
				
		$loading.start('quiz-review');
		var index = 0;
		QuizReviewService.getQuiz(courseId, topicId, quizMaterialId).then(function(data){
	          $timeout(function() {
	           self.quiz = data.data;
	           
	           if (self.quiz.quizTemplate.questionTemplates != null) {
	        	   index = 0;
	        	   self.current = self.quiz.quizTemplate.questionTemplates[index];
	           }
	           
	           $loading.finish('quiz-review');
	         }, 0);
		});
		
		
		
		self.sortableOptions = {
			    connectWith: '.connectedItemsExample .list',
			    stop: function(){
			    	//console.log(self.user.rolesNeedSave)
			    }
			  };
		
		$scope.toggleSelection1 = function toggleSelection1(ansIndex) {
		    
			var idx = self.selection.indexOf(ansIndex);
		    
		    // is currently selected
		    if (idx > -1) {
		      self.selection.splice(idx, 1);
		    }

		    // is newly selected
		    else {
		      self.selection.push(ansIndex);
		    }
		  };
		  
		$scope.toggleSelection = function toggleSelection(ansIndex) {
			var idx = self.selection.indexOf(ansIndex);
		    
		    // is currently selected
		    if (idx > -1) {
		      self.selection.splice(idx, 1);
		    }

		    // is newly selected
		    else {
		      self.selection.push(ansIndex);
		    }
			
		  };

		  $scope.toggleSingleChoice = function toggleSingleSelection(ansIndex) {
			  self.selection = [];
			  self.selection.push(ansIndex);
			
			  };
	      
			  
					  
		  
		  
		  $scope.nextQuestion = function nextQuestion() {
			
			  index = index + 1;
			  if (index >= self.quiz.quizTemplate.questionTemplates.length) {
				  index = 0;
			  }
			  
			  self.current = self.quiz.quizTemplate.questionTemplates[index];
		  };
		  
		  $scope.viewQuestion = function viewQuestion(questionIndex) {
			  
			  index = questionIndex;
			  if (index >= self.quiz.quizTemplate.questionTemplates.length) {
				  index = 0;
			  }
			  
			  self.current = self.quiz.quizTemplate.questionTemplates[index];
		  };
		
}]);	