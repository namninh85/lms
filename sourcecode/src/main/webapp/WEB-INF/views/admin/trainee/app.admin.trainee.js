var admin = angular.module("lms.admin.trainee", ['ngRoute', 'bootstrap-tagsinput' , 
                                             	'summernote', 'ngAnimate', 'ui.bootstrap', 'ngLoadScript', 'ngTable' ,'lk-google-picker','ui.sortable','oitozero.ngSweetAlert']	);
admin.config(['$routeProvider', function($routeProvider) {

	$routeProvider.when('/list', {
	    templateUrl: 'list', 
	    controller: 'AdminController',
	    resolve: {
	    	trainees: function(TraineeService){
	    		
	    		return TraineeService.getAll();
	    	},
			view: function(){
				return 'list';
			}
	    }
	  });
	
	$routeProvider.when('/new', {
	    templateUrl: 'new', 
	    controller: 'AdminController',
	    resolve: {
	    	trainees: function(){
	    		return null;
	    	},
	    	view: function(){
				return 'new';
			}
	    }
	  });
	
	$routeProvider.when('/edit/:userId', {
	    templateUrl: 'edit', 
	    controller: 'AdminController',
	    resolve: {
	    	trainees: function(TraineeService, $route){ 
	    		return TraineeService.getByIdTrainee($route.current.params.userId);
	    	},
	    	view: function(){
				return 'edit';
			}
	    }
	});
	
	$routeProvider.when('/admin-changepwd/:userId', {
	    templateUrl: 'admin-changepwd', 
	    controller: 'AdminController',
	    resolve: {
	    	trainees: function(TraineeService, $route){ 
	    		return TraineeService.getByIdTrainee($route.current.params.userId);
	    	},
	    	view: function(){
				return 'admin-changepwd';
			}
	    }
	});
  
  $routeProvider.otherwise({
    redirectTo: '/list'
  });
}]);

