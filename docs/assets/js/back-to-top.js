window.onload = function() {
  var elevator = new Elevator({
    element: document.querySelector('#back-to-top'),
    targetElement: document.querySelector('#at-the-top'),
    verticalPadding: 100,  // in pixels
    duration: 1000
  });
}