var req;
var isIE;
var searchId;
var completeTable;
var autoRow;

function init() {
    searchId = document.getElementById("searchId");
    completeTable = document.getElementById("complete-table");
    autoRow = document.getElementById("auto-row");
	var x = document.getElementById("latitude1");
	var y = document.getElementById("longitude1");
	getLocation();
}

function getLocation() {
  if (navigator.geolocation) {
	 
    navigator.geolocation.getCurrentPosition(showPosition);
  } else { 
    x.innerHTML = "Geolocation is not supported by this browser.";
  }
}

function showPosition(position) {
  //x.innerHTML =   ;
//window.alert(position.coords.latitude);
  // y.innerHTML =  position.coords.longitude;
  var lat=position.coords.latitude;
  var longi=position.coords.longitude;
	$.ajax({
			url: "Home?latitude='"+lat+"'&longitude='"+longi+"'",
			type: "GET",
			data: "{}",
			success: function (msg) {
			},
			error: function(){
				console.log("error occurred while making ajax call;")
				window.alert("error occurred while making ajax call");
			}
		});
	document.getElementById("latitude").value = lat;
	document.getElementById("longitude").value = longi;

}

function doCompletion() {
    var url = "autocomplete?action=complete&searchId=" + escape(searchId.value);
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = callback;
    req.send(null);
}

function initRequest() {
    if (window.XMLHttpRequest) {
        if (navigator.userAgent.indexOf('MSIE') != -1) {
            isIE = true;
        }
        return new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        isIE = true;
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
}

function callback() {
    clearTable();

    if (req.readyState == 4) {
        if (req.status == 200) {
            parseMessages(req.responseXML);
        }
    }
}

function appendevent(eventName) {
    var row;
    var cell;
    var linkElement;
    
    if (isIE) {
        completeTable.style.display = 'block';
        row = completeTable.insertRow(completeTable.rows.length);
        cell = row.insertCell(0);
    } else {
		//window.alert("getting childnodes1!")
        completeTable.style.display = 'table';
		//window.alert("getting childnodes2!")
        row = document.createElement("tr");
		//window.alert("getting childnodes3!")
        cell = document.createElement("td");
		//window.alert("getting childnodes4!")
        row.appendChild(cell);
		//window.alert("getting childnodes5!")
        completeTable.appendChild(row);
		//window.alert("getting childnodes6!")
    }

    cell.className = "popupCell";
//window.alert("getting childnodes7!")
    linkElement = document.createElement("a");
	//window.alert("getting childnodes8!")
    linkElement.className = "popupItem";
	//window.alert("getting childnodes9!")
    linkElement.setAttribute("href", "autocomplete?action=lookup&searchId=" + eventName);
	//linkElement.setAttribute("style", "color:#000000;");

	////window.alert("getting childnodes10!")
    linkElement.appendChild(document.createTextNode(eventName));
	////window.alert("getting childnodes11!")
    cell.appendChild(linkElement);
	

}

function clearTable() {
    if (completeTable.getElementsByTagName("tr").length > 0) {
        completeTable.style.display = 'none';
        for (var loop = completeTable.childNodes.length -1; loop >= 0 ; loop--) {
            completeTable.removeChild(completeTable.childNodes[loop]);
        }
    }
}


function parseMessages(responseXML) {
    
    // no matches returned
    if (responseXML == null) {
        return false;
    } else {

        var events = responseXML.getElementsByTagName("events")[0];
		var size=events.childNodes.length;
        if ( size> 0) {
			
            completeTable.setAttribute("bordercolor", "black");
            completeTable.setAttribute("border", "1");
            for (var loop = 0; loop<size; loop++) {
				
                var evente = events.childNodes[loop];
                var eventName = evente.getElementsByTagName("eventName")[0];
				appendevent(eventName.childNodes[0].nodeValue);
            }
        }
    }
}