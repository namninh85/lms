var app1 = angular.module("lms.trainee", ['ngRoute', 'ngTable', 'bootstrap-tagsinput', 'darthwade.loading']);

app1.config(function($routeProvider) {
	
  
  
  
	$routeProvider.when('/trainee/browse-courses', {
	    templateUrl: 'trainee/browse-courses', 
	    controller: 'TraineeController as traineeController',
	    resolve: {
	    	courses: function(TraineeService){	    		
	    		//return CourseService.searchCourses();
	    		return null;
	    	},
	    	
	    	
	    }
	  });
	
	$routeProvider.when('/trainee/my-courses', {
	    templateUrl: 'trainee/my-courses', 
	    controller: 'TraineeController as traineeController',
	    resolve: {
	    	mycourses: function(TraineeService){	    		
	    		//return CourseService.searchCourses();
	    		return null;
	    	},
	    	
	    	
	    }
	  });
	
	$routeProvider.when('/trainee/view-courses', {
	    templateUrl: 'trainee/browse-courses', 
	    controller: 'TraineeController as traineeController',
	    resolve: {
	    	courses: function(TraineeService){
	    		return TraineeService.searchCourses();
	    	},
	    	
	    	
	    }
	  });

  
	$routeProvider.when('/trainee/my-history', {
	    templateUrl: 'trainee/my-history', 
	    controller: 'TraineeController as traineeController',
	    resolve: {
	    	mycourses: function(TraineeService){	    		
	    		//return CourseService.searchCourses();
	    		return null;
	    	},
	    	
	    	
	    }
	  });
  
});