function initZoom(containerId) {
	const container = d3.select(`#${containerId}`);
	const zoom = d3.zoom()
		.scaleExtent([0.5, 10]) // Adjust the scale extent as needed
		.on('zoom', handleZoom);
	container.call(zoom);
	addToZoomList(containerId, zoom)
}

function handleZoom(event) {
	const containerId = d3.select(this).attr('id');
	const currentTransform = event.transform;
	d3.select(`#${containerId} svg`).attr('transform', currentTransform);
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
				if (mutation.target.nodeName == "PRE") {
					preCnt++;
					//console.log(preCnt);
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
	const container = d3.select(`#${containerId}`);
	const currentTransform = d3.zoomIdentity;
	container.call(d3.zoom().transform, currentTransform); // Reset the transformation
	handleZoom.call(container.node(), d3.zoomIdentity); // Call handleZoom with the reset transformation
}

function centerSvg(containerId) {
	const svg = d3.select(`#${containerId} svg`);

	// Get the dimensions of the container and the SVG
	const containerWidth = container.node().clientWidth;
	const containerHeight = container.node().clientHeight;
	const svgWidth = svg.node().getBBox().width;
	const svgHeight = svg.node().getBBox().height;

	// Calculate the translation values to center the SVG based on the current zoom level
	const scale = d3.zoomTransform(container.node()).k;
	const tx = (containerWidth - svgWidth * scale) / 2;
	const ty = (containerHeight - svgHeight * scale) / 2;

	// Create a D3 zoom transform with the calculated translation
	const transform = d3.zoomIdentity.translate(tx, ty).scale(scale);

	// Apply the transform to the container and call handleZoom
	container.call(d3.zoom().transform, transform);
	handleZoom.call(container.node(), transform);
}

function downloadSvg(containerId) {
	// Get the SVG content from the container
	var svgContainer = document.getElementById(containerId).firstElementChild;;
	var svgContent = new XMLSerializer().serializeToString(svgContainer);

	// Create an SVG blob
	var blob = new Blob([svgContent], { type: "image/svg+xml" });

	// Create a download link
	var downloadLink = document.createElement("a");
	downloadLink.href = URL.createObjectURL(blob);
	downloadLink.download = containerId + ".svg"; // Set the filename

	downloadLink.click();

}

function openSvg(containerId) {
	// Open a new window or navigate to a new HTML page
	window.open("./../../image-page.html?svg=" + containerId, "_blank");
}