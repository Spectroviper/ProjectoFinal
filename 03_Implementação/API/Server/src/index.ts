import express from 'express';
import { graphqlHTTP } from 'express-graphql';
import { schema } from "./Schema";
import cors from 'cors';
import { DataSource } from 'typeorm';
import { Persons } from './Entities/Persons';
import { Games } from './Entities/Games';
import { Achievements } from './Entities/Achievements';
import { PersonAchievements } from './Entities/PersonAchievements';

const main = async () => {

    const appDataSource = new DataSource({
        type: "mysql",
        host: "127.0.0.1",
        username: "root",
        port: 3306,
        password: "EXAMPLE",
        database: "EXAMPLE",
        logging: true,
        synchronize: false,
        entities: [Persons, Games, Achievements, PersonAchievements],
    });

    await appDataSource.initialize();

    const app = express()
    app.use(cors())
    app.use(express.json())
    app.use("/graphql", graphqlHTTP({
        schema,
        graphiql: true
    }))

    app.listen(3001, () =>{
        console.log("SERVER RUNNING ON PORT 3001");
    });

}

main().catch((err) => {
    console.log(err);
});