import express from "express";
import routes from './routes/routes'; // ajuste o caminho conforme necessário

const app = express();

app.use(express.json());

// Rota raiz
app.get('/', (req, res) => {
    res.send('API is running');
});

// Rotas de usuários
app.use('/users', routes);

export default app;
