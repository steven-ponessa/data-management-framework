let zoom = d3.zoom()
.on('zoom', handleZoom);

function initZoom(containerId) {
const container = d3.select(`#${containerId}`);
const zoom = d3.zoom().on('zoom', handleZoom);
container.call(zoom);
}

function handleZoom(event) {
const containerId = d3.select(this).attr('id');
d3.select(`#${containerId} svg`).attr('transform', event.transform);
}

//Mermaid.js library doesn't provide a built-in callback or event mechanism to notify you when an image is rendered. 
// As a workaround well monitor the presence of the rendered image in the DOM.   Using the `MutationObserver` API to 
// watch for changes in the DOM and trigger a callback when the Mermaid diagram is successfully rendered. 
function checkForRenderedDiagram(id) {
const diagramContainer = document.getElementById(id); // Replace with your container's ID

let preCnt = 0;

const observer = new MutationObserver(function (mutationsList) {
    for (const mutation of mutationsList) {
        if (mutation.type === "childList" && diagramContainer.contains(mutation.target)) {
            //console.log("mutation.target="+mutation.target.nodeName+", "+mutation.target.nextSibling)
            if (mutation.target.nodeName == "PRE") {
                preCnt++;
                console.log(preCnt);
            }
        }
    }
    if (preCnt > 3) {
        initZoom(id);
        // The diagram has been rendered
        // You can now perform actions or callbacks here
        observer.disconnect(); // Disconnect the observer to stop further monitoring
    }
});

observer.observe(diagramContainer, { childList: true, subtree: true });
}


document.addEventListener("DOMContentLoaded", function () {
console.log("DOMContentLoaded")
mermaid.initialize({ startOnLoad: true })
// Get all elements with the class "svg-zoomable-content"
const elements = document.querySelectorAll('.svg-zoomable-content');

// Loop through the NodeList and get the "id" values
for (let i = 0; i < elements.length; i++) {
    const element = elements[i];
    const idValue = element.id;

    if (idValue) checkForRenderedDiagram(idValue);
}
});

function restoreSvg(containerId) {
console.log('restoreSvg(' + containerId + ")")
d3.select(`#${containerId} svg`).attr('transform', "translate(0,0) scale(1)");
}

function centerSvg(containerId) {
console.log('centerSvg(' + containerId + ")")
// Select the SVG container using D3.js
var svgContainer = d3.select(`#${containerId} svg`);

// Get the width and height of the SVG container
var containerWidth = +svgContainer.attr("width");
var containerHeight = +svgContainer.attr("height");

// Calculate the center coordinates
var centerX = containerWidth / 2;
var centerY = containerHeight / 2;

// Define the zoom level to center (adjust as needed)
var zoomLevel = 1;

// Apply the zoom and pan to center the diagram
svgContainer.call(
    d3.zoom().transform,
    d3.zoomIdentity.translate(centerX, centerY).scale(zoomLevel)
);
}

function downloadSvg(containerId) {
// Get the SVG content from the container
var svgContainer = document.getElementById(containerId).firstElementChild;;
var svgContent = new XMLSerializer().serializeToString(svgContainer);

console.log("downloadSvg() .. svgContent, length="+svgContent.length);
console.log(svgContent);

// Create an SVG blob
var blob = new Blob([svgContent], { type: "image/svg+xml" });

// Create a download link
var downloadLink = document.createElement("a");
downloadLink.href = URL.createObjectURL(blob);
downloadLink.download = containerId + ".svg"; // Set the filename

downloadLink.click();

// Attach a click event listener to the button
//document.getElementById("download-svg").addEventListener("click", function () {
// Trigger the download
//downloadLink.click();
//});
}

function openSvg(containerId) {
 // Open a new window or navigate to a new HTML page
 window.open("./../../image-page.html?svg="+containerId, "_blank");
}