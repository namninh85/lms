var admin = angular.module("lms.admin.trainer", ['ngRoute', 'bootstrap-tagsinput' , 
                                              	'summernote', 'ngAnimate', 'ui.bootstrap', 'ngLoadScript', 'ngTable' ,'lk-google-picker','ui.sortable','oitozero.ngSweetAlert']	);
admin.config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/list', {
	    templateUrl: 'list', 
	    controller: 'AdminController',
	    resolve: {
	    	trainers: function(TrainerService){
	    		return TrainerService.getAllTrainer();
	    		
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
	    	trainers: function(){
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
	    	trainers: function(TrainerService, $route){ 
	    		return TrainerService.getByIdTrainer($route.current.params.userId);
	    	},
	    	view: function(){
				return 'edit';
			}
	    }
	});
  
  $routeProvider.otherwise({
    redirectTo: '/list'
  });
}]);


