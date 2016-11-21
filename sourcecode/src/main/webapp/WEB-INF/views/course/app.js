var app = angular.module("lms", ['ngRoute', 'bootstrap-tagsinput' , 
	'summernote', 'ngAnimate', 'ui.bootstrap','lms.trainee','lms.trainer', 'ngLoadScript', 
  'ngTable' ,'lk-google-picker','ui.sortable', 'darthwade.loading', 'oitozero.ngSweetAlert']);



app.config(['$routeProvider', function($routeProvider) {
	
  $routeProvider.when('/list', {
    templateUrl: 'list', 
    controller: 'CourseController as courseController',
    resolve: {
    	courses: function(CourseService){
    		return null;
    	}
    }
  });

  $routeProvider.when('/new', {
	    templateUrl: 'edit', 
	    controller: 'CourseController as courseController',
	    resolve: {
	    	courses: function(){    
	    		return null;
	    	}
	    }
	});
  
  $routeProvider.when('/edit/:courseId', {
	    templateUrl: 'edit', 
	    controller: 'CourseController as courseController',
	    resolve: {
	    	courses: function(CourseService, $route){ 
	    		return CourseService.findById($route.current.params.courseId);
	    	}
	    }
	});

  	$routeProvider.when('/:courseId/section/:sectionId/topic/add/:startDate/:endDate', {
	    templateUrl: GLOBAL_URL + 'topic/edit', 
	    controller: 'TopicController as topicController',
	    resolve: {
	    	topic: function(){   
	    		return null;
	    	}
	    }
	});

	$routeProvider.when('/:courseId/section/:sectionId/topic/edit/:topicId', {
	    templateUrl: GLOBAL_URL + 'topic/edit', 
	    controller: 'TopicController as topicController',
	    resolve: {
	    	topic: function(TopicService, $route){   
	    		return TopicService.findById($route.current.params.topicId);
	    	}
	    }
	});
	
	$routeProvider.when('/trainee1/list', {
	    templateUrl: GLOBAL_URL +'trainee/list', 
	    controller: 'CourseController as courseController',
	    resolve: {
	    	courses: function(CourseService){
	    		return CourseService.getAll();
	    	}
	    }
	  });
	
  
  $routeProvider.otherwise({
    redirectTo: '/list'
  });
}]);

app.run(['$rootScope', function($root){
	$root.getCourseIcon = function (icon) {
		if (icon == '' || icon == 'null' || icon == null) {
			return GLOBAL_URL+'resources/images/vuejs.png';
		}
		return GLOBAL_URL+ 'file/download?fileName=' + icon;
	}
	
	$root.$on('$rootChangeStart', function(e, curr, prev){
		if(curr.$$route && curr.$$route.resolve){
			//show loading message
			$root.loadingView = true;
		}
	});
	$root.$on('$rootChangeSuccess', function(e, curr, prev){
		if(curr.$$route && curr.$$route.resolve){
			//hide message
			$root.loadingView = false;
		}
	});

	  $root.summerNoteOptions = {
            height: 150,
           toolbar: [
		        ['style', ['style']],
		        ['font', ['bold', 'italic', 'underline', 'clear']],
		        // ['font', ['bold', 'italic', 'underline', 'strikethrough', 'superscript', 'subscript', 'clear']],
		        //['fontname', ['fontname']],
		        ['fontsize', ['fontsize']],
		        ['color', ['color']],
		        ['para', ['ul', 'ol', 'paragraph']],
		        ['height', ['height']],
		        ['table', ['table']],
		        ['insert', ['link', 'picture', 'hr']],
		        ['view', ['fullscreen', 'codeview']],
		        ['help', ['help']]
		      ]
     };


/***********Date configuration**************/
 var formats = ['dd/MM/yyyy','dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
 $root.formatDate = formats[0];
 $root.altInputDateFormats = ['M!/d!/yyyy'];

    $root.dateOptions = {
  //  dateDisabled: disabled,
    formatYear: 'yyyy',
    //maxDate: new Date(2020, 5, 22),
    //minDate: new Date()
    //startingDay: 1
  };

   // Disable weekend selection
function disabled(data) {
	    var date = data.date,
	      mode = data.mode;
	    return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
}


 $root.inlineDateOptions = {
    customClass: getDayClass,
    minDate: new Date(),
    showWeeks: true
 };

$root.toggleDateMin = function() {
    $root.inlineDateOptions.minDate = self.inlineDateOptions.minDate ? null : new Date();
    $root.dateOptions.minDate = self.inlineDateOptions.minDate;
};


  function getDayClass(data) {
    var date = data.date,
      mode = data.mode;
    if (mode === 'day') {
      var dayToCheck = new Date(date).setHours(0,0,0,0);

      for (var i = 0; i < self.events.length; i++) {
        var currentDay = new Date(self.events[i].date).setHours(0,0,0,0);

        if (dayToCheck === currentDay) {
          return self.events[i].status;
        }
      }
    }

    return '';
  }

   /* var tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);
  var afterTomorrow = new Date();
  afterTomorrow.setDate(tomorrow.getDate() + 1);

  $root.events = [
    {
      date: tomorrow,
      status: 'full'
    },
    {
      date: afterTomorrow,
      status: 'partially'
    }
  ];*/


	
}])

app.directive('section', function() {
  return {
    restrict: "E",
    scope: true,
    templateUrl: GLOBAL_URL+'resources/views/course/section.html',
    controller: function($rootScope, $scope, $element) {
      //$scope.contacts = $rootScope.GetContactTypes;
      $scope.delete = function(e) {
        //remove element and also destoy the scope that element
        $element.remove();
        $scope.$destroy();
      }

      
    }
  }
});


app.directive('siteHeader', function () {
    return {
        restrict: 'E',
        template: '<a href="javascript:void(0)" title="{{back}}"><i class="fa fa-arrow-right"></i></a>',
        scope: {
            back: '@back',
            forward: '@forward',
            icons: '@icons'
        },
        link: function(scope, element, attrs) {
            $(element[0]).on('click', function() {
                history.back();
                scope.$apply();
            });
            /*$(element[1]).on('click', function() {
                history.forward();
                scope.$apply();
            });*/
        }
    };
});

app.config(['lkGoogleSettingsProvider', function (lkGoogleSettingsProvider) {

  // Configure the API credentials here
  lkGoogleSettingsProvider.configure({
    apiKey   : 'AIzaSyDZEdOXpyksfU6SHMEWMqKTMY7deOBxC-k',
    clientId : '1081668895516-2br1jm4kich0h5agaajnu89p6om2hh2n.apps.googleusercontent.com',
    features : [],
  });
}])


app.filter('getExtension', function () {
  return function (url) {
    if(url !== undefined)
      return url.split('.').pop();
    return "";
  };
})
