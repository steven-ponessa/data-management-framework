function validateSearchBox() {
    var searchBox = document.getElementById("search-box");
    if (searchBox.value == "") {
      alert("Please enter a search term");
      return false;
    } else {
      return true;
    }
  }