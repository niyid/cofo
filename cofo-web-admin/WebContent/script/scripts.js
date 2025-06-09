if (!VasWorks)
	var VasWorks = {};
VasWorks.FileUploader = Class.create();

VasWorks.FileUploader.prototype = {
	// number of pending files
	pending: 0,
	uploadedCount: 0,
	files: $A([]),
	postUrls: $A([]),
	
	initialize: function(statusElement, hostUrl) {
		if (statusElement.bar == null) {
			var bar = VasWorks.FileUploader.createBar(statusElement, this.cancelDownload.bind(this));
			statusElement.bar = $(bar);
		}
		this.progressBar=statusElement.bar.firstChild;
		this.statusBar=statusElement.bar.childNodes[2];

		this.log = function(logline) {
			var line=document.createElement('DIV');
			line.innerHTML=logline + "<br />";
			statusElement.bar.childNodes[1].appendChild(line);
		}

		var self=this;
		this.workerPool = google.gears.factory.create('beta.workerpool');
		this.workerPool.onmessage = function(a, b, message) {
			if (message.body.uploadComplete) {
				self.uploadComplete(message.body);
				if (!self.uploadNext())
					self.finishedTimeout=setTimeout(function() { window.location.href=window.location.href; }, 2000);
			} else if (message.body.errorMessage) {
				alert("Error in upload.js line " + message.body.errorMessage.lineNumber + ": " + message.body.errorMessage.message + "\nGears " + google.gears.factory.version);
			} else if (message.body.progress) {
				self.updateProgress(100 * message.body.progress.loaded / message.body.progress.total);
			} else if (message.body.startingUpload) {
				self.updateProgress(0, message.body.fileName);
			} else if (message.body.aborted) {
				self.log("Upload aborted.");
				self.uploadAborted();
			} else {
				self.log('Worker ' + message.sender + ': \n' + message.body);
			}
		};
		this.childWorkerId = this.workerPool.createWorkerFromUrl(hostUrl + '/script/gears/upload.js');
	},
	upload: function(files, postUrl) {
		if (self.finishedTimeout) clearTimeout(self.finishedTimeout);
		//this.log("Uploading " + files.length + " files");
		//this.log("Uploading to " + postUrl);
		if (!postUrl.startsWith('http://'))
			postUrl = window.location.protocol + "//" + window.location.host
					+ postUrl;
		for (var i=0; i<files.length; i++) {
			this.pending++;
			this.files[this.files.length]=files[i];
			this.postUrls[this.postUrls.length]=postUrl;
		}
		if (this.pending==files.length) this.uploadNext();
	},
	uploadNext: function() {
		if (this.files.length==0) {
			this.progressBar.innerHTML="Done. Will refresh in 2s.";
			return false;
		} else {
			var nextFile=this.files.splice(0, 1)[0];
			var nextFileUrl=this.postUrls.splice(0, 1)[0];
			this.workerPool.sendMessage( { file: nextFile, postUrl: nextFileUrl }, this.childWorkerId);
			this.log("Queue size: " + this.files.length);
			return true;
		}
	},
	uploadComplete: function() {
		this.pending--;
		this.uploadedCount++;
		this.progressBar.innerHTML="Done.";
		this.progressBar.addClassName("upload-complete");
		this.refreshStatus();
	},
	uploadAborted: function() {
		this.progressBar.removeClassName("upload-complete");
		this.progressBar.addClassName("upload-aborted");
		this.refreshStatus();
	},
	updateProgress: function(percentage, filename) {
		this.progressBar.removeClassName("upload-complete");
		this.progressBar.removeClassName("upload-aborted");
		var style = { width : "" + percentage + "%"	};
		this.progressBar.setStyle(style);
		if (filename) this.progressBar.innerHTML=filename;
		this.refreshStatus();
	},
	cancelDownload: function() {
		this.log("Aborting!");
		this.workerPool.sendMessage( { abort: true }, this.childWorkerId);
		this.pending = 0;
		this.uploadedCount = 0;
		this.files = $A([]);
		this.postUrls = $A([]);
	},
	refreshStatus: function() {
		if (this.files.length==0)
			this.statusBar.innerHTML=null;
		else {
			this.statusBar.innerHTML="";
//			this.statusBar.innerHTML="Queue: " + this.files.length + "<br />";
			for (var i=0; i<this.files.length; i++)
				this.statusBar.innerHTML+=(i>0 ? ', ' : '') + this.files[i].name;
		}
	}
};

