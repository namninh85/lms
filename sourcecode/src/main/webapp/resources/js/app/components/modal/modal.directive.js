var module = angular.module('lms')
	.directive('modal',  function(){
	// Runs during compile
	return {
		// name: '',
		// priority: 1,
		// terminal: true,
		 scope: true, // {} = isolate, true = child, false/undefined = no change
		// controller: function($scope, $element, $attrs, $transclude) {},
		// require: 'ngModel', // Array = multiple requires, ? = optional, ^ = check parent elements
		 restrict: 'E', // E = Element, A = Attribute, C = Class, M = Comment
		// template: '',
		 templateUrl: GLOBAL_URL+ 'resources/js/app/components/modal/modal.html',
		 replace: true,
		 transclude: true,
		// compile: function(tElement, tAttrs, function transclude(function(scope, cloneLinkingFn){ return function linking(scope, elm, attrs){}})),
		link:  function postLink(scope, element, attrs) {
		
			scope.title = attrs.title;

	        scope.$watch(attrs.visible, function(value){
	          if(value == true)
	            $(element).modal('show');
	          else
	            $(element).modal('hide');
	        });

	        $(element).on('shown.bs.modal', function(){
	          scope.$apply(function(){
	            scope.$parent[attrs.visible] = true;
	          });
	        });

	        $(element).on('hidden.bs.modal', function(){
	          scope.$apply(function(){
	            scope.$parent[attrs.visible] = false;
	          });
	        });
		}
	};
});

var module1 = angular.module('lms.admin.trainee')
.directive('modal',  function(){
// Runs during compile
return {
	// name: '',
	// priority: 1,
	// terminal: true,
	 scope: true, // {} = isolate, true = child, false/undefined = no change
	// controller: function($scope, $element, $attrs, $transclude) {},
	// require: 'ngModel', // Array = multiple requires, ? = optional, ^ = check parent elements
	 restrict: 'E', // E = Element, A = Attribute, C = Class, M = Comment
	// template: '',
	 templateUrl: GLOBAL_URL+ 'resources/js/app/components/modal/modal.html',
	 replace: true,
	 transclude: true,
	// compile: function(tElement, tAttrs, function transclude(function(scope, cloneLinkingFn){ return function linking(scope, elm, attrs){}})),
	link:  function postLink(scope, element, attrs) {
	
		scope.title = attrs.title;

        scope.$watch(attrs.visible, function(value){
          if(value == true)
            $(element).modal('show');
          else
            $(element).modal('hide');
        });

        $(element).on('shown.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = true;
          });
        });

        $(element).on('hidden.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = false;
          });
        });
	}
};
});

var module2 = angular.module('lms.admin.trainer')
.directive('modal',  function(){
// Runs during compile
return {
	// name: '',
	// priority: 1,
	// terminal: true,
	 scope: true, // {} = isolate, true = child, false/undefined = no change
	// controller: function($scope, $element, $attrs, $transclude) {},
	// require: 'ngModel', // Array = multiple requires, ? = optional, ^ = check parent elements
	 restrict: 'E', // E = Element, A = Attribute, C = Class, M = Comment
	// template: '',
	 templateUrl: GLOBAL_URL+'resources/js/app/components/modal/modal.html',
	 replace: true,
	 transclude: true,
	// compile: function(tElement, tAttrs, function transclude(function(scope, cloneLinkingFn){ return function linking(scope, elm, attrs){}})),
	link:  function postLink(scope, element, attrs) {
	
		scope.title = attrs.title;

        scope.$watch(attrs.visible, function(value){
          if(value == true)
            $(element).modal('show');
          else
            $(element).modal('hide');
        });

        $(element).on('shown.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = true;
          });
        });

        $(element).on('hidden.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = false;
          });
        });
	}
};
});
