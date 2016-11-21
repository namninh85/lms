angular.module("lms")
	.controller('TopicController', ['$scope', 'topic', '$timeout',
		'TopicService', '$route','$location', '$sce', 'SpreadSheetService','$http', '$loading', 'SweetAlert',
    function($scope, topic, $timeout, TopicService, $route, $location, $sce, SpreadSheetService,$http,$loading,SweetAlert){
	var self = $scope;
	
	self.isSave = false;
	self.isAddTopic = topic === null ? true : false;
	self.alertMessage = false; 
	self.saveSuccess = false;
	self.saveFailed = false;

	self.topic = topic != null ? topic.data : {};
	self.materials = [];
	self.courseId = $route.current.params.courseId;
	self.sectionId = $route.current.params.sectionId;
	
	var section = {
		sectionId:  $route.current.params.sectionId
	}

	self.topic.sectionId = self.sectionId;
	
	if(self.isAddTopic){
		self.topic.startDate = $route.current.params.startDate;	
		self.topic.endDate = $route.current.params.endDate;
	}
	
	if(self.topic != null){
		self.materials = self.topic.materials;
	}

	self.saveTopic = function(){
	    self.topic.icon = angular.element(document.getElementById('iconHidden')).val();

	    $loading.start('topic');
	    TopicService.update(self.topic).then(function(topicId){
	      $loading.finish('topic');
	      
	      self.isAddTopic = false;
	      self.saveFailed=false; 
	      
	      SweetAlert.swal("Save successfully!", "", "success");
//	      self.saveSuccess=true; 
	//      $timeout(function () { self.saveSuccess = false; }, 2000);
	     
	      $location.path("/"+self.courseId+"/section/"+self.sectionId+"/topic/edit/"+topicId.data);
	      
	      TopicService.findById(topicId.data).then(function(data){
	        
	        //https://docs.angularjs.org/error/$rootScope/inprog?p0=$digest
	         $timeout(function() {
	      		 self.topic  = data.data;
		         self.materials = self.topic.materials === undefined ? [] : self.topic.materials;
		         self.initDate();
	         
   			 }, 0);

   			 //self.$apply();
	      });
	    
	    },function(){
	      $loading.finish('topic');
	      self.saveFailed=true; 
	    });
  	}

  self.addMaterialFragment = function() {
  
    var material = {
      materialName: 'New Material',
      isNew: true,
      open: true,
      topic : {
      	topicId : self.topic.topicId
      }
    }

    self.materials.push(material);

  } 
  
  self.getSpreadsheets = function(title) {
	    return $http.get(GLOBAL_URL + 'google/spreadsheets?title='+title)
	    .then(function(response){
	      return response.data.results.map(function(item){
	    	return item;
	      });
	    });
	
  };
  
  self.onSelect = function ($item, $model, $label, material) {
	  material.fileId = $item.id;
	  material.path = $item.title; 
	};
	
  self.trustUrl = function(url, material){
  	if(url !== undefined){
  		material.urlVideo = $sce.trustAsResourceUrl(url.replace("/watch?v=", "/embed/"));
  	}
  		
  }   

  self.parserQuestionFromUrl = function(material){

	var quizTemplateId = null;
	if(material.quizTemplate !== undefined)
		quizTemplateId = material.quizTemplate.quizTemplateId;

    $loading.start('topic');
  	SpreadSheetService.parser(material.fileId).then(function(quizTemplateBean){
  		 $loading.finish('topic');
  		 $timeout(function() {
  			material.quizTemplate = quizTemplateBean.data;
  			
  			
      		//material.questionTemplates = questionTemplates.data;
      		//var quizTemplate = {
      			//quizTemplateId : quizTemplateId,
      			//questionTemplates : questionTemplates.data,
      			//passingGrade: 70,
      			//quizTime: questionTemplates.data.length
			// }
			 //material.quizTemplate = quizTemplate;
		}, 0);
  	}, function(){
  		 $loading.finish('topic');
  		alert("File invalid format!!!");
  	});
  }

  self.removeQuiz = function(type){
  	//console.log(type)
  }

  self.initDate = function() { 
     if(self.topic !== null) {
    	 if(self.topic.startDate !== undefined)
			self.topic.startDate = new Date(self.topic.startDate);
    	 if(self.topic.endDate !== undefined)
			self.topic.endDate = new Date(self.topic.endDate);
    }
  } 
  self.initDate();


  self.clear = function() {
    self.topic.startDate = null;
    self.topic.endDate= null;
  };


  self.open1 = function() {
    self.popup1.opened = true;
  };

  self.open2 = function() {
    self.popup2.opened = true;
  };

  self.popup1 = {
    opened: false
  };

  self.popup2 = {
    opened: false
  };

  // Callback triggered after Picker is shown
  self.onLoaded = function () {
    console.log('Google Picker loaded!');
  }

  // Callback triggered after selecting files
  self.onPicked = function (material, docs) {
	  material.files  = [];
	    
    angular.forEach(docs, function (file, index) {
    	material.fileId = file.id;
		material.path = file.name; 
		material.files.push(file);
    });
    
  }
  
  self.initPicker = function(material){
	  material.files  = [];
		
	  if(material.fileId != null){// ==> handler at load page
  		 SpreadSheetService.getByFileId(material.fileId).then(function(respone){
  			material.files.push(respone.data);
  		}, function(){
  			alert("error")
  		});	
	  }else if(material.path != null){
		  var file ={
			'name': material.path,
			'url': GLOBAL_URL + "file/download?fileName="+material.path+"&folder=course&isDownload=download"
		  }
		  material.files.push(file);
	  }
  }

  // Callback triggered after clicking on cancel
  self.onCancel = function () {
    console.log('Google picker close/cancel!');
  }

  self.removeFile = function(file, material){
  	var index = material.files.indexOf(file);
  	material.files.splice(index, 1);
  	material.fileId = null;
  }
  self.removeMaterial = function(material){
	 var index = self.materials.indexOf(material);
	 self.materials.splice(index, 1);
  }

  self.sortableOptions = {
   handle: '.handle',
    // called after a node is dropped
    stop: function(e, ui) {
      //call back
    }
  };


  self.oneAtATime = true;

  self.open = function(g){
    
    if(g.open)
      g.open = false;
    else
      g.open = true;
    return g.open;
  }
}]);	