VasWorks.FileUploader.upload = function(statusElement, files, postUrl, hostUrl) {
	if (files == null || files.length == 0)
		return true;
	if (!statusElement.gearsUploader) statusElement.gearsUploader = new VasWorks.FileUploader(statusElement, hostUrl);
	statusElement.gearsUploader.upload(files, postUrl);
};
VasWorks.FileUploader.createBar = function(statusElement, cancelDownloadFunction) {
	var div = $(document.createElement('DIV'));
	div.addClassName("upload-bar");
	var e = $(document.createElement('DIV'));
	e.addClassName("upload-progress");
	e.setStyle("width: 0px;");
	div.appendChild(e);
	e = $(document.createElement('DIV'));
	e.addClassName("upload-log");
	e.innerHTML = "x";
	div.appendChild(e);
	e = $(document.createElement('DIV'));
	e.addClassName("upload-queue-status");
	e.innerHTML = "";
	div.appendChild(e);
	e = $(document.createElement('DIV'));
	e.addClassName("upload-cancel");
	e.innerHTML="<input type='button' value='Cancel upload' />";
	div.appendChild(e);
	
	statusElement.appendChild(div);	
	// register event handlers	 
	Event.observe(e, 'click', cancelDownloadFunction);
	return div;
};
VasWorks.FileUploader.uploadFiles = function(statusElement, postUrl, rootUrl) {
	if (statusElement==null) throw new Error("Status element is null!");
	return function uploadFiles(event) {
		if (!VasWorks.hasGears()) { Event.stop(event); return false; }
		var desktop = google.gears.factory.create('beta.desktop');
		desktop.openFiles(function (files) {
			for (var i=0; i<files.length; i++)
				files[i].metaData=desktop.extractMetaData(files[i].blob);
	  		VasWorks.FileUploader.upload(statusElement, files, postUrl, rootUrl);
		}, { singleFile: false, filter: [] });
		return false;
	}
};
VasWorks.FileUploader.registerDocumentDrop = function(target, invitationElement, statusElement, postUrl, rootUrl) {
	if (invitationElement==null) throw new Error("Invitation element is null!");
	if (statusElement==null) throw new Error("Status element is null!");
	if (!target.vasworksFileDrop) {
		target.vasworksFileDrop=true;
		if (window.google && google.gears && !Prototype.Browser.IE) { 
			var desktop=google.gears.factory.create('beta.desktop');
			Event.observe(target, 'dragenter', function(event) { desktop.setDropEffect(event, 'copy'); })
			Event.observe(target, 'dragover', function(event) { desktop.setDropEffect(event, 'copy'); })
			Event.observe(target, 'dragdrop', function(event) { VasWorks.FileUploader.uploadFilesByDrag(event, statusElement, postUrl, rootUrl); });
		} else {
			// No drag and drop
			Event.observe(target, 'dragdrop', function(event) { window.alert("You need to have Gears http://gears.google.com installed for drag-and-drop to work!"); Event.stop(event); });
			// change invitation element!
			if (Prototype.Browser.IE) {
				invitationElement.innerHTML="You need to have <a target='_blank' href='http://gears.google.com'>Gears</a> installed for drag-and-drop file uploads to work! Does <b>NOT</b> seem to work with IE. Use <a href='http://getfirefox.com'>Firefox</a>.";
			} else {
				invitationElement.innerHTML="You need to have <a target='_blank' href='http://gears.google.com'>Gears</a> installed for drag-and-drop file uploads to work!";
			}
		}
		Element.setStyle(invitationElement, { display: 'block' });
	} else {
		Element.hide(invitationElement);
	}
};
VasWorks.FileUploader.uploadFilesByDrag = function(event, statusElement, postUrl, rootUrl) {
	if (!VasWorks.hasGears()) { alert('no gears'); Event.stop(event); return false; }
	var desktop = google.gears.factory.create('beta.desktop');
	var data = desktop.getDragData(event, 'application/x-gears-files');
	var files = data && data.files;
	if (files) {
		for (var i=0; i<files.length; i++)
			files[i].metaData=desktop.extractMetaData(files[i].blob);

		VasWorks.FileUploader.upload(statusElement, files, postUrl, rootUrl);
	} else {
		alert('No files available in drag information.'); 
	}
	Event.stop(event);
	return false;
};
VasWorks.hasGears = function() {
	if (!window.google || !google.gears) { 
		if (window.confirm("This function requires Gears plug-in.\nWould you like to install Gears?")) 
			window.location.href='http://gears.google.com'; 
		return false; 
	} else
		return true;
}

