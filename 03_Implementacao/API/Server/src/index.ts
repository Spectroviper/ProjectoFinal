import express, { Request, Response } from 'express';
import { ApolloServer } from '@apollo/server';
import { expressMiddleware } from '@apollo/server/express4';
import cors from 'cors';
import path from 'path';
import { schema } from './Schema';
import { upload } from './middleware/upload';
import { appDataSource } from './DataSource';

const main = async () => {
  try {
    await appDataSource.initialize();
    console.log('Data Source has been initialized!');

    const app = express();

    app.use(cors());

    app.use(express.json());

    app.use('/Images', express.static(path.join(__dirname, 'Images')));

    app.post('/Images', upload.single('file'), (req: Request, res: Response) => {
      if (!req.file) {
        res.status(400).send('No file uploaded.');
        return;
      }
      res.json({ filePath: `/Images/${req.file.filename}` });
    });

    const server = new ApolloServer({ schema });

    await server.start();

    app.use(
      '/graphql',
      cors(),
      express.json(),
      expressMiddleware(server, {
        context: async ({ req }) => ({ req }),
      })
    );

    const PORT = process.env.SERVER_PORT ? parseInt(process.env.SERVER_PORT) : 3000;

    app.listen(PORT, () => {
      console.log(`🚀 Server running at http://localhost:${PORT}/graphql`);
      console.log(`📁 File uploads served at http://localhost:${PORT}/Images`);
    });
  } catch (error) {
    console.error('Error during Data Source initialization or server start:', error);
  }
};

main();
