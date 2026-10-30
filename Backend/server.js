require('dotenv').config();

const express = require('express');
const app = express();
const PORT = 1995;

const pool = require('./db/database');

// Parse
app.use(express.text());
app.use(express.json());

// Homepage
app.use(express.static('public'));

// Start Server
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});

//-------------Routes -----------------------

// READ
app.get('/data/bikes', async (req, res) => {
    try {
        const result = await pool.query('SELECT * FROM bikes');
        res.json(result.rows);
    } catch (error) {
        console.error(error);
        res.status(500).send('Database error');
    }
});


// CREATE
app.post('/data/bikes', async (req, res) => {
    const { brand, model, type, condition, status } = req.body;

    try {
        const result = await pool.query(
            `INSERT INTO bikes (brand, model, type, condition, status)
             VALUES ($1, $2, $3, $4, $5)
             RETURNING *`,
            [brand, model, type, condition, status]
        );

        res.status(201).json(result.rows[0]);
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Database error' });
    }
});

// Delete
app.delete('/data/bikes/:id', async (req, res) => {
    const id = req.params.id;

    try {
        const result = await pool.query(
            'DELETE FROM bikes WHERE id = $1 RETURNING *',
            [id]
        );

        if (result.rows.length === 0) {
            return res.status(404).json({ error: "Bike not found" });
        }

        res.json({ message: "Bike deleted successfully" });

    } catch (error) {
        console.error(error);
        res.status(500).json({ error: "Failed to delete bike" });
    }
});