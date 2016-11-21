var admin = angular.module("lms.admin.profile", ['ngRoute', 'bootstrap-tagsinput' , 
                                             	'summernote', 'ngAnimate', 'ui.bootstrap', 'ngLoadScript', 'ngTable' ,'lk-google-picker','ngMessages','oitozero.ngSweetAlert']	);
admin.config(['$routeProvider', function($routeProvider) {

	$routeProvider.when('/edit/:userId', {
	    templateUrl: 'edit', 
	    controller: 'AdminController',
	    resolve: {
	    	profile: function(ProfileService, $route){ 
	    		return ProfileService.getById($route.current.params.userId);
	    	},
	    	view: function(){
				return '/';
			}
	    }
	});
	
	$routeProvider.when('/changepassword/:userId', {
	    templateUrl: 'changepassword', 
	    controller: 'AdminController',
	    resolve: {
	    	profile: function(ProfileService, $route){ 
	    		return ProfileService.getById($route.current.params.userId);
	    	},
	    	view: function(){
				return '/';
			}
	    }
	});
	
  $routeProvider.otherwise({
    redirectTo: '/'
  });
}]);
