
$(document).ready(function(){

    $('#fileuploads').fileupload({
        dataType: 'json',
        done: function (e, data) {
        	//$("tr:has(td)").remove();
            $.each(data.result, function (index, file) {
            	
                $("#uploaded-files").append(

                		$('<tr/>')
                		.append($('<td scope="row"/>').text(file.fileName.substring(0,11)+'...'))
                		.append($('<td/>').text(file.fileSize))
                		.append($('<td/>').text(file.fileType))
                		.append($('<td/>').html("<a href='lms/file/temp/get/"+file.fileName+"'>Click</a>"))
                        //.append($('<td/>').html("<a href='javascript:void(0)' onclick='deleteRow(this)'>Delete</a>"))
                		)//end $("#uploaded-files").append()
            }); 
        },
        
        progressall: function (e, data) {
	        var progress = parseInt(data.loaded / data.total * 100, 10);
	        if(progress > 0)
                $('#progress').show();
            if(progress >= 100)
                $('#progress').hide();
            
            $('#progress .progress').val(progress)
   		},
   		
		dropZone: $('#dropzone')
    });

    $('#fileupload').fileupload({
        dataType: 'json',
        done: function (e, data) {
            //$("tr:has(td)").remove();

            $.each(data.result, function (index, file) {
                $("#uploaded-files").html(
                    $('<div class="form-group"/>')
                    .append($('<div class="col-sm-3"/>').append('<img src="data:image/image/jpeg;base64,'+file.bytes+'"width="100" height="80"/>'))
                    .append($('<div class="col-sm-9"/>').append('<span ng-init="courses.icon">'+file.fileName+'</span>'))
                    .append($('<input type="hidden" id="iconHidden" name="someData" value="'+file.fileName+'"/>'))
                )//end $("#uploaded-files").append()

            }); 
        },
        
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);

           if(progress > 0)
                $('#progress').show();
            if(progress >= 100)
                $('#progress').hide();
            
            $('#progress .progress').val(progress)
        },

        add: function(e, data) {
            var uploadErrors = [];
            var acceptFileTypes = /^image\/(gif|jpe?g|png)$/i;
            if(data.originalFiles[0]['type'].length && !acceptFileTypes.test(data.originalFiles[0]['type'])) {
                uploadErrors.push('Not an accepted file type');
            }
            if(data.originalFiles[0]['size'].length && data.originalFiles[0]['size'] > 5000000) {
                uploadErrors.push('Filesize is too big');
            }
            if(uploadErrors.length > 0) {
                alert(uploadErrors.join("\n"));
            } else {
                data.submit();
            }
        },
        
        dropZone: $('#dropzone')
    });
    
    
    $('#avatar').fileupload({
        dataType: 'json',
        done: function (e, data) {
            //$("tr:has(td)").remove();

            $.each(data.result, function (index, file) {
                $("#uploaded-files").html(
                    $('<div class="form-group"/>')
                    .append('<img src="data:image/image/jpeg;base64,'+file.bytes+'"width="80" height="80"/>')
                    .append($('<input type="hidden" id="iconHidden" name="someData" value="'+file.fileName+'"/>'))
                )//end $("#uploaded-files").append()

            }); 
        },
        
        progressall: function (e, data) {
            
        },

        add: function(e, data) {
            var uploadErrors = [];
            var acceptFileTypes = /^image\/(gif|jpe?g|png)$/i;
            if(data.originalFiles[0]['type'].length && !acceptFileTypes.test(data.originalFiles[0]['type'])) {
                uploadErrors.push('Not an accepted file type');
            }
            if(data.originalFiles[0]['size'].length && data.originalFiles[0]['size'] > 5000000) {
                uploadErrors.push('Filesize is too big');
            }
            if(uploadErrors.length > 0) {
                alert(uploadErrors.join("\n"));
            } else {
                data.submit();
            }
        },
        
        dropZone: $('#dropzone')
    });
   
});

