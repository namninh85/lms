angular.module("lms")
	.controller('CourseController', ['$scope', 'courses', '$timeout',
    '$filter', 'TraineeService', '$http', '$compile', '$sce', '$templateRequest'
    ,'CourseService','$location' , '$route','NgTableParams', 'TrainerService','$loading','SweetAlert',
    function($scope, courses, $timeout, 
      $filter, TraineeService, $http, $compile, $sce, $templateRequest, CourseService,
      $location, $route, NgTableParams, TrainerService, $loading, SweetAlert){
	var self = $scope;
	
  self.courses = null;
  self.sections = [];
  self.isSave = false;
  self.isAddCourse = $location.path().indexOf('new') > 0 ? true : false;

 if($location.path().indexOf('list') > 0 ){
    //load table
    self.courses = new NgTableParams({
            page: 1, // show first page
            count: 6 // count per page
          }, {
        filterDelay: 300,
        counts :[],
        getData: function(params) {
          
        	$loading.start('courses');
          // ajax request to api
          return CourseService.searchPaging(params.url()).then(function(data) {
        	 $loading.finish('courses');
            params.total(data.data.total);
            return data.data.listResult;
          });
        }
      }); 
  }else{ //edit, add
    if(courses !== null)//==> edit
       self.courses = courses.data;
    if(self.courses !== null){ 
      self.sections = self.courses.sections;
      self.trainees = self.courses.trainees != null ? self.courses.trainees : [];
      self.trainers = self.courses.trainers != null ? self.courses.trainers : [];
    }
  }
  
  self.showModalSection  = false;
  self.alertMessage = false; 
  self.saveSuccess = false;
  self.saveFailed = false;
  self.showModalCloneCourse  = false;

  self.openSection = function(section){
      self.showModalSection = !self.showModalSection;
      self.sectionEdit = angular.copy(section);
      
      self.sectionEdit.startDate = new Date(section.startDate);
      self.sectionEdit.toDate = new Date(section.toDate);
      
      self.sectionEditIndex = self.sections.indexOf(section); 
  };
  
  self.openDuplicateCourse = function(course){
      self.showModalCloneCourse = !self.showModalCloneCourse;
      self.courseClone = angular.copy(course);
      
      self.courseClone.startDate = new Date();
      self.courseClone.endDate = this.calculateTimeEndDate(self.courseClone.startDate, course);
      console.log(self.courseClone.startDate);
      console.log(self.courseClone.endDate);
     
  };
  
  self.calculateTimeEndDate = function(startDate, course){
     var milisecondDiff = new Date(course.endDate).getTime() - new Date(course.startDate).getTime();
     var newDateMilisecond = startDate.getTime() + milisecondDiff;
     return new Date(newDateMilisecond);
  };
  	
  self.updateSection = function() {
	  angular.element(document.getElementById('myModal')).modal('hide');
	  
      self.sectionEdit.isNew = false;
      self.sections[self.sectionEditIndex] = self.sectionEdit;
      self.alertMessage=true; 
      $timeout(function () { self.alertMessage = false; }, 2000);  
      self.sectionEdit = angular.copy(self.sectionEdit);
      self.isSave = true;

      //save course
      self.saveCourse();
  }

  self.loadfile = function(path) {
	  return GLOBAL_URL + 'file/download?fileName=' + path;
  };
  self.addSection = function() {
    self.sectionEdit.isNew = false;
    self.sections[self.sectionEditIndex] = self.sectionEdit;
    self.alertMessage=true; 
    $timeout(function () { self.alertMessage = false; }, 2000);  
    self.sectionEdit = angular.copy(self.sectionEdit);   
  }

 self.cancleSection = function() {
      if(self.sectionEdit.isNew)
          self.sections.splice(self.sectionEditIndex, 1);
      self.sectionEdit = {};
      self.isSave = false;
  }
 
 self.removeSection = function(section){
   SweetAlert.swal({   
			title: "Are you sure?",   
			text: "",   
			type: "warning",   
			showCancelButton: true,   
			confirmButtonColor: "#DD6B55",   
			confirmButtonText: "Yes, delete it!",   
			cancelButtonText: "No, cancel plx!",   
			closeOnConfirm: false,   
			closeOnCancel: true 
		}, function(isConfirm){  
			if (isConfirm) {     
				var index = self.sections.indexOf(section);
				self.sections.splice(index, 1);  
				SweetAlert.swal("Deleted!", "Your section has been deleted.", "success"); 
			} else {     
				SweetAlert.swal("Cancelled", "Your section is safe :)", "error");   
			} 
		});
  }
 
 self.removeTopic = function(topic, sectionIndex){
	 SweetAlert.swal({   
			title: "Are you sure?",   
			text: "",   
			type: "warning",   
			showCancelButton: true,   
			confirmButtonColor: "#DD6B55",   
			confirmButtonText: "Yes, delete it!",   
			cancelButtonText: "No, cancel plx!",   
			closeOnConfirm: false,   
			closeOnCancel: true 
		}, function(isConfirm){  
			if (isConfirm) {     
				 var index = self.sections[sectionIndex].topics.indexOf(topic);
				 self.sections[sectionIndex].topics.splice(index, 1);
				 SweetAlert.swal("Deleted!", "Your topic has been deleted.", "success"); 
			} else {     
				SweetAlert.swal("Cancelled", "Your topic is safe :)", "error");   
			} 
		});
	
  }

  self.queryTrainees = function(query) {
      return TraineeService.getAll();
  };
  
  self.queryTrainers = function(query) {
      return TrainerService.getAll();
  };

  self.saveCourse = function(){
    if(angular.element(document.getElementById('iconHidden')).val() != undefined && angular.element(document.getElementById('iconHidden')).val() !== null)
    	self.courses.icon = angular.element(document.getElementById('iconHidden')).val();
  
    self.courses.trainees = self.trainees;
    self.courses.trainers = self.trainers;
  
    $loading.start('course');
    CourseService.update(self.courses).then(function(courseId){
    	
      self.isAddCourse = false;
      self.saveFailed=false; 

      // self.saveSuccess=true; 
      //$timeout(function () { self.saveSuccess = false; }, 2000);
      SweetAlert.swal("Save successfully!", "", "success");
      $location.path("/edit/"+courseId.data);  
      
     CourseService.findById(courseId.data).then(function(data){
    	 $loading.finish('course');
          //https://docs.angularjs.org/error/$rootScope/inprog?p0=$digest
          $timeout(function() {
           self.courses = data.data;
           self.sections = self.courses.sections === undefined ? [] : self.courses.sections;
           self.initDate();
           
         }, 0);

          // self.$apply();
      });
      
    },function(){
       $loading.finish('course');
      self.saveFailed=true; 
    });
  }

  self.addSectionFragment = function() {
    var section = {
      sectionId: null,
      title: 'New Section',
      startDate: self.courses.startDate,
      toDate: self.courses.endDate,
      isNew: true
    };

    self.sections.push(section);
  
  }    

  self.getTagClass = function(city) {
     var number = Math.ceil(Math.random() * 6);

      switch (number) {
        case 1  : return 'label label-default';
        case 2  : return 'label label-primary';
        case 3  : return 'label label-success';
        case 4  : return 'label label-info';
        case 5  : return 'label label-warning';
        case 6  : return 'label label-danger';
        default : return 'label label-default';
        //label label-danger
      }
    };

    // Init with some cities
  

  self.initDate = function() { 
     if(self.courses !== null) {
    	 if(self.courses.startDate !== undefined)
    		 self.courses.startDate = new Date(self.courses.startDate);
    	 if(self.courses.endDate !== undefined)
    		 self.courses.endDate = new Date(self.courses.endDate);
    }
  } 
  self.initDate();


  self.clear = function() {
    self.courses.startDate = null;
    self.courses.endDate= null;
  };


  self.open1 = function() {
    self.popup1.opened = true;
  };

  self.open2 = function() {
    self.popup2.opened = true;
  };

   self.open3 = function() {
    self.popup3.opened = true;
  };

   self.open4 = function() {
    self.popup4.opened = true;
  };


  self.popup1 = {
    opened: false
  };

  self.popup2 = {
    opened: false
  };

  self.popup3 = {
    opened: false
  };

  self.popup4 = {
    opened: false
  };
  
  

  self.deleteCourse = function(courseId){
	  SweetAlert.swal({   
			title: "Are you sure?",   
			text: "",   
			type: "warning",   
			showCancelButton: true,   
			confirmButtonColor: "#DD6B55",   
			confirmButtonText: "Yes, delete it!",   
			cancelButtonText: "No, cancel plx!",   
			closeOnConfirm: false,   
			closeOnCancel: true 
		}, function(isConfirm){  
			if (isConfirm) {     
				
				CourseService.detele(courseId).then(function(res){					
					SweetAlert.swal("Deleted!", "Your course has been deleted.", "success"); 
					
					$location.path("/list");  
				});
			} else {     
				SweetAlert.swal("Cancelled", "Your course is safe :)", "error");   
			} 
		});
  }
  
  self.createCourseClone = function(){
	  $loading.start('course');
	  
	    if(angular.element(document.getElementById('iconHidden')).val() != undefined && angular.element(document.getElementById('iconHidden')).val() !== null)
	    	self.courses.icon = angular.element(document.getElementById('iconHidden')).val();
	  
	    
	    CourseService.clone(self.courseClone).then(function(courseId){
	    	
	      // self.saveSuccess=true; 
	      //$timeout(function () { self.saveSuccess = false; }, 2000);
	      SweetAlert.swal("Save successfully!", "", "success");
	      $location.path("/edit/"+courseId.data);  
	      self.showModalCloneCourse = false;
	      $loading.finish('course');
          //https://docs.angularjs.org/error/$rootScope/inprog?p0=$digest

	    },function(){
	       $loading.finish('course');
	      //$timeout(function () { self.saveSuccess = false; }, 2000);
	      SweetAlert.swal("Save fail!", "", "error");
	      self.showModalCloneCourse = false;
	      $loading.finish('course');
	    });
	  }

}]);	

