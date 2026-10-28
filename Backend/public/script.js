async function loadBikes() {
    const response = await fetch('/data/bikes');
    const bikes = await response.json();

    document.getElementById('bikeList').textContent =
        JSON.stringify(bikes, null, 2);
}