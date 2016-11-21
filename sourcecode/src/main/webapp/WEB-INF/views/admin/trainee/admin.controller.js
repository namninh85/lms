angular.module("lms.admin.trainee")
	.controller('AdminController', ['view','trainees','$scope','TraineeService','$location','$timeout','ngTableParams','$filter','SweetAlert',
    function(view,trainees,$scope,TraineeService,$location,$timeout,ngTableParams,$filter,SweetAlert){
		var self = $scope;
		self.showModalConfirm  = false;
		if(trainees != null){
			if(view === 'list')
				{
					self.traineeLst = trainees.data;
					self.usersTable = new ngTableParams({
			            page: 1,
			            count: 10
			        }, {
			            total: self.traineeLst.length, 
			            getData: function ($defer, params) {
			            	self.data = params.sorting() ? $filter('orderBy')(self.traineeLst, params.orderBy()) : self.traineeLst;
			            	self.data = params.filter() ? $filter('filter')(self.traineeLst, params.filter()) : self.traineeLst;
			            	self.data = self.data.splice((params.page() - 1) * params.count(), params.page() * params.count());
			                $defer.resolve(self.data);
			            },
			            dataset: trainees
			        });
				}
			else{
				self.user = trainees.data;
				 TraineeService.getAllRolesByUserName(self.user.userId).then(function(res){
					 self.leftArray = res.data;
					 self.user.rolesNeedSave = 	res.data;
					
				});
				 TraineeService.getAllRolesNotBelongByUserName(self.user.userId).then(function(res){
					 self.rightArray = 	res.data;
						
				});
				
				self.sortableOptions = {
					    connectWith: '.connectedItemsExample .list',
					    stop: function(){
					    	console.log(self.user.rolesNeedSave)
					    }
					  };
				
			}
				
		}
		
		// search
		self.searchUser = function(role){
			if(role == 'TRAINEE'){
				TraineeService.getAllTraniee().then(function(res){
					self.traineeLst = res.data;
					self.usersTable = new ngTableParams({
			            page: 1,
			            count: 10
			        }, {
			            total: self.traineeLst.length, 
			            getData: function ($defer, params) {
			            	self.data = params.sorting() ? $filter('orderBy')(self.traineeLst, params.orderBy()) : self.traineeLst;
			            	self.data = params.filter() ? $filter('filter')(self.traineeLst, params.filter()) : self.traineeLst;
			            	self.data = self.data.splice((params.page() - 1) * params.count(), params.page() * params.count());
			                $defer.resolve(self.data);
			            },
			            dataset: trainees
			        });
				});
			} else if(role == 'TRAINER'){
				TraineeService.getAllTranier().then(function(res){
					self.traineeLst = res.data;
					self.usersTable = new ngTableParams({
			            page: 1,
			            count: 10
			        }, {
			            total: self.traineeLst.length, 
			            getData: function ($defer, params) {
			            	self.data = params.sorting() ? $filter('orderBy')(self.traineeLst, params.orderBy()) : self.traineeLst;
			            	self.data = params.filter() ? $filter('filter')(self.traineeLst, params.filter()) : self.traineeLst;
			            	self.data = self.data.splice((params.page() - 1) * params.count(), params.page() * params.count());
			                $defer.resolve(self.data);
			            },
			            dataset: trainees
			        });
				});
			} else {
				TraineeService.getAll().then(function(res){
					self.traineeLst = res.data;
					self.usersTable = new ngTableParams({
			            page: 1,
			            count: 10
			        }, {
			            total: self.traineeLst.length, 
			            getData: function ($defer, params) {
			            	self.data = params.sorting() ? $filter('orderBy')(self.traineeLst, params.orderBy()) : self.traineeLst;
			            	self.data = params.filter() ? $filter('filter')(self.traineeLst, params.filter()) : self.traineeLst;
			            	self.data = self.data.splice((params.page() - 1) * params.count(), params.page() * params.count());
			                $defer.resolve(self.data);
			            },
			            dataset: trainees
			        });
				});
			}
			
		}
		
        // Save Trainee
        self.saveTrainee = function() {
            if(angular.element(document.getElementById('iconHidden')).val() != undefined && angular.element(document.getElementById('iconHidden')).val() !== null)
            	self.user.avatar = angular.element(document.getElementById('iconHidden')).val();
            
        	TraineeService.saveTrainee(self.user).then(function(data){
        		
        		SweetAlert.swal("Save successfully!", "You clicked the button!", "success")
        		$location.path("/trainee/list");
        	});
        	
        };
        
        self.loadfile = function(path) {
      	  return GLOBAL_URL + 'file/download?fileName=' + path;
        };
     // Save Trainee
        self.changePassword = function() {
        	TraineeService.changePassword(self.user).then(function(data){
        		SweetAlert.swal("Save successfully!", "You clicked the button!", "success")
        		$location.path("/trainee/list");
        	});
        	
        };
        
        
        self.cancelTrainee = function(){
        	$location.path("/trainee/list");
        }
        // Export excel
        self.exportToExcel = function(tableId){ // ex: '#my-table'
        	
            var exportHref = TraineeService.tableToExcel(tableId,'sheet name');
            $timeout(function(){location.href = exportHref;},100); // trigger download
        }
        // Confirm delete
        
     /*   self.openModalConfim = function(id){
        	self.idDelete = id;
            self.showModalConfirm =  !self.showModalConfirm ;
        };
        */
     /*   self.deleteTrainee = function(id){
          	self.showModalConfirm = false;
        	$timeout(function () {	TraineeService.deleteTrainee(id).then(function(){
        		$location.path("/trainee/list");
        	});
        	 }, 10);
        	
        };*/
        
        // Delete new version
        self.deleteTrainee = function(id){
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
      				
      				TraineeService.deleteTrainee(id).then(function(res){
      					SweetAlert.swal("Deleted!", "This trainee has been deleted.", "success");
      					var index = -1;		
  						var comArr = eval(self.data);
  						if(comArr.length == 1){
  							$location.path("/trainee/list");
  						}
  						for( var i = 0; i < comArr.length; i++ ) {
  							if( comArr[i].userId === id ) {
  								index = i;
  								break;
  							}
  						}
  						if( index === -1 ) {
  							alert( "Something gone wrong" );
  						}
  						self.data.splice( index, 1);	
      					//$location.path("/trainee/list");
      				});
      			} else {     
      				SweetAlert.swal("Cancelled", "This trainee is safe.", "error");   
      			} 
      		});
        };
        
        self.dob = new Date();
		self.open1 = function() {
		    self.popup1.opened = true;
		};
		self.popup1 = {
			opened: false
		};  
		
		
		
		
		
		
	}]);
	