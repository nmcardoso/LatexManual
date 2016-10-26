function getUrlVar(key) {
	var result = new RegExp(key + "=([^&]*)", "i").exec(window.location.search);
	return result && unescape(result[1]) || "";
}

function configureStyleSheet() {
	var fontSize = "css/fontsize-" + getUrlVar("fontsize") + ".css";
	document.getElementById('fontsize').setAttribute('href', fontSize);
}