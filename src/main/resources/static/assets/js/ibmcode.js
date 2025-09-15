/*jshint multistr:true */

(function (ibmcode, undefined) {

	//public methods
	ibmcode.$ = function (selector) {
		return document.querySelectorAll(selector);
	};
	ibmcode.addEvent = function (el, type, handler) {
		if (el.attachEvent) {
			el.attachEvent('on' + type, handler);
		} else {
			el.addEventListener(type, handler);
		}
	};
	ibmcode.removeEvent = function (el, type, handler) {
		if (el.detachEvent) {
			el.detachEvent('on' + type, handler);
		} else {
			el.removeEventListener(type, handler);
		}
	};


	ibmcode.copyText = function (element) {
		var range, selection, worked;

		if (document.body.createTextRange) {
			range = document.body.createTextRange();
			range.moveToElementText(element);
			range.select();
		} else if (window.getSelection) {
			selection = window.getSelection();
			range = document.createRange();
			range.selectNodeContents(element);
			selection.removeAllRanges();
			selection.addRange(range);
		}

		try {
			document.execCommand('copy');
		}
		catch (err) {
			alert('unable to copy text');
		}
	}


	ibmcode.getClosest = function (elem, selector) {
		// Element.matches() polyfill
		if (!Element.prototype.matches) {
			Element.prototype.matches =
				Element.prototype.matchesSelector ||
				Element.prototype.mozMatchesSelector ||
				Element.prototype.msMatchesSelector ||
				Element.prototype.oMatchesSelector ||
				Element.prototype.webkitMatchesSelector ||
				function (s) {
					var matches = (this.document || this.ownerDocument).querySelectorAll(s),
						i = matches.length;
					while (--i >= 0 && matches.item(i) !== this) { }
					return i > -1;
				};
		}
		for (; elem && elem !== document; elem = elem.parentNode) {
			if (elem.matches(selector)) {
				return elem;
			}
		}
		return null;
	};

	ibmcode.widgetSetSameHeight = function ($collection, resizing, minHeight) {
		minHeight = minHeight || 0;
		[].forEach.call($collection, function (el) {
			var $children = el.querySelectorAll(el.dataset.items);
			var $maxHeight = 0;
			[].forEach.call($children, function (el) {
				if (resizing && resizing === true) {
					el.style.height = 'auto';
				}
				if (el.offsetHeight > $maxHeight) {
					$maxHeight = el.offsetHeight;
				}
			});
			//now go back through the elements and set their heights
			if ($maxHeight<minHeight) $maxHeight = minHeight;
			console.log("????????????> ibmcode.widgetSetSameHeight $maxHeight="+$maxHeight+", minHeight="+minHeight);
			[].forEach.call($children, function (el) {
				el.style.height = $maxHeight + 'px';
			});
		});
	};

	ibmcode.getAjax = function (url, callback) {
		var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP');
		xhr.open('GET', url);
		xhr.onreadystatechange = function () {
			if (xhr.readyState > 3 && xhr.status === 200 && typeof callback === "function") {
				callback(xhr.responseText);
			}
		};
		xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
		xhr.send();
		return xhr;
	};
	ibmcode.postAjax = function (url, data, callback) {
		var params;
		if (typeof data === 'string') {
			params = data;
		} else {
			params = Object.keys(data).map(function (k) {
				return encodeURIComponent(k) + '=' + encodeURIComponent(data[k]);
			}).join('&');
		}
		var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP");
		xhr.open('POST', url);
		xhr.onreadystatechange = function () {
			if (xhr.readyState > 3 && xhr.status === 200) {
				callback(xhr.responseText);
			}
		};
		xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xhr.send(params);
		return xhr;
	};
	ibmcode.postJSON = function (url, data, callback) {
		var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP");
		xhr.open("POST", url, true);
		xhr.onreadystatechange = function () {
			if (xhr.readyState > 3 && xhr.status === 200) {
				callback(xhr.responseText);
			}
		}
		xhr.setRequestHeader("Content-type", "application/json");
		data = JSON.stringify(data);
		xhr.send(data);
	}

	ibmcode.socialpopup = function (elm) {
		var winpops = window.open(elm.href, "", "width=800,height=600,status,scrollbars,resizable");
	};

	ibmcode.setCookie = function (name, value, days) {
		var expires = "";
		if (days) {
			var date = new Date();
			date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
			expires = "; expires=" + date.toUTCString();
		};
		document.cookie = name + "=" + (value || "") + expires + "; path=/";
	};

	ibmcode.getCookie = function (name) {
		var nameEQ = name + "=";
		var ca = document.cookie.split(';');
		for (var i = 0; i < ca.length; i++) {
			var c = ca[i];
			while (c.charAt(0) == ' ') c = c.substring(1, c.length);
			if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
		}
		return null;
	};

	ibmcode.eraseCookie = function (name) {
		document.cookie = name + '=; Max-Age=-99999999;';
	};

}(window.ibmcode = window.ibmcode || {}));