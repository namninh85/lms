var report = angular.module("lms.report", ['ngRoute', 'bootstrap-tagsinput' , 
                                             	'summernote', 'ngAnimate', 'ui.bootstrap', 'ngLoadScript', 'ngTable' ,'lk-google-picker']	);
report.config(['$routeProvider' ,function($routeProvider) {

	$routeProvider.when('/course-report', {
	    templateUrl: 'course-report', 
	    controller: 'ReportController',
	    resolve: {
	    	courses: function(ReportService){
	    		
	    		return null;
	    	},
	    	courseId: function ($route) {
	    		return 0;
	    	},
	    	traineeId: function ($route) {
	    		return 0;
	    	}
	    }
	  });
	
	$routeProvider.when('/trainees/:courseId', {
	    templateUrl: 'trainee-report',  
	    controller: 'ReportController',
	    resolve: {
	    	trainees: function(ReportService){
	    		return null;
	    	},
	    	courseId: function ($route) {
	    		return $route.current.params.courseId;
	    	},
	    	traineeId: function ($route) {
	    		return 0;
	    	}
	    }
	  });
	
	$routeProvider.when('/quiz-report/:courseId/:materialId', {
	    templateUrl: 'quiz-report',  
	    controller: 'ReportController',
	    resolve: {
	    	
	    	courseId: function ($route) {
	    		return $route.current.params.courseId;
	    	},
	    	materialId: function ($route) {
	    		return $route.current.params.materialId;
	    	}
	    }
	  });
	
	$routeProvider.when('/course-detail/:courseId', {
	    templateUrl: 'course-detail',  
	    controller: 'ReportController',
	    resolve: {
	    	
	    	courseId: function ($route) {
	    		return $route.current.params.courseId;
	    	}
	    }
	  });
	
	$routeProvider.when('/trainee-detail/:courseId/:traineeId', {
	    templateUrl: 'trainee-detail', 
	    controller: 'ReportController',
	    resolve: {
	    	trainees: function(ReportService){
	    		
	    		return null;
	    	},
	    	courseId: function ($route) {
	    		return $route.current.params.courseId;
	    	},
	    	traineeId: function ($route) {
	    		return $route.current.params.traineeId;
	    	}
	    }
	  });
	
	
}]);

