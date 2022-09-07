// Color code in Lode's CG Tutorial

var elements = document.getElementsByClassName("code");

for(var i = 0; i < elements.length; i++) {
  elements[i].innerHTML = elements[i].innerHTML.replace(/\"(.*?)\"/g, '<span style="color:red">"$1"</span>');
  elements[i].innerHTML = elements[i].innerHTML.replace(/\/\*([\s\S]*)\*\//g, '<span style="color:#009">/*$1*/</span>');
  elements[i].innerHTML = elements[i].innerHTML.replace(/\/\/(.*)(\n|$)/g, '<span style="color:#009">//$1$2</span>');
}