Event.observe(window, 'load', function() {
	// gears-installed
	var matching = document.getElementsByClassName("gears-only");
	if (window.google && google.gears) {
		if (matching!=null && matching.length>0) 
			for (var i=0; i<matching.length; i++) {
				Element.setStyle($(matching[i]), { display: 'block' });
			}
	} else {
		if (matching!=null && matching.length>0) 
			for (var i=0; i<matching.length; i++) {
				Element.hide($(matching[i]));
			}
	}
	// no gears
	var matching = document.getElementsByClassName("gears-missing");
	if (window.google && google.gears) {
		if (matching!=null && matching.length>0) 
			for (var i=0; i<matching.length; i++) {
				Element.hide($(matching[i]));
			}
	} else {
		if (matching!=null && matching.length>0) 
			for (var i=0; i<matching.length; i++) {
				Element.setStyle($(matching[i]), { display: 'block' });
			}
	}

	// find drop zones
	var matching = document.getElementsByClassName("file-drop-zone");
	if (matching!=null && matching.length>0) 
		for (var i=0; i<matching.length; i++) {
			VasWorks.FileUploader.registerDocumentDrop($(matching[i]), $(matching[i]), $(matching[i].getAttribute("vasworks:upload-log")), matching[i].getAttribute("vasworks:destination"), matching[i].getAttribute("vasworks:root"));
		}
	// find gears browse buttons
	matching = document.getElementsByClassName("file-gears-browse");
	if (matching!=null && matching.length>0) 
		for (var i=0; i<matching.length; i++) {
			if (!window.google || !google.gears || Prototype.Browser.IE) { 
				Element.hide($(matching[i]));
			} else {
				Event.observe($(matching[i]), "click", VasWorks.FileUploader.uploadFiles(
							$($(matching[i]).getAttribute("vasworks:upload-log")), 
							$(matching[i]).getAttribute("vasworks:destination"), 
							$(matching[i]).getAttribute("vasworks:root")));
				Element.setStyle($(matching[i]), { display: 'inline' });
			}
		}
});

VasWorks.Collapse = Class.create();

VasWorks.Collapse.collapse = function(element) {
	var a=$(element).ancestors();
	for (var i=0; i<a.length; i++) {
		if (Element.hasClassName(a[i], "collapse")) {
			Element.addClassName(a[i], "collapsed"); Event.stop(); return true;
		}
	}
}

VasWorks.Collapse.show = function(element) {
	var a=$(element).ancestors();
	for (var i=0; i<a.length; i++) {
		if (Element.hasClassName(a[i], "collapse")) {
			Element.removeClassName(a[i], "collapsed"); Event.stop(); return true;
		}
	}
}
