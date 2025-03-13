import express from 'express';
import { graphqlHTTP } from 'express-graphql';
import { schema } from "./Schema";
import cors from 'cors';
import dotenv from 'dotenv';
import { DataSource } from 'typeorm';
import { Persons } from './Entities/Persons';
import { Games } from './Entities/Games';
import { Achievements } from './Entities/Achievements';
import { PersonAchievements } from './Entities/PersonAchievements';

dotenv.config();

const main = async () => {

    const appDataSource = new DataSource({
        type: "mysql",
        host: "database",
        username: "admin",
        port: 3306,
        password: "admin1234",
        database: "admin",
        logging: true,
        synchronize: false,
        entities: [Persons, Games, Achievements, PersonAchievements],
    });

    await appDataSource.initialize().then(() =>{
        console.log("Database ON");
    }).catch((error) =>{
        console.error("Database", error); 
    });

    const app = express()
    app.use(cors())
    app.use(express.json())
    app.use("/graphql", graphqlHTTP({
        schema,
        graphiql: true
    }))

    const PORT = process.env.PORT ?? 3000;

    app.listen(PORT, () =>{
        console.log(`SERVER RUNNING ON PORT ${PORT}`);
    });

}

main().catch((err) => {
    console.log(err);
});