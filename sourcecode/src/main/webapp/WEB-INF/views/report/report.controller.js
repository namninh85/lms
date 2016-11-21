angular.module("lms.report")
	.controller('ReportController', ['$scope', 'ReportService', 'NgTableParams','$timeout','$location','$routeParams',
    function($scope, ReportService, NgTableParams,$timeout, $location,$routeParams){
		var self = $scope;
		
		self.courses = null;
		self.quizList = null;
		self.trainees = null;
		self.traineeCourse = null;
		self.comments = null;
		self.comments = null;
		self.courseStatus = "ALL";
		self.quizReportBean = null;
		
		// Co the sai $routeParams hoac $route
		self.courseId = $routeParams.courseId;
		self.traineeId = $routeParams.traineeId;
		self.materialId = $routeParams.materialId;
		//load table
	    self.courses = new NgTableParams({
	            page: 1, // show first page
	            count: 10 // count per page
	          }, {
	        filterDelay: 300,
	        counts :[],
	        getData: function(params) {
	          // ajax request to api
	        	
	          return ReportService.searchPaging(params.url(), self.courseStatus).then(function(data) {
	        	  
	            params.total(data.data.total);
	            return data.data.listResult;
	          });
	        }
	      });
	    
	    self.trainees = new NgTableParams({
            page: 1, // show first page
            count: 10, // count per page
            courseId: self.courseId
          }, {
        filterDelay: 300,
        counts :[],
        getData: function(params) {
          // ajax request to api
          return ReportService.searchTrainees(params.url()).then(function(data) {
        	  
            params.total(data.data.total);
            return data.data.listResult;
          });
        }
      });
	    if($location.path().indexOf('trainee-detail') > 0 ){
	    	
		    ReportService.traineeDetailData(self.courseId, self.traineeId).then(function(data) {
		    	
		    	self.traineeCourse = data.data; 
	            
	          });
		    
		    ReportService.getComments(self.courseId, self.traineeId).then(function(data) {
		    	
		    	self.comments = data.data; 
	            
	          });
        
	    }
	    
	    if($location.path().indexOf('course-detail') > 0 ){
	    	
	    	self.quizList = new NgTableParams({
	            page: 1, // show first page
	            count: 100, // count per page
	            courseId: self.courseId
	          }, {
	        filterDelay: 300,
	        counts :[],
	        getData: function(params) {
	          // ajax request to api
	          return ReportService.getAllQuiz(params.url()).then(function(data) {
	        	  
	            params.total(data.data.length);
	            return data.data;
	          });
	        }
	      });
		            
	    }
	    
	    if($location.path().indexOf('quiz-report') > 0 ){
	    	if (self.courseId != undefined && self.materialId != undefined) {
	    	ReportService.getQuizSummary(self.courseId, self.materialId).then(function(data) {
		    	
		    	self.quizReportBean = data.data; 
	            
	          });
	    	}
		            
	    }

	 // Export excel
        self.exportToExcel = function(tableId){ // ex: '#my-table'
        	
            var exportHref = ReportService.tableToExcel(tableId,'sheet name');
            $timeout(function(){location.href = exportHref;},100); // trigger download
        }
        
        self.saveAssessment = function(){
        	
        		ReportService.saveAssessment(self.traineeCourse).then(function(data) {
		    	alert('Saved Successful !')
		    	 
	            
	          });
        }  ;
        
        
        self.doSearchCourse = function() {
        	self.courses = new NgTableParams({
	            page: 1, // show first page
	            count: 10 // count per page
	          }, {
	        filterDelay: 300,
	        counts :[],
	        getData: function(params) {
	          // ajax request to api
	          
	          return ReportService.searchPaging(params.url(), self.courseStatus).then(function(data) {
	        	  
	            params.total(data.data.total);
	            return data.data.listResult;
	          });
	        }
	      });
        }
        self.addComment = function(){
        	self.comment.courseId=self.courseId;
        	self.comment.userId = self.traineeId;
    		ReportService.addComment(self.comment).then(function(data) {
    			ReportService.getComments(self.courseId, self.traineeId).then(function(data) {
    		    	
    		    	self.comments = data.data; 
    	            
    	          });	 
            
          });
    		
    		
    }  
		
}]);	