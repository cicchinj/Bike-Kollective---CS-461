const express = require('express');
const app = express();
const PORT = 1995;

const bikes = require('./data/bikes.js');

// Parse
app.use(express.text());
app.use(express.json());

// Serve Files
app.use(express.static('public'));

// Routes
app.get('/data/bikes', (req, res) => {
    res.json(bikes);
});

// Start Server
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});