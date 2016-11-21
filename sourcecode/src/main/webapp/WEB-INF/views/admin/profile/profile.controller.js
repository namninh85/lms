angular.module("lms.admin.profile")
	.controller('AdminController', ['profile','$scope','ProfileService','$location','$window','SweetAlert',
    function(profile,$scope,ProfileService,$location,$window,SweetAlert){
		var self = $scope;
		
		if(profile != null){
			self.user = profile.data;
		}
		
		
	
        // Update profile
        self.update = function() {
        	if(angular.element(document.getElementById('iconHidden')).val() != undefined && angular.element(document.getElementById('iconHidden')).val() !== null)
            	self.user.avatar = angular.element(document.getElementById('iconHidden')).val();
        	ProfileService.update(self.user).then(function(data){
        		SweetAlert.swal("Update profile successfully!", "You clicked the button!", "success")
        	});
        	
        };
        
     // Change pass
        self.changePassword = function() {
        	SweetAlert.swal(
	        		{   
	        			title: "Change password",   
	        			text: "Submit to change password",   
	        			type: "info",   
	        			showCancelButton: true,   
	        			closeOnConfirm: false,   
	        			showLoaderOnConfirm: true, 
	        			}, 
	        			function(isConfirm){
	        				if (isConfirm) {
	        					 ProfileService.changePassword(self.user).then(function(res) {
	        						 console.log(res.data.error);
	        						if(res.data.error == "error"){
	        							SweetAlert.swal("Cancelled", "Not change password!", "error");
	        						}else{
	        							SweetAlert.swal("Changed password", "You clicked the button!", "success");
	        						}
	        					 });
	        				} else {     
	        					SweetAlert.swal("Cancelled", "Not change password :)", "error");   }
	        });
        }
        
            self.forgotPassword = function() {
       	 	alert("Changed password. Send to email")
       };
       
        self.cancel = function(){
        	$window.location = GLOBAL_URL + '/index';
        }
        // Confirm delete
        
        self.dob = new Date();
		self.open1 = function() {
		    self.popup1.opened = true;
		};
		self.popup1 = {
			opened: false
		};  
		
	}]);