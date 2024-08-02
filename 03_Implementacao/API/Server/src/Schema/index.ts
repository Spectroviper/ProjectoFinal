import { GraphQLObjectType, GraphQLSchema } from "graphql";
import { GET_ALL_PERSONS, GET_PERSON, GET_PERSONS_ACHIEVEMENTS, GET_PERSONS_GAMES } from './Queries/Person'
import { ADD_GAME, CREATE_PERSON, DELETE_PERSON, LOCK_ACHIEVEMENT, REMOVE_GAME, UNLOCK_ACHIEVEMENT, UPDATE_PERSON } from './Mutations/Person'
import { GET_ALL_GAMES, GET_GAME, GET_GAMES_ACHIEVEMENTS, GET_GAMES_BY_CONSOLE, GET_GAMES_BY_DEVELOPER, GET_GAMES_BY_GENRE, GET_GAMES_BY_PUBLISHER } from "./Queries/Game";
import { CREATE_GAME, DELETE_GAME, UPDATE_GAME } from "./Mutations/Game";
import { CREATE_ACHIEVEMENT, DELETE_ACHIEVEMENT, UPDATE_ACHIEVEMENT } from "./Mutations/Achievement";
import { GET_ACHIEVEMENT, GET_ALL_ACHIEVEMENTS } from "./Queries/Achievement";


const RootQuery = new GraphQLObjectType({
    name: "RootQuery",
    fields: {
        //PERSON
        getAllPersons: GET_ALL_PERSONS,
        getPerson: GET_PERSON,
        getPersonsGames: GET_PERSONS_GAMES,
        getPersonAchievements: GET_PERSONS_ACHIEVEMENTS,
        //GAME
        getAllGames: GET_ALL_GAMES,
        getGame: GET_GAME,
        getGameByConsole: GET_GAMES_BY_CONSOLE,
        getGameByPublisher: GET_GAMES_BY_PUBLISHER,
        getGameByGenre: GET_GAMES_BY_GENRE,
        getGameByDeveloper: GET_GAMES_BY_DEVELOPER,
        getGamesAchievements: GET_GAMES_ACHIEVEMENTS,
        //ACHIEVEMENT
        getAllAchievements: GET_ALL_ACHIEVEMENTS,
        getAchievement: GET_ACHIEVEMENT
    }
})

const Mutation = new GraphQLObjectType({
    name: "Mutation",
    fields: {
        //PERSON 
        createPerson: CREATE_PERSON,
        deletePerson: DELETE_PERSON,
        updatePerson: UPDATE_PERSON,
        addGame: ADD_GAME,
        removeGame: REMOVE_GAME,
        unlockAchievement: UNLOCK_ACHIEVEMENT,
        lockAchievement: LOCK_ACHIEVEMENT,
        //GAME
        createGame: CREATE_GAME,
        deleteGame: DELETE_GAME,
        updateGame: UPDATE_GAME,
        //ACHIEVEMENT
        createAchievemet: CREATE_ACHIEVEMENT,
        deleteAchievemet: DELETE_ACHIEVEMENT,
        updateAchievement: UPDATE_ACHIEVEMENT
    }
})


export const schema = new GraphQLSchema({
    query: RootQuery,
    mutation: Mutation
});