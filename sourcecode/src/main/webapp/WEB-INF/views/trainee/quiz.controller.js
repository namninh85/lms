var app2 = angular.module("lms.quiz", ['ui.sortable', 'ui.bootstrap', 'bootstrap-tagsinput', 'darthwade.loading']);

	  
angular.module("lms.quiz")
	.controller('QuizController', ['$scope', 'QuizService','$timeout', '$window', '$loading', '$interval',
    function($scope, QuizService, $timeout, $window, $loading, $interval){
		
		var self = $scope;
		self.finish = 0;
		self.rightArray = null;
		self.selection = [];
		var courseId = document.getElementById("courseId").value;;
		var topicId = document.getElementById("topicId").value;;
		var quizMaterialId = document.getElementById("quizMaterialId").value;;
		var traineeQuizId = document.getElementById("traineeQuizId").value;;
		self.objAns = {text: ''};
		self.singleAns = '';
		var quiz = {total:0};
		var traineeQuizDetailId = 0;
		$interval(updateTime, 1000);
		function updateTime() {
			if (self.quiz != null && self.quiz.timeLeft != null) {
			self.quiz.timeLeft = self.quiz.timeLeft - 1;
			if (self.quiz.timeLeft <= 0) {
				self.quizFinish();
			}
		}
		}
		
		function hightLightQuiz(score) {
			alert(score);
			return "LowScore";
		}
		
		$loading.start('quiz');
		getNextQuestion();
		
		
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
	      
			  
					  
		  $scope.quizAns = function quizAns() {
			  
			 // self.textAns = document.getElementById("textAns").value;
			  
			  if (self.objAns.text != '' ) {
				  self.selection.push(self.objAns.text);
			  }
			  
			  if (self.quiz.current.question.questionType=='Ordering') {
				  if (self.quiz.current.ansList.length > 0) {
					  self.selection = self.quiz.current.ansList[0].index;
				  }
				  if (self.quiz.current.ansList.length > 1)
				  for (var i = 1; i <self.quiz.current.ansList.length; i++) {
					  self.selection = $scope.selection + ',' + self.quiz.current.ansList[i].index;
				  }
				  
			  };
			  
			  if (self.selection=='' && self.quiz.current.question.required=='1') {
				  alert('Please answer the question !');
				  $loading.finish('quiz');
				  return 0;
			  }
			  $loading.start('quiz');
			  QuizService.ansQuestion(self.selection, self.traineeQuizDetailId, courseId, topicId, quizMaterialId, traineeQuizId).then(function(data){
				  
		          https://docs.angularjs.org/error/$rootScope/inprog?p0=$digest
		          $timeout(function() {
		           //self.selection=[];
		           //self.objAns.text = '';
		           
		           self.quiz.ansResult = data.data.ansResult;
		           
		           if (self.quiz.ansResult != null && self.quiz.ansResult.result == '1') {		        	   
		        	   self.quiz.ansResult = null;
		        	   getNextQuestion();
		           } else {
		        	   if (self.quiz.current.question.required=='0') {
		        		   self.quiz.ansResult = null;
			        	   getNextQuestion();
		        	   }
		           }
		           $loading.finish('quiz');
		           /*
		           self.quiz = data.data;
		           if (self.quiz.current != null) {
		        	   self.rightArray = self.quiz.current.ansList;
		        	   
		        	   self.traineeQuizDetailId = self.quiz.current.question.traineeQuizDetailId;
		           }
		           
		           angular.element(document.getElementsByClassName('countdown')).attr("data-value",self.quiz.timeLeft);
		           $('.countdown').tkCountdown();
		           */
		         }, 0);
			});

			  
		  };
		  
		  $scope.skipQuestion = function skipQuestion() {
			  
			  if (self.quiz.current.question.required == null || self.quiz.current.question.required=='1') {
					alert ('You can not skip this question !');
					
				} else {
				$loading.start('quiz');
				  QuizService.skipQuestion(self.traineeQuizDetailId, courseId, topicId, quizMaterialId, traineeQuizId).then(function(data){
					  
			          https://docs.angularjs.org/error/$rootScope/inprog?p0=$digest
			          $timeout(function() {
			        	  getNextQuestion();
			        	  $loading.finish('quiz');
			         }, 0);
				});

				}  
		  };
		  
		  $scope.quizFinish = function quizFinish() {
			  if (self.finish == 0) {
			  $loading.start('quiz');
			  QuizService.ansFinish($scope.selection, $scope.traineeQuizDetailId, courseId, topicId, quizMaterialId, traineeQuizId).then(function(data){
				  
		          https://docs.angularjs.org/error/$rootScope/inprog?p0=$digest
		          $timeout(function() {
		           self.quiz = null;// data.data;      
		           self.finish = 1;
		           $window.location.href = GLOBAL_URL + 'trainee/view-topic/' + courseId + '/' + topicId;
		           //$loading.finish('quiz');
		         }, 0);
			});

			  };
			  
		  };
		  
		  $scope.nextQuestion = function nextQuestion() {
			  
			  getNextQuestion();
			  
			  
		  };
		  
		  function getNextQuestion() {
			  $loading.start('quiz');
			  self.selection=[];
	          self.objAns.text = '';
			  QuizService.getNextQuestion(courseId, topicId, quizMaterialId, traineeQuizId).then(function(data){
					console.log(data.data)
		          //https://docs.angularjs.org/error/$rootScope/inprog?p0=$digest
		          $timeout(function() {
		        	 // $loading.finish('quiz');
		           //$scope.toggleSelection=[];
		           //self.textAns = '';
		           self.quiz = data.data;
		           if (self.quiz.current != null) {
		        	   self.rightArray = self.quiz.current.ansList;
		        	   self.traineeQuizDetailId = self.quiz.current.question.traineeQuizDetailId;
		           }
		           angular.element(document.getElementsByClassName('countdown')).attr("data-value",self.quiz.timeLeft);
		           
		           $('.countdown').tkCountdown();
		           $loading.finish('quiz');
		         }, 0);
			});

		  }
		  
		  var uri='data:application/vnd.ms-excel;base64,',
	        template='<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>',
	        base64=function(s){return $window.btoa(unescape(encodeURIComponent(s)));},
	        format=function(s,c){return s.replace(/{(\w+)}/g,function(m,p){return c[p];})};
		  
		// Export excel
	        self.exportToExcel = function(tableId){ // ex: '#my-table'
	        	
	            var exportHref = QuizService.tableToExcel(tableId,'sheet name');
	            $timeout(function(){location.href = exportHref;},100); // trigger download
	        }

}]);	