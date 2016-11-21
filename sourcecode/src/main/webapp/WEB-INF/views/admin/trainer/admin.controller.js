angular.module("lms.admin.trainer")
	.controller('AdminController', ['view','trainers','$scope','TrainerService','$location','$timeout','ngTableParams','$filter','SweetAlert',
    function(view,trainers,$scope,TrainerService,$location,$timeout,ngTableParams,$filter,SweetAlert){
		var self = $scope;
		if(trainers != null){
			if(view === 'list')
			{	self.trainerLst = trainers.data;
				self.usersTable = new ngTableParams({
		            page: 1,
		            count: 10
		        }, {
		            total: self.trainerLst.length, 
		            getData: function ($defer, params) {
		            	self.data = params.sorting() ? $filter('orderBy')(self.trainerLst, params.orderBy()) : self.trainerLst;
		            	self.data = params.filter() ? $filter('filter')(self.trainerLst, params.filter()) : self.trainerLst;
		            	self.data = self.data.splice((params.page() - 1) * params.count(), params.page() * params.count());
		                $defer.resolve(self.data);
		            }
		        });
			}
			else
				{
				 self.user = trainers.data;
				 TrainerService.getAllRolesByUserName(self.user.userId).then(function(res){
					 self.leftArray = res.data;
					 self.user.rolesNeedSave = 	res.data;
					
				});
				 TrainerService.getAllRolesNotBelongByUserName(self.user.userId).then(function(res){
					 self.rightArray = 	res.data;
						
				});
				
				self.sortableOptions = {
					    connectWith: '.connectedItemsExample .list',
					    stop: function(){
					    	//console.log(self.user.rolesNeedSave)
					    }
					  };
				}
		}
	
        // Save Trainee
        self.saveTrainer = function() {
        	if(angular.element(document.getElementById('iconHidden')).val() != undefined && angular.element(document.getElementById('iconHidden')).val() !== null)
            	self.user.avatar = angular.element(document.getElementById('iconHidden')).val();
        	TrainerService.saveTrainer(self.user).then(function(data){
        		SweetAlert.swal("Save successfully!", "You clicked the button!", "success")
        		$location.path("/trainer/list");
        	});
        	
        };
        
        self.cancelTrainer = function(){
        	$location.path("/trainer/list");
        }
        
        // Confirm delete
        self.showModalConfirm  = false;
        self.openModalConfim = function(id){
        	self.idDelete = id;
            self.showModalConfirm =  !self.showModalConfirm ;
        };
        
        self.loadfile = function(path) {
        	  return GLOBAL_URL + 'file/download?fileName=' + path;
          };
          
        
     // Delete new version
        self.deleteTrainer = function(id){
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
      				
      				TrainerService.deleteTrainer(id).then(function(res){					
      					SweetAlert.swal("Deleted!", "This trainer has been deleted.", "success"); 
      					var index = -1;		
  						var comArr = eval(self.data);
  						if(comArr.length == 1){
  							$location.path("/trainer/list");
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
      				});
      			} else {     
      				SweetAlert.swal("Cancelled", "This trainer is safe :)", "error");   
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