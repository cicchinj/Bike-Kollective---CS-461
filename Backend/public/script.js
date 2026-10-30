async function loadBikes() {
    const response = await fetch('/data/bikes');
    const bikes = await response.json();

    document.getElementById('bikeList').textContent = JSON.stringify(bikes, null, 2);
}


async function addBike(event) {
    event.preventDefault();

    const bike = {
        brand: document.getElementById('brand').value,
        model: document.getElementById('model').value,
        type: document.getElementById('type').value,
        condition: document.getElementById('condition').value,
        status: document.getElementById('status').value
    };

    const response = await fetch('/data/bikes', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bike)
    });

    const result = await response.json();
    console.log(result);
}